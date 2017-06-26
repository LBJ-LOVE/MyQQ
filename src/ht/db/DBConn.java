package ht.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DBConn {

	private static final String URL="jdbc:mysql://127.0.0.1:3306/qq";
	private static final String USER="root";
	private static final String PASSWORD="135983";
	
	private static Connection conn=null;
	
	//��̬���飬����������������
	static{
		try{
			//1.��������
			Class.forName("com.mysql.jdbc.Driver");//��������
			//2.�������ݿ�
			if(conn==null || conn.isClosed()){
				conn = DriverManager.getConnection(URL, USER, PASSWORD);
			}
			if(!conn.isClosed())
				System.out.println("���ݿ����ӳɹ�...");
			else
				System.out.println("���ݿ�����ʧ�ܣ���ȷ���û����������Ƿ�׼ȷ...");
		}catch (Exception e) {
			e.printStackTrace();//��ӡ������Ϣ
		}	
	}
	//�������ݿ�
	public static Connection opendb(){
		try{
			//1.��������
			Class.forName("com.mysql.jdbc.Driver");//��������
			//2.�������ݿ�
			if(conn==null || conn.isClosed()){
				conn = DriverManager.getConnection(URL, USER, PASSWORD);
			}
			if(!conn.isClosed())
				System.out.println("���ݿ����ӳɹ�...");
			else
				System.out.println("���ݿ�����ʧ�ܣ���ȷ���û����������Ƿ�׼ȷ...");
		}catch (Exception e) {
			e.printStackTrace();//��ӡ������Ϣ
		}	
		return conn;
	}
	public void closedb(){
		try {
			if(!conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		
	}
}