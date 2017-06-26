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
		//��ȡ�ǳ� ��ע QQ���� ����ǩ��
		String str=acc.getNickName()+"("+acc.getQQNum()+")";
		lblHead=new JLabel(str,new ImageIcon(acc.getFaceImage()),JLabel.LEFT);
		add(lblHead,BorderLayout.NORTH);
		String[] status={"����","����","����","æµ"};
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
		tab.addTab("����", new JScrollPane(family));
		tab.addTab("����", new JScrollPane(friend));
		tab.addTab("ͬѧ", new JScrollPane(classmate));
		tab.addTab("������", new JScrollPane(hmd));
		add(tab);
		createMenu();
		btnFind=new JButton("���Һ���");
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
		//�����߳�
		new ReceThread().start();
		call(Cmd.CMD_ONLINE);
		System.out.println("�����淢������");
	}
	//�����ı�����
	class listmodel extends AbstractListModel { //�̳�AbstractListModel�࣬������ʾͼƬ
		Vector dats;
		public listmodel(Vector dats) {
			this.dats = dats;
		}
		// ��ȡ����
		public Object getElementAt(int index) {
			Account user = (Account) dats.get(index);
			return user.getNickName().trim() + "(" + user.getQQNum() + ")";
		}
		// ��ȡ����
		public int getSize() {
			return dats.size();
		}
	}
		// ��ȡ����ͷ��
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
				// ���б��к���״̬����ͷ��
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
			// ����������ɫ
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
	//ˢ�½��� �õ���������
	public void refresh(){
		vAllDetail=new DBOper().getAllinfo(acc);
		//�����ǰ���м�¼
		vFriend.clear();
		vFamily.clear();
		vClassmate.clear();
		vHmd.clear();
		//�����ݿ��е��������ݷֱ�ŵ���Ӧ��������
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
		//����������list�ؼ���
		friend.setModel(new listmodel(vFriend));			//��ʾ��������
		friend.setCellRenderer(new myfind(vFriend));		//��ʾ����ͷ��
		family.setModel(new listmodel(vFamily));			//��ʾ��������
		family.setCellRenderer(new myfind(vFamily));		//��ʾ����ͷ��
		classmate.setModel(new listmodel(vClassmate));		//��ʾͬѧ����
		classmate.setCellRenderer(new myfind(vClassmate));	//��ʾͬѧͷ��
		hmd.setModel(new listmodel(vHmd));                  //��ʾ����������
		hmd.setCellRenderer(new myfind(vHmd));              //��ʾ������ͷ��
	}
	//�����˵�
	public void createMenu(){
		pop=new JPopupMenu();
		miChat=new JMenuItem("����");
		miChat.addActionListener(this);
		miLookInfo=new JMenuItem("�鿴����");
		miLookInfo.addActionListener(this);
		miDel=new JMenuItem("ɾ������");
		miDel.addActionListener(this);
		miMovFri=new JMenuItem("�ƶ�������");
		miMovFri.addActionListener(this);
		miMovFal=new JMenuItem("�ƶ�������");
		miMovFal.addActionListener(this);
		miMovCla=new JMenuItem("�ƶ���ͬѧ");
		miMovCla.addActionListener(this);
		miMovHmd=new JMenuItem("�ƶ���������");
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
			//˫��
			if(e.getClickCount()==2){
				findWin(friendAcc.getQQNum(),null);
			}
			//�Ҽ�
			if(e.getButton()==3){
				if(friend.getSelectedIndex()>=0)
					pop.show(friend, e.getX(), e.getY());
			}
		}
		if(e.getSource()==family){
			friendAcc=(Account) vFamily.get(family.getSelectedIndex());
			//˫��
			if(e.getClickCount()==2){
				findWin(friendAcc.getQQNum(),null);
			}
			//�Ҽ�
			if(e.getButton()==3){
				if(family.getSelectedIndex()>=0)
					pop.show(family, e.getX(), e.getY());
			}
		}
		if(e.getSource()==classmate){
			friendAcc=(Account) vClassmate.get(classmate.getSelectedIndex());
			//˫��
			if(e.getClickCount()==2){
				findWin(friendAcc.getQQNum(),null);
			}
			//�Ҽ�
			if(e.getButton()==3){
				if(classmate.getSelectedIndex()>=0)
					pop.show(classmate, e.getX(), e.getY());
			}
		}
		if(e.getSource()==hmd){
			friendAcc=(Account) vHmd.get(hmd.getSelectedIndex());
			//˫��
			if(e.getClickCount()==2){
				findWin(friendAcc.getQQNum(),null);
			}
			//�Ҽ�
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
	//���Ҵ����Ƿ���ڣ��������򴴽���������ֱ����ʾ��Ϣ�ڽ���
	public ChatUI findWin(Integer qqcode,SendMsg msg){
		ChatUI chat=null;
		//�����Ƿ����,���������򷵻ؿ�ֵ
		chat=ht_ChatUsers.get(qqcode);
		if(chat==null){//�������򴴽����촰��
			if(msg==null)//˫�������Ҽ��򿪴���
				chat=new ChatUI(acc,friendAcc);
			else//�̴߳򿪴���
				chat=new ChatUI(msg.friendAcc,msg.myAcc);
			//�������
			ht_ChatUsers.put(qqcode, chat);
		}
		if(!chat.isVisible()){
			chat.show();
		}
		return chat;
	}
	//���� ���ߵ�֪ͨ
	public void call(int cmd){
		for(int i=0;i<vAllDetail.size();i++){
			Account a=vAllDetail.get(i);
			//
			if(a.getQQNum()!=acc.getQQNum()&&a.getStatus()!=Cmd.STATUS_LEAVE){
				//���ɷ�����
				SendMsg msg=new SendMsg();
				msg.myAcc=acc;
				msg.friendAcc=a;
				msg.Cmd=cmd;
				//����
				new SendSocket().send(msg);
			}

		}
	}
	//������Ϣ���߳�
	class ReceThread extends Thread{	
		public ReceThread(){
			ht_ChatUsers=new Hashtable<Integer,ChatUI>();
		}
		@Override
		public void run(){
			try {
				System.out.println("�߳�������....");			
				//���Լ��Ķ˿ڽ�������
				DatagramSocket serverSocket=new DatagramSocket(acc.getPort());		
				//���տͻ��˷�������Ϣ
				while(true){
					byte[] b=new byte[1024*70];
					DatagramPacket pack=new DatagramPacket(b, b.length);
					serverSocket.receive(pack);
					//���ֽ�����ת����SendMsg����
					ByteArrayInputStream bis=new ByteArrayInputStream(b,0,pack.getLength());
					ObjectInputStream ois=new ObjectInputStream(bis);
					SendMsg msg=(SendMsg) ois.readObject();
					
					switch(msg.Cmd){
						case Cmd.CMD_ONLINE:
							System.out.println("�յ�CMD_ONLINE");
							refresh();
							new Sound();
							//��ʾ������ʾ����
							new TipUI(msg.myAcc);
							break;
						case Cmd.CMD_LEAVE:
							System.out.println("�յ�CMD_LEAVE");
							refresh();
							break;
						case Cmd.CMD_BUSY:
							System.out.println("�յ�CMD_BUSY");
							refresh();
							break;
						case Cmd.CMD_HIDDEN://��������״̬��ִ��refresh()
							System.out.println("�յ�CMD_HIDDEN");
							refresh();
							break;
						case Cmd.CMD_SEND:
							ChatUI chat=findWin(msg.myAcc.getQQNum(),msg);
							//��ȡ������Ϣ����ʾ�ڴ���
							chat.appendView(msg.myAcc.getNickName(), msg.doc);
							break;
						case Cmd.CMD_SHAKE:
							ChatUI chat1=findWin(msg.myAcc.getQQNum(),msg);
							chat1.shake();
							break;
						case Cmd.CMD_ADDFRIEND:
							String str= msg.myAcc.getNickName()+"�����Ϊ���ѣ���ȷ�ϣ�";
							SendMsg m=new SendMsg();
							m.friendAcc=msg.myAcc;
							m.myAcc=msg.friendAcc;
							//������ȷ�ϰ�ť�����
							if(JOptionPane.showConfirmDialog(null, str,"��Ӻ���",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
								//firend�������������Ϊ���ѵļ�¼
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
							JOptionPane.showMessageDialog(null, msg.myAcc.getNickName()+"�ܾ�����ĺ�������");							
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
		//System.out.println("���ڹر�");
		new DBOper().changeStatus(acc.getQQNum(), Cmd.STATUS_LEAVE);
		//����������Ϣ
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
			//����������Ϣ
			switch (cbStatus.getSelectedItem().toString().trim()) {
			case Cmd.STATUS_ONLINE:
				file=filename;
				call(Cmd.CMD_ONLINE);
				System.out.println("����CMD_ONLINE");
				break;
			case Cmd.STATUS_LEAVE:
				file = filename.substring(0, filename.indexOf('.'))+ "_h.png";
				call(Cmd.CMD_LEAVE);
				System.out.println("����CMD_LEAVE");
				break;
			case Cmd.STATUS_BUSY:
				file = filename.substring(0, filename.indexOf('.'))+ "_w.png";
				call(Cmd.CMD_BUSY);
				System.out.println("����CMD_BUSY");
				break;
			case Cmd.STATUS_HIDDEN:
				file = filename.substring(0, filename.indexOf('.'))+ "_l.png";	
				call(Cmd.CMD_HIDDEN);
				System.out.println("����CMD_HIDDEN");
				break;
			}
			lblHead.setIcon(new ImageIcon(file));
			System.out.println("����ͷ��ı�");
		}
	}
}