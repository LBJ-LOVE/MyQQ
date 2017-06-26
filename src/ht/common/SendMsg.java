package ht.common;

import java.io.Serializable;

import javax.swing.text.StyledDocument;

import ht.bean.Account;

public class SendMsg implements Serializable{
	public int Cmd;//命令字
	public Account myAcc;//发送者
	public Account friendAcc;//接收者
	public StyledDocument doc;//发送内容
	public String sFileName;//发送文件的名字
	public Object[] b;//文件内容  (字节数组)
}