package ht.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import javax.swing.border.Border;

import ht.bean.Account;
import ht.db.DBOper;


public class Login extends JFrame implements ActionListener,ItemListener,MouseListener{
	
	//JTextField
	JComboBox cbQQ;
	JPasswordField txtPwd;
	JLabel lblHead,lblFace,lblReg,lblFindPwd;
	JCheckBox cbmemory,cbautologin;
	JButton btnLogin;
	Account acc;
	DBOper dbop;
	Hashtable<Integer, Account> myAccount=new Hashtable<Integer,Account>(); 
	public Login(){
		super("QQ2012");
		try {
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.MotifLookAndFeel");
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//			com.sun.java.swing.plaf.windows.WindowsLookAndFeel
		} catch (Exception e) {
			e.printStackTrace();
		}
		Image icon=new ImageIcon("faces/touxiang1.png").getImage();
		setIconImage(icon);
		lblHead=new JLabel(new ImageIcon("faces/bg6.jpg"));
		add(lblHead,BorderLayout.NORTH);
		
		lblFace=new JLabel(new ImageIcon("faces/touxiang1.png"));
		lblFace.setBounds(5, 105, 90, 90);
		lblHead.add(lblFace);
		
		cbQQ=new JComboBox();
		cbQQ.setBounds(100, 120, 150, 30);
		cbQQ.setEditable(true);
		lblHead.add(cbQQ);
		cbQQ.addItemListener(this);
		readAccount();
		
		txtPwd=new JPasswordField();
		txtPwd.setBounds(100, 160, 150, 30);
		lblHead.add(txtPwd);		
		
		lblReg=new JLabel("ע���˺�");
		lblReg.setBounds(260, 120, 80, 30);
		lblReg.setForeground(Color.BLUE);
		lblHead.add(lblReg); 
		lblReg.addMouseListener(this);
		
		lblFindPwd=new JLabel("�һ�����");
		lblFindPwd.setBounds(260, 160, 80, 30);
		lblFindPwd.setForeground(Color.BLUE);
		lblHead.add(lblFindPwd);
		lblFindPwd.addMouseListener(this);

		cbmemory=new JCheckBox("��ס����");
		cbmemory.setBounds(100, 190, 80, 30);
		lblFindPwd.setForeground(Color.BLUE);
		lblHead.add(cbmemory);
		
		cbautologin=new JCheckBox("�Զ���¼");
		cbautologin.setBounds(190, 190, 80, 30);
		lblHead.add(cbautologin);
		
		btnLogin=new JButton("��  ¼");
		btnLogin.setBounds(100, 220, 150, 30);
		lblHead.add(btnLogin);
		btnLogin.addActionListener(this);
		
		setSize(380,300);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static void main(String[] args) {
		new Login();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnLogin){
			Object qqcode = cbQQ.getSelectedItem();
			String pwd = txtPwd.getText().trim();
			if(qqcode==null || qqcode.equals("")){
				JOptionPane.showMessageDialog(null, "������QQ����");
				return;
			}
			try{
				Integer.parseInt(qqcode.toString());
			}catch(Exception ex){
				JOptionPane.showMessageDialog(null, "QQ���뺬�зǷ��ַ���");
				return;
			}
			if(pwd==null || pwd.equals("")){
				JOptionPane.showMessageDialog(null, "������QQ����");
				return;
			}
			acc= new Account();
			dbop=new DBOper();
			acc.setQQNum(Integer.parseInt(qqcode.toString()));
			acc.setPassword(pwd);
			acc = dbop.login(acc);//��½
			if(acc==null){
				JOptionPane.showMessageDialog(null, "��½ʧ�ܣ�����QQ������������Ƿ�׼ȷ.");
				return;
			}
			saveAccount(acc);//����QQ���뵽�ļ�
			dispose();//�رյ�½����
			//��������
			new MainUI(acc);
		}
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource()==cbQQ){
			String qqcode=cbQQ.getSelectedItem().toString();
			String face=new DBOper().getFace(Integer.parseInt(qqcode));
			lblFace.setIcon(new ImageIcon(face));
		}		
	}
	@Override
	public void mouseClicked(MouseEvent e){
		if(e.getSource()==lblReg){
			new Reg();
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {	
	}
	//��������QQ�˺ŵ��ļ�
	public void saveAccount(Account acc) {
		myAccount.put(acc.getQQNum(), acc);
		File file=new File("account.dat");
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(myAccount);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//��ȡ�Ա����QQ�˺�
	public void readAccount(){
		File file=new File("account.dat");
		if(!file.exists()){
			return;
		}
			try {
				ObjectInputStream ois=new ObjectInputStream(new FileInputStream(file));
					myAccount=(Hashtable<Integer, Account>) ois.readObject();
					ois.close();
					Set<Integer> set=myAccount.keySet();
					Iterator it= set.iterator(); 
					while(it.hasNext()){
						cbQQ.addItem(it.next());
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}