package ht.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ht.bean.Account;
import ht.common.Cmd;
import ht.common.SendMsg;
import ht.common.SendSocket;

public class ChatUI extends JFrame implements ActionListener,ItemListener {
	
	private Account myAcc,friendAcc;
	JTextPane recePanel,sendPanel;
	JButton btnSend,btnShake,btnSendFile,btnColor,btnClose;
	JComboBox cbFont,cbSize,cbImg;
	JLabel lblBoy,lblGirl,lblFriendInfo;
	
	public ChatUI() {

	}
	public ChatUI(Account myAcc,Account friendAcc){
		this.myAcc=myAcc;
		this.friendAcc=friendAcc;
		setIconImage(new ImageIcon(friendAcc.getFaceImage()).getImage());
		setTitle(friendAcc.getNickName());
		lblFriendInfo=new JLabel(friendAcc.getNickName()+"("+friendAcc.getQQNum()+")");
		add(lblFriendInfo,BorderLayout.NORTH);
		
		recePanel=new JTextPane();
		recePanel.setEditable(false);
		
		btnShake=new JButton(new ImageIcon("faces/zd.png"));
		btnShake.setMargin(new Insets(0, 0, 0, 0));//调整按钮的大小为图片大小
		btnSendFile=new JButton("发送文件");
		btnColor=new JButton("字体颜色");
		String[] fonts={"宋体","楷体","黑体","隶书"};
		cbFont=new JComboBox(fonts);
		String[] fontSize={"15","16","18","20","22","24","26"};
		cbSize=new JComboBox(fontSize);
		cbImg=new JComboBox(getImg());
		JPanel btnPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		btnPanel.add(cbFont);
		btnPanel.add(cbSize);
		btnPanel.add(btnColor);
		btnPanel.add(btnShake);
		btnPanel.add(btnSendFile);
		btnPanel.add(cbImg);
		
		sendPanel=new JTextPane();
		JPanel southPanel=new JPanel(new BorderLayout());
		southPanel.add(btnPanel,BorderLayout.NORTH);
		southPanel.add(new JScrollPane(sendPanel));
		
		JPanel middlePanel=new JPanel(new GridLayout(2,1,0,0));
		middlePanel.add(new JScrollPane(recePanel));
		middlePanel.add(new JScrollPane(southPanel));
		add(middlePanel);
		
		btnSend=new JButton("发送(S)");
		btnSend.setMnemonic('S');
		btnClose=new JButton("关闭(X)");
		btnClose.setMnemonic('X');
		JPanel bottomPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bottomPanel.add(btnSend);
		bottomPanel.add(btnClose);
		add(bottomPanel,BorderLayout.SOUTH);
		
		lblBoy=new JLabel(new ImageIcon("faces/boy.gif"));
		lblGirl=new JLabel(new ImageIcon("faces/girl.gif"));
		JPanel rightPanel=new JPanel(new GridLayout(2, 1,5,0));
		rightPanel.add(lblBoy);
		rightPanel.add(lblGirl);
		add(rightPanel,BorderLayout.EAST);
		
		btnShake.addActionListener(this);
		btnColor.addActionListener(this);
		btnSend.addActionListener(this);
		btnClose.addActionListener(this);
		btnSendFile.addActionListener(this);
		cbFont.addItemListener(this);
		cbSize.addItemListener(this);
		cbImg.addItemListener(this);
		
		setSize(600,500);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); 
		sendPanel.requestFocus();
	}
	//获得表情
	public Icon[] getImg(){
		Icon[] icons=null;
		File file=new File("bq");
		String sfile[]=file.list();
		icons=new ImageIcon[sfile.length];
		for(int i=0;i<sfile.length;i++){
			icons[i]=new ImageIcon("bq/"+sfile[i]);
		}
		return icons;
	}
	//把发送框的内容提交到接收框，同时清除发送框的内容
	public void appendView(String name,StyledDocument xx) throws BadLocationException{
		//获取接收框的文档（内容）
		StyledDocument vdoc=recePanel.getStyledDocument();
		//格式化时间
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String time=sdf.format(date);
		//创建一个属性集合
		SimpleAttributeSet as=new SimpleAttributeSet();
		String s=name+"  "+time+"\n";
		vdoc.insertString(vdoc.getLength(),s,as);
		int end =0;
		//处理显示的内容
		while(end<xx.getLength()){
			Element e0= xx.getCharacterElement(end);
			//获取对应风格
			SimpleAttributeSet as1=new SimpleAttributeSet();
			StyleConstants.setForeground(as1, StyleConstants.getForeground(e0.getAttributes()));
			StyleConstants.setFontSize(as1, StyleConstants.getFontSize(e0.getAttributes()));
			StyleConstants.setFontFamily(as1, StyleConstants.getFontFamily(e0.getAttributes()));
			//获取元素内容
			s=e0.getDocument().getText(end,e0.getEndOffset()-end);
			if("icon".equals(e0.getName())){
				vdoc.insertString(vdoc.getLength(), s, e0.getAttributes());
			}else{
				vdoc.insertString(vdoc.getLength(), s, as1);
			}
			end=e0.getEndOffset();
		}
		vdoc.insertString(vdoc.getLength(), "\n", as);
		//设置显示视图加字符的位置于文档结尾，一遍视图滚动
		recePanel.setCaretPosition(vdoc.getLength());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnSend){
			try {
				appendView(myAcc.getNickName(),sendPanel.getStyledDocument());
				//生成发送类
				SendMsg msg=new SendMsg();
				msg.Cmd=Cmd.CMD_SEND;
				msg.doc=sendPanel.getStyledDocument();
				msg.myAcc=myAcc;
				msg.friendAcc=friendAcc;
				//发送
				new SendSocket().send(msg);
				System.out.println("发送成功.....");
				sendPanel.setText("");
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}else if(e.getSource()==btnShake){
			//生成发送类
			SendMsg msg=new SendMsg();
			msg.Cmd=Cmd.CMD_SHAKE;
			msg.myAcc=myAcc;
			msg.friendAcc=friendAcc;
			//发送
			new SendSocket().send(msg);			
			shake();
		}else if(e.getSource()==btnColor){
			JColorChooser cc=new JColorChooser();
			Color c=cc.showDialog(this, "字体颜色", Color.BLACK);
			sendPanel.setForeground(c);
		}else if(e.getSource()==btnSendFile){
			
		}else if(e.getSource()==btnClose){
			dispose();
		}
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource()==cbFont||e.getSource()==cbSize){
			String sfont=cbFont.getSelectedItem().toString().trim();
			int size=Integer.valueOf(cbSize.getSelectedItem().toString().trim());
			sendPanel.setFont(new Font(sfont,Font.PLAIN,size));
		}else if(e.getSource()==cbImg){
			Icon g=(Icon) cbImg.getSelectedItem();
			sendPanel.insertIcon(g);
		}
	}
	public void shake(){
		Point p=this.getLocationOnScreen();
		for(int i=0;i<20;i++){
			if(i%2==0){
				setLocation(p.x+5, p.y+5);
			}else{
				setLocation(p.x-5, p.y-5);
			}
			try {
				Thread.sleep(100);//0.1秒抖一次
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
}