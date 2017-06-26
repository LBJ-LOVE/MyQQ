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
		//去掉窗口标题栏
		setUndecorated(true);
		setSize(300,150);
		//获取屏幕宽度,高度减去30（屏幕下方的标题栏）
		int width=getToolkit().getScreenSize().width;
		int height=getToolkit().getScreenSize().height-30;
		width-=this.getWidth();//屏幕右下角
		Container con=getContentPane();
		String s=self.getNickName()+"("+self.getQQNum()+")上线了";
		lblTip=new JLabel(s,new ImageIcon(self.getFaceImage()),JLabel.CENTER);
		lblTip.setBackground(Color.PINK);
		lblTip.setOpaque(true);//透明
		con.add(lblTip);
		setVisible(true);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	
		try {
			//改变窗口坐标
			for(int i=0;i<100;i++){
				setLocation(width, (int) (height-(i*1.5)));
				Thread.sleep(50);
			}
			//设置窗口透明度
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
