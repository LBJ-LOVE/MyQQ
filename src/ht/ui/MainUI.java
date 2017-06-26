package ht.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListModel;

import org.w3c.dom.events.MouseEvent;

import ht.bean.Account;
import ht.common.Cmd;
import ht.common.SendMsg;
import ht.common.SendSocket;
import ht.common.Sound;
import ht.db.DBOper;

public class MainUI extends JFrame implements MouseListener,ActionListener,WindowListener,ItemListener{
	private Account acc,friendAcc;
	private JTabbedPane tab;
	private JLabel lblHead;
	private JList friend,family,classmate,hmd;
	private JButton btnFind;
	private Vector<Account> vFriend,vFamily,vClassmate,vHmd,vAllDetail;
	private JPopupMenu pop;
	private JComboBox cbStatus;
	private JMenuItem miChat,miLookInfo,miDel,miMovFri,miMovFal,miMovCla,miMovHmd;
	private Hashtable<Integer, ChatUI> ht_ChatUsers=null;
	
	public MainUI(Account acc){
		this.acc=acc;
		setTitle(acc.getNickName());
		setIconImage(new ImageIcon(acc.getFaceImage()).getImage());
		//获取昵称 备注 QQ号码 个性签名
		String str=acc.getNickName()+"("+acc.getQQNum()+")";
		lblHead=new JLabel(str,new ImageIcon(acc.getFaceImage()),JLabel.LEFT);
		add(lblHead,BorderLayout.NORTH);
		String[] status={"在线","离线","隐身","忙碌"};
		cbStatus=new JComboBox(status);
		vFriend=new Vector<Account>();
		vFamily=new Vector<Account>();
		vClassmate=new Vector<Account>();
		vHmd=new Vector<Account>();
		family=new JList();
		friend=new JList();
		classmate=new JList();
		hmd=new JList();
		friend.addMouseListener(this);
		family.addMouseListener(this);
		classmate.addMouseListener(this);
		hmd.addMouseListener(this);
		refresh();
		tab=new JTabbedPane();
		tab.addTab("家人", new JScrollPane(family));
		tab.addTab("好友", new JScrollPane(friend));
		tab.addTab("同学", new JScrollPane(classmate));
		tab.addTab("黑名单", new JScrollPane(hmd));
		add(tab);
		createMenu();
		btnFind=new JButton("查找好友");
		//add(cbStatus,);
		JPanel southPanel=new JPanel(new GridLayout(1, 2, 0, 0));
		southPanel.add(btnFind);
		southPanel.add(cbStatus);
		add(southPanel,BorderLayout.SOUTH);
		cbStatus.addItemListener(this);
		btnFind.addActionListener(this);
		setSize(250,670);
		setVisible(true);
		setResizable(false);
		int width=Toolkit.getDefaultToolkit().getScreenSize().width-280;
		setLocation(width,50);
		addWindowListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//启动线程
		new ReceThread().start();
		call(Cmd.CMD_ONLINE);
		System.out.println("主界面发送在线");
	}
	//设置文本内容
	class listmodel extends AbstractListModel { //继承AbstractListModel类，才能显示图片
		Vector dats;
		public listmodel(Vector dats) {
			this.dats = dats;
		}
		// 获取行数
		public Object getElementAt(int index) {
			Account user = (Account) dats.get(index);
			return user.getNickName().trim() + "(" + user.getQQNum() + ")";
		}
		// 获取长度
		public int getSize() {
			return dats.size();
		}
	}
		// 获取好友头像
	class myfind extends DefaultListCellRenderer {
		Vector datas;
		public myfind(Vector datas) {
			this.datas = datas;
		}
			
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Component c = super.getListCellRendererComponent(list, value,
					index, isSelected, cellHasFocus);
			if (index >= 0 && index < datas.size()) {
				Account user = (Account) datas.get(index);
				// 给列表中好友状态设置头像
				String status = user.getStatus();
				String filename=user.getFaceImage();//face/6.png
				String file ="";
				if (status.equals(Cmd.STATUS_ONLINE)) {
					file=filename;
				} else if(status.equals(Cmd.STATUS_LEAVE)){
					file = filename.substring(0, filename.indexOf('.'))+ "_h.png";	
				}else if(status.equals(Cmd.STATUS_BUSY)){
					file = filename.substring(0, filename.indexOf('.'))+ "_w.png";		
				}else if(status.equals(Cmd.STATUS_HIDDEN)){
					file = filename.substring(0, filename.indexOf('.'))+ "_l.png";
				}
				setIcon(new ImageIcon(file));
				setText(user.getNickName().trim() + "(" + user.getQQNum() + ")");
				}
			// 设置字体颜色
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			setEnabled(list.isEnabled());
			setFont(list.getFont());
			setOpaque(true);
			return this;
		}
	}
	//刷新界面 得到好友资料
	public void refresh(){
		vAllDetail=new DBOper().getAllinfo(acc);
		//清除以前所有记录
		vFriend.clear();
		vFamily.clear();
		vClassmate.clear();
		vHmd.clear();
		//把数据库中的最新数据分别放到对应的向量中
		for(int i=0;i<vAllDetail.size();i++){
			Account a=vAllDetail.get(i);
			if(a.getGroupName().equals(Cmd.TYPE_FRIEND)){
				vFriend.add(a);
			}else if(a.getGroupName().equals(Cmd.TYPE_FAMILY)){
				vFamily.add(a);
			}else if(a.getGroupName().equals(Cmd.TYPE_CLASSMATE)){
				vClassmate.add(a);
			}else if(a.getGroupName().equals(Cmd.TYPE_BLACK)){
				vHmd.add(a);
			}
		}
		//把向量放在list控件中
		friend.setModel(new listmodel(vFriend));			//显示好友资料
		friend.setCellRenderer(new myfind(vFriend));		//显示好友头像
		family.setModel(new listmodel(vFamily));			//显示家人资料
		family.setCellRenderer(new myfind(vFamily));		//显示家人头像
		classmate.setModel(new listmodel(vClassmate));		//显示同学资料
		classmate.setCellRenderer(new myfind(vClassmate));	//显示同学头像
		hmd.setModel(new listmodel(vHmd));                  //显示黑名单资料
		hmd.setCellRenderer(new myfind(vHmd));              //显示黑名单头像
	}
	//创建菜单
	public void createMenu(){
		pop=new JPopupMenu();
		miChat=new JMenuItem("聊天");
		miChat.addActionListener(this);
		miLookInfo=new JMenuItem("查看资料");
		miLookInfo.addActionListener(this);
		miDel=new JMenuItem("删除好友");
		miDel.addActionListener(this);
		miMovFri=new JMenuItem("移动到好友");
		miMovFri.addActionListener(this);
		miMovFal=new JMenuItem("移动到家人");
		miMovFal.addActionListener(this);
		miMovCla=new JMenuItem("移动到同学");
		miMovCla.addActionListener(this);
		miMovHmd=new JMenuItem("移动到黑名单");
		miMovHmd.addActionListener(this);
		pop.add(miChat);
		pop.add(miLookInfo);
		pop.add(miDel);
		pop.add(miMovFri);
		pop.add(miMovFal);
		pop.add(miMovCla);
		pop.add(miMovHmd);
	}
	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {
		if(e.getSource()==friend){
			friendAcc=(Account) vFriend.get(friend.getSelectedIndex());
			//双击
			if(e.getClickCount()==2){
				findWin(friendAcc.getQQNum(),null);
			}
			//右键
			if(e.getButton()==3){
				if(friend.getSelectedIndex()>=0)
					pop.show(friend, e.getX(), e.getY());
			}
		}
		if(e.getSource()==family){
			friendAcc=(Account) vFamily.get(family.getSelectedIndex());
			//双击
			if(e.getClickCount()==2){
				findWin(friendAcc.getQQNum(),null);
			}
			//右键
			if(e.getButton()==3){
				if(family.getSelectedIndex()>=0)
					pop.show(family, e.getX(), e.getY());
			}
		}
		if(e.getSource()==classmate){
			friendAcc=(Account) vClassmate.get(classmate.getSelectedIndex());
			//双击
			if(e.getClickCount()==2){
				findWin(friendAcc.getQQNum(),null);
			}
			//右键
			if(e.getButton()==3){
				if(classmate.getSelectedIndex()>=0)
					pop.show(classmate, e.getX(), e.getY());
			}
		}
		if(e.getSource()==hmd){
			friendAcc=(Account) vHmd.get(hmd.getSelectedIndex());
			//双击
			if(e.getClickCount()==2){
				findWin(friendAcc.getQQNum(),null);
			}
			//右键
			if(e.getButton()==3){
				if(hmd.getSelectedIndex()>=0)
					pop.show(hmd, e.getX(), e.getY());
			}
		}
	}
	@Override
	public void mouseEntered(java.awt.event.MouseEvent e) {
		
	}
	@Override
	public void mouseExited(java.awt.event.MouseEvent e) {
		
	}
	@Override
	public void mousePressed(java.awt.event.MouseEvent e) {
		
	}
	@Override
	public void mouseReleased(java.awt.event.MouseEvent e) {
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnFind){
			new FindUI(acc);
		}else if(e.getSource()==miChat){
			findWin(friendAcc.getQQNum(),null);
		}else if(e.getSource()==miLookInfo){
			if(friendAcc!=null)
				new LookInfo(friendAcc);
		}else if(e.getSource()==miDel){
//			if(friendAcc!=null)
//				new LookInfo(friendAcc);
		}else if(e.getSource()==miMovFri){
//			if(friendAcc!=null)
//				new LookInfo(friendAcc);
		}else if(e.getSource()==miMovFal){
//			if(friendAcc!=null)
//				new LookInfo(friendAcc);
		}else if(e.getSource()==miMovCla){
//			if(friendAcc!=null)
//				new LookInfo(friendAcc);
		}else if(e.getSource()==miMovHmd){
//			if(friendAcc!=null)
//				new LookInfo(friendAcc);
		}
	}
	//查找窗口是否存在，不存在则创建，存在则直接显示信息在界面
	public ChatUI findWin(Integer qqcode,SendMsg msg){
		ChatUI chat=null;
		//窗口是否存在,若不存在则返回空值
		chat=ht_ChatUsers.get(qqcode);
		if(chat==null){//不存在则创建聊天窗口
			if(msg==null)//双击或者右键打开窗口
				chat=new ChatUI(acc,friendAcc);
			else//线程打开窗口
				chat=new ChatUI(msg.friendAcc,msg.myAcc);
			//存入表中
			ht_ChatUsers.put(qqcode, chat);
		}
		if(!chat.isVisible()){
			chat.show();
		}
		return chat;
	}
	//上线 离线等通知
	public void call(int cmd){
		for(int i=0;i<vAllDetail.size();i++){
			Account a=vAllDetail.get(i);
			//
			if(a.getQQNum()!=acc.getQQNum()&&a.getStatus()!=Cmd.STATUS_LEAVE){
				//生成发送类
				SendMsg msg=new SendMsg();
				msg.myAcc=acc;
				msg.friendAcc=a;
				msg.Cmd=cmd;
				//发送
				new SendSocket().send(msg);
			}

		}
	}
	//接收消息的线程
	class ReceThread extends Thread{	
		public ReceThread(){
			ht_ChatUsers=new Hashtable<Integer,ChatUI>();
		}
		@Override
		public void run(){
			try {
				System.out.println("线程已启动....");			
				//在自己的端口接收数据
				DatagramSocket serverSocket=new DatagramSocket(acc.getPort());		
				//接收客户端发来的信息
				while(true){
					byte[] b=new byte[1024*70];
					DatagramPacket pack=new DatagramPacket(b, b.length);
					serverSocket.receive(pack);
					//把字节数组转出成SendMsg对象
					ByteArrayInputStream bis=new ByteArrayInputStream(b,0,pack.getLength());
					ObjectInputStream ois=new ObjectInputStream(bis);
					SendMsg msg=(SendMsg) ois.readObject();
					
					switch(msg.Cmd){
						case Cmd.CMD_ONLINE:
							System.out.println("收到CMD_ONLINE");
							refresh();
							new Sound();
							//显示上线提示窗口
							new TipUI(msg.myAcc);
							break;
						case Cmd.CMD_LEAVE:
							System.out.println("收到CMD_LEAVE");
							refresh();
							break;
						case Cmd.CMD_BUSY:
							System.out.println("收到CMD_BUSY");
							refresh();
							break;
						case Cmd.CMD_HIDDEN://以上三种状态均执行refresh()
							System.out.println("收到CMD_HIDDEN");
							refresh();
							break;
						case Cmd.CMD_SEND:
							ChatUI chat=findWin(msg.myAcc.getQQNum(),msg);
							//获取聊天消息，显示在窗口
							chat.appendView(msg.myAcc.getNickName(), msg.doc);
							break;
						case Cmd.CMD_SHAKE:
							ChatUI chat1=findWin(msg.myAcc.getQQNum(),msg);
							chat1.shake();
							break;
						case Cmd.CMD_ADDFRIEND:
							String str= msg.myAcc.getNickName()+"添加你为好友，请确认！";
							SendMsg m=new SendMsg();
							m.friendAcc=msg.myAcc;
							m.myAcc=msg.friendAcc;
							//如果点击确认按钮则添加
							if(JOptionPane.showConfirmDialog(null, str,"添加好友",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
								//firend表中添加两条互为好友的记录
								new DBOper().addFriend(msg.friendAcc, msg.myAcc.getQQNum());
								refresh();
								m.Cmd=Cmd.CMD_AGREE;
							}else{
								m.Cmd=Cmd.CMD_REJECT;
							}
							new SendSocket().send(m);
							break;
						case Cmd.CMD_AGREE:
							refresh();
							break;
						case Cmd.CMD_REJECT:
							JOptionPane.showMessageDialog(null, msg.myAcc.getNickName()+"拒绝了你的好友请求");							
							break;		
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		//System.out.println("窗口关闭");
		new DBOper().changeStatus(acc.getQQNum(), Cmd.STATUS_LEAVE);
		//发送离线消息
		call(Cmd.CMD_LEAVE);
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange()==ItemEvent.SELECTED){
			String status = acc.getStatus();
			String filename=acc.getFaceImage();//face/6.png
			String file ="";
			new DBOper().changeStatus(acc.getQQNum(), cbStatus.getSelectedItem().toString().trim());
			//发送离线消息
			switch (cbStatus.getSelectedItem().toString().trim()) {
			case Cmd.STATUS_ONLINE:
				file=filename;
				call(Cmd.CMD_ONLINE);
				System.out.println("发送CMD_ONLINE");
				break;
			case Cmd.STATUS_LEAVE:
				file = filename.substring(0, filename.indexOf('.'))+ "_h.png";
				call(Cmd.CMD_LEAVE);
				System.out.println("发送CMD_LEAVE");
				break;
			case Cmd.STATUS_BUSY:
				file = filename.substring(0, filename.indexOf('.'))+ "_w.png";
				call(Cmd.CMD_BUSY);
				System.out.println("发送CMD_BUSY");
				break;
			case Cmd.STATUS_HIDDEN:
				file = filename.substring(0, filename.indexOf('.'))+ "_l.png";	
				call(Cmd.CMD_HIDDEN);
				System.out.println("发送CMD_HIDDEN");
				break;
			}
			lblHead.setIcon(new ImageIcon(file));
			System.out.println("本人头像改变");
		}
	}
}