package ht.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;

import ht.bean.Account;
import ht.common.Cmd;
import ht.common.SendMsg;
import ht.common.SendSocket;
import ht.db.DBOper;

public class FindUI extends JFrame implements ActionListener{
	private JTextField txtQQ,txtNickname,txtAge;
	private JComboBox cbSex;
	private JTable dataTable;
	private JButton btnFind,btnClose,btnAdd;
	private Vector vdata;
	private Vector<String> vhead;
	private Account account;
	myTable mytable=null;
	
	public FindUI(Account account) {
		super("查找好友");
		this.account=account;
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JPanel topPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel lblQQ=new JLabel("QQ号：");
		JLabel lblNickname=new JLabel("昵称：");
		JLabel lblAge=new JLabel("年龄：");
		JLabel lblSex=new JLabel("性别：");
		txtQQ=new JTextField(20);
		txtNickname=new JTextField(20);
		txtAge=new JTextField(10);
		cbSex=new JComboBox(new String[]{"男","女"});
		btnFind=new JButton("查找（F）");
		btnFind.setMnemonic('F');
		topPanel.add(lblQQ);
		topPanel.add(txtQQ);
		topPanel.add(lblNickname);
		topPanel.add(txtNickname);
		topPanel.add(lblAge);
		topPanel.add(txtAge);
		topPanel.add(lblSex);
		topPanel.add(cbSex);
		topPanel.add(btnFind);
		btnFind.addActionListener(this);
		add(topPanel,BorderLayout.NORTH);
		//表头
		vhead = new Vector<String>();
		vhead.addElement("QQ号码");
		vhead.addElement("昵称");
		vhead.addElement("性别");
		vhead.addElement("年龄");
		vhead.addElement("民族");
		vhead.addElement("头像");
		vhead.addElement("IP地址");
		vhead.addElement("端口");
		vhead.addElement("状态");
		vdata=new Vector();
		mytable=new myTable(vdata,vhead);
		dataTable=new JTable(mytable);
		add(new JScrollPane(dataTable));
		
		btnClose=new JButton("关闭（X）");
		btnClose.setMnemonic('X');
		btnAdd=new JButton("添加（A）");
		btnAdd.setMnemonic('A');
		JPanel bottomPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bottomPanel.add(btnAdd);
		bottomPanel.add(btnClose);
		add(bottomPanel,BorderLayout.SOUTH);
		btnAdd.addActionListener(this);
		btnClose.addActionListener(this);
		
		setSize(700,400);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
	}
	public static void main(String[] args) {
//		new FindUI();
	}
	public void find(Account acc){
		vdata=new DBOper().find(acc);
		mytable.vdata=vdata;
		dataTable.updateUI();//刷新界面
	}
	//设置Jtable的值 
	class myTable extends AbstractTableModel{
		Vector vdata,vhead;
		//构造函数传进来表头和数据
		public myTable(Vector vdata,Vector<String> vhead) {
			this.vdata=vdata;
			this.vhead=vhead;
		}
		@Override//返回列数
		public int getColumnCount() {
			return vhead.size();
		}
		@Override//获取列名
		public String getColumnName(int columnIndex){
			return vhead.get(columnIndex).toString();
		}
		@Override//返回行数
		public int getRowCount() {
			return vdata.size();
		}
		@Override//每一列要显示的数据类型
		public Object getValueAt(int rowIndex, int columnIndex) {
			Vector v=(Vector) vdata.get(rowIndex);
			if(columnIndex==5)//头像列返回图片
				return new ImageIcon(v.get(columnIndex).toString());
			else
				return v.get(columnIndex);
		}	
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnFind){
			Account acc=new Account();
			if(!txtQQ.getText().equals(""))
				acc.setQQNum(Integer.valueOf(txtQQ.getText().trim()));
			if(!txtNickname.getText().equals(""))
				acc.setNickName(txtNickname.getText().trim());
			if(!txtAge.getText().equals(""))
				acc.setAge(Integer.valueOf(txtAge.getText().trim()));
			acc.setSex(cbSex.getSelectedItem().toString());
			find(acc);
		}else if(e.getSource()==btnAdd){
			int row,col;
			row=dataTable.getSelectedRow();
//			col=dataTable.getSelectedColumn();
			Vector v=(Vector) vdata.get(row);
			int qqcode=(int) v.get(0);
			Account acc=new DBOper().findByQQcode(qqcode);
			SendMsg msg=new SendMsg();
			msg.Cmd=Cmd.CMD_ADDFRIEND;
			msg.friendAcc=acc;
			msg.myAcc=account;
			new SendSocket().send(msg);
//			JOptionPane.showMessageDialog(null, "row="+dataTable.getSelectedRow()+";column="+dataTable.getSelectedColumn());
		}else if(e.getSource()==btnClose){
			dispose();
		}
	}
}
