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


public class LookInfo extends JFrame {
	private JLabel lblTitle,lblUserName,lblNickName,lblAge,lblSex,lblNation,lblQQ;
	private JLabel lblFace,lblEmal,lblAddr;
	private JLabel lblbg;
	
	public LookInfo(Account acc) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		setIconImage( new ImageIcon("faces/tubiao.png").getImage());
		lblbg = new JLabel(new ImageIcon("faces/bg1.jpg"));
		add(lblbg);
		//���ܸ��Ĵ��ڵĴ�С
		setResizable(false);
		lblTitle = new JLabel("��������",JLabel.CENTER);
		lblTitle.setFont(new Font("����",Font.BOLD,18));
		lblTitle.setForeground(Color.BLACK);
		lblTitle.setSize(200, 30);
		lblTitle.setLocation(50, 10);
		lblbg.add(lblTitle);
		
		lblFace = new JLabel(new ImageIcon(acc.getFaceImage()),JLabel.CENTER);
		lblFace.setLocation(50, 40);
		lblFace.setSize(200, 80);		
		lblbg.add(lblFace);
		
		lblQQ = new JLabel("QQ����:"+acc.getQQNum(),JLabel.CENTER);
		lblQQ.setLocation(50, 120);
		lblQQ.setSize(200, 20);
		lblbg.add(lblQQ);
		
		lblUserName = new JLabel("��ʵ����:"+acc.getTrueName(),JLabel.CENTER);
		lblUserName.setLocation(50, 150);
		lblUserName.setSize(200, 20);
		lblNickName = new JLabel("�ǳ�:"+acc.getNickName(),JLabel.CENTER);
		lblNickName.setLocation(50, 180);
		lblNickName.setSize(200, 20);
		lblbg.add(lblUserName);
		lblbg.add(lblNickName);
		
		lblAge = new JLabel("����:"+acc.getAge(),JLabel.CENTER);
		lblAge.setLocation(50, 210);
		lblAge.setSize(200, 20);
		lblbg.add(lblAge);

		lblSex = new JLabel("�Ա�:"+acc.getSex(),JLabel.CENTER);
		lblSex.setLocation(50, 240);
		lblSex.setSize(200, 20);
		lblbg.add(lblSex);
		
		lblNation = new JLabel("����:"+acc.getNation(),JLabel.CENTER);
		lblNation.setLocation(50, 270);
		lblNation.setSize(200, 20);
		lblbg.add(lblNation);
		
		lblEmal = new JLabel("�����ַ:"+acc.getEmail(),JLabel.CENTER);
		lblEmal.setLocation(50, 300);
		lblEmal.setSize(200, 20);
		lblbg.add(lblEmal);

		lblAddr = new JLabel("��ͥ��ַ:"+acc.getAddress(),JLabel.CENTER);
		lblAddr.setLocation(50, 330);
		lblAddr.setSize(200, 20);
		lblbg.add(lblAddr);

		setSize(300, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//�رյ�ǰ����
		setLocationRelativeTo(null);
	}
}