package ht.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.HeadlessException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import ht.bean.Account;
import ht.db.DBOper;

public class TipUI extends JFrame{
	JLabel lblTip;
	public TipUI(Account self) {
		//ȥ�����ڱ�����
		setUndecorated(true);
		setSize(300,150);
		//��ȡ��Ļ���,�߶ȼ�ȥ30����Ļ�·��ı�������
		int width=getToolkit().getScreenSize().width;
		int height=getToolkit().getScreenSize().height-30;
		width-=this.getWidth();//��Ļ���½�
		Container con=getContentPane();
		String s=self.getNickName()+"("+self.getQQNum()+")������";
		lblTip=new JLabel(s,new ImageIcon(self.getFaceImage()),JLabel.CENTER);
		lblTip.setBackground(Color.PINK);
		lblTip.setOpaque(true);//͸��
		con.add(lblTip);
		setVisible(true);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	
		try {
			//�ı䴰������
			for(int i=0;i<100;i++){
				setLocation(width, (int) (height-(i*1.5)));
				Thread.sleep(50);
			}
			//���ô���͸����
			for(int i=100;i>0;i--){
				setOpacity((float) (0.01*i));
				Thread.sleep(50);
			}
			dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
