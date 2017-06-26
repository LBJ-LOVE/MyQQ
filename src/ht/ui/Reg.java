package ht.ui;

import ht.bean.Account;
import ht.common.Cmd;
import ht.db.DBOper;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;


public class Reg extends JFrame implements ActionListener{
	private Account acc;
	private JLabel lblTitle,lblUserName,lblNickName,lblPwd,lblCfgPwd,lblAge,lblSex,lblNation;
	private JLabel lblFace,lblIp,lblEmal,lblAddr;
	private JLabel lblbg;
	private JTextField txtUserName,txtNickName,txtAge,txtIp,txtEmail,txtAddr;
	private JPasswordField txtPwd,txtCfgPwd;
	private JRadioButton rbmale,rbremale;
	private JComboBox cbNation,cbFace;
	private JButton btnReg,btnCancel,btnopenFile;
	private DBOper dbop;
	String imgFiles[]={
			"faces/0.png",
			"faces/1.png",
			"faces/2.png",
			"faces/3.png",
			"faces/4.png",
			"faces/5.png",
			"faces/6.png",
			"faces/7.png",
			"faces/8.png",
			"faces/9.png",
			"faces/10.png"
	};
	
	public Reg() {
		super("QQ注册");
		try {
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.MotifLookAndFeel");
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//			com.sun.java.swing.plaf.windows.WindowsLookAndFeel
		} catch (Exception e) {
			e.printStackTrace();
		}
		setIconImage( new ImageIcon("faces/tubiao.png").getImage());
		lblbg = new JLabel(new ImageIcon("faces/beijin.jpg"));
		add(lblbg);
		//不能更改窗口的大小
		setResizable(false);
		lblTitle = new JLabel("用户注册",JLabel.CENTER);
		lblTitle.setFont(new Font("隶书",Font.BOLD,28));
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setSize(500, 30);
		lblTitle.setLocation(0, 10);
		lblbg.add(lblTitle);
		
		lblUserName = new JLabel("真实名字:",JLabel.RIGHT);
		lblUserName.setLocation(100, 50);
		lblUserName.setSize(80, 20);
		txtUserName = new JTextField();
		txtUserName.setLocation(180, 50);
		txtUserName.setSize(180, 20);

		lblNickName = new JLabel("昵称:",JLabel.RIGHT);
		lblNickName.setLocation(100, 80);
		lblNickName.setSize(80, 20);
		txtNickName = new JTextField();
		txtNickName.setLocation(180, 80);
		txtNickName.setSize(180, 20);

		lblbg.add(lblUserName);
		lblbg.add(txtUserName);
		lblbg.add(lblNickName);
		lblbg.add(txtNickName);
		lblPwd = new JLabel("登陆密码:",JLabel.RIGHT);
		lblPwd.setLocation(100, 110);
		lblPwd.setSize(80, 20);
		txtPwd = new JPasswordField();
		txtPwd.setLocation(180, 110);
		txtPwd.setSize(180, 20);
		lblCfgPwd = new JLabel("确认密码:",JLabel.RIGHT);
		lblCfgPwd.setLocation(100, 140);
		lblCfgPwd.setSize(80, 20);
		txtCfgPwd = new JPasswordField();
		txtCfgPwd.setLocation(180, 140);
		txtCfgPwd.setSize(180, 20);
		lblbg.add(lblPwd);
		lblbg.add(txtPwd);
		lblbg.add(lblCfgPwd);
		lblbg.add(txtCfgPwd);
		
		lblAge = new JLabel("年龄:",JLabel.RIGHT);
		lblAge.setLocation(100, 170);
		lblAge.setSize(80, 20);
		txtAge = new JTextField();
		txtAge.setLocation(180, 170);
		txtAge.setSize(180, 20);

		lblbg.add(lblAge);
		lblbg.add(txtAge);

		lblSex = new JLabel("性别:",JLabel.RIGHT);
		lblSex.setLocation(100, 200);
		lblSex.setSize(80, 20);
		rbmale = new JRadioButton("男");
		rbmale.setLocation(180, 200);
		rbmale.setSize(50, 20);
		rbmale.setSelected(true);//选中
		rbremale = new JRadioButton("女");
		rbremale.setLocation(230, 200);
		rbremale.setSize(90, 20);
		ButtonGroup bg = new ButtonGroup();
		bg.add(rbmale);
		bg.add(rbremale);
		lblbg.add(lblSex);
		lblbg.add(rbmale);
		lblbg.add(rbremale);
		
		lblNation = new JLabel("民族:",JLabel.RIGHT);
		lblNation.setLocation(100, 230);
		lblNation.setSize(80, 20);
		String[] snation={"汉族","苗族","壮族","回族","藏族","侗族"};
		cbNation = new JComboBox(snation);
		cbNation.setLocation(180, 230);
		cbNation.setSize(180, 20);
		lblbg.add(lblNation);
		lblbg.add(cbNation);
		
		lblFace = new JLabel("头像:",JLabel.RIGHT);
		lblFace.setLocation(100, 260);
		lblFace.setSize(80, 20);
		ImageIcon img[]={
				new ImageIcon("faces/0.png"),
				new ImageIcon("faces/1.png"),
				new ImageIcon("faces/2.png"),
				new ImageIcon("faces/3.png"),
				new ImageIcon("faces/4.png"),
				new ImageIcon("faces/5.png"),
				new ImageIcon("faces/6.png"),
				new ImageIcon("faces/7.png"),
				new ImageIcon("faces/8.png"),
				new ImageIcon("faces/9.png"),
				new ImageIcon("faces/10.png")
		};
		cbFace = new JComboBox(img);
		cbFace.setLocation(180, 260);
		cbFace.setSize(180, 65);
		
		lblbg.add(lblFace);
		lblbg.add(cbFace);
		
		lblIp = new JLabel("IP地址:",JLabel.RIGHT);
		lblIp.setLocation(100, 340);
		lblIp.setSize(80, 20);
		
		InetAddress ipAdr;
		try {
			ipAdr = InetAddress.getLocalHost();
			txtIp = new JTextField(ipAdr.getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}		
		txtIp.setLocation(180, 340);
		txtIp.setSize(180, 20);

		lblbg.add(lblIp);
		lblbg.add(txtIp);
		
		lblEmal = new JLabel("邮箱地址:",JLabel.RIGHT);
		lblEmal.setLocation(100, 370);
		lblEmal.setSize(80, 20);
		txtEmail = new JTextField();
		txtEmail.setLocation(180, 370);
		txtEmail.setSize(180, 20);
		lblbg.add(lblEmal);
		lblbg.add(txtEmail);

		lblAddr = new JLabel("家庭地址:",JLabel.RIGHT);
		lblAddr.setLocation(100, 400);
		lblAddr.setSize(80, 20);
		txtAddr = new JTextField();
		txtAddr.setLocation(180, 400);
		txtAddr.setSize(180, 20);
		lblbg.add(lblAddr);
		lblbg.add(txtAddr);

		
		btnReg = new JButton("注册");
		btnCancel = new JButton("关闭");
		btnReg.addActionListener(this);
		btnCancel.addActionListener(this);
		btnReg.setSize(80, 30);
		btnCancel.setSize(80, 30);
		btnReg.setLocation(140, 460);
		btnCancel.setLocation(280, 460);
		
		lblbg.add(btnReg);
		lblbg.add(btnCancel);
		setSize(500, 550);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//关闭当前窗口
		setLocationRelativeTo(null);
		dbop=new DBOper();;//创建数据库操作对象
	}
	
	public static void main(String[] args) {
		new Reg();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnReg){
			if(txtUserName.getText().trim().equals("")){
				JOptionPane.showMessageDialog(null, "请输入真实名字");
				txtUserName.requestFocus();
				return;
			}
			if(txtNickName.getText().trim().equals("")){
				JOptionPane.showMessageDialog(null, "请输入昵称");
				txtNickName.requestFocus();
				return;
			}
			if(txtPwd.getText().equals("")){
				JOptionPane.showMessageDialog(null, "请输入密码");
				txtPwd.requestFocus();
				return;
			}
			if(!txtPwd.getText().equals(txtCfgPwd.getText())){
				JOptionPane.showMessageDialog(null, "两次密码输入不一致");
				txtPwd.requestFocus();
				return;
			}
			String sage = txtAge.getText().trim();
			int age=0;
			try{
				age = Integer.parseInt(sage);
				if(age<0 || age>150){
					JOptionPane.showMessageDialog(null,"年龄必须为>0并且<150的数字");
					return;
				}
			}catch (Exception e1) {
				JOptionPane.showMessageDialog(null,"年龄必须为>0并且<150的数字");
				txtAge.requestFocus();//获取焦点
				return;
			}
			acc = new Account();
			System.out.println(txtUserName.getText());
			acc.setTrueName(txtUserName.getText().trim());
			acc.setNickName(txtNickName.getText().trim());
			acc.setPassword(txtPwd.getText());
			acc.setAge(age);
			if(rbmale.isSelected()) acc.setSex("男");
			if(rbremale.isSelected()) acc.setSex("女");
			acc.setNation(cbNation.getSelectedItem().toString());
			acc.setFaceImage(imgFiles[cbFace.getSelectedIndex()]);
			acc.setIp(txtIp.getText().trim());
			acc.setEmail(txtEmail.getText().trim());
			acc.setAddress(txtAddr.getText().trim());
			acc.setStatus("在线");
			acc.setPort(getPort());
			acc.setQQNum(getQQ());		
			if(dbop.addAccount(acc)>0){	
				JOptionPane.showMessageDialog(null, "注册成功,您的QQ号码是："+acc.getQQNum()+";请妥善保管。");
			}else{
				JOptionPane.showMessageDialog(null, "注册失败！");
			}
		}else if(e.getSource()==btnCancel){
			dispose();
		}
	}
	
	public int getQQ(){
		Random rd=new Random();
		int QQ=rd.nextInt(90000)+10000;
		//boolean exist=base.isExistsQQ();
		//循环判断，如果QQ号码已经存在，重新生成新的号码，再查询，直到号码不存在为止
//		while(exist){
//			int QQ=rd.nextInt(90000)+10000;
//			boolean exist=base.isExistsQQ();
//		}
		return QQ;
	}
	public int getPort(){
		Random ran = new Random();
		int port = ran.nextInt(50000)+8000;
//		boolean exist = isExistsPort(qqcode,port);
//		//循环判断，如果QQ号码已经存在，重新生成新的号码，再查询，直到号码不存在为止
//		while(exist){
//			port = ran.nextInt(50000)+8000;
//			exist = isExistsPort(qqcode,port);
//		}
		return port;
	}
}