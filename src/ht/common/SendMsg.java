package ht.common;

import java.io.Serializable;

import javax.swing.text.StyledDocument;

import ht.bean.Account;

public class SendMsg implements Serializable{
	public int Cmd;//������
	public Account myAcc;//������
	public Account friendAcc;//������
	public StyledDocument doc;//��������
	public String sFileName;//�����ļ�������
	public Object[] b;//�ļ�����  (�ֽ�����)
}