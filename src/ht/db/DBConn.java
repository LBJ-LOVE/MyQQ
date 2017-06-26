package ht.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DBConn {

	private static final String URL="jdbc:mysql://127.0.0.1:3306/qq";
	private static final String USER="root";
	private static final String PASSWORD="135983";
	
	private static Connection conn=null;
	
	//静态语句块，程序启动立即运行
	static{
		try{
			//1.调入驱动
			Class.forName("com.mysql.jdbc.Driver");//调入驱动
			//2.连接数据库
			if(conn==null || conn.isClosed()){
				conn = DriverManager.getConnection(URL, USER, PASSWORD);
			}
			if(!conn.isClosed())
				System.out.println("数据库连接成功...");
			else
				System.out.println("数据库连接失败，请确认用户名或密码是否准确...");
		}catch (Exception e) {
			e.printStackTrace();//打印错误信息
		}	
	}
	//连接数据库
	public static Connection opendb(){
		try{
			//1.调入驱动
			Class.forName("com.mysql.jdbc.Driver");//调入驱动
			//2.连接数据库
			if(conn==null || conn.isClosed()){
				conn = DriverManager.getConnection(URL, USER, PASSWORD);
			}
			if(!conn.isClosed())
				System.out.println("数据库连接成功...");
			else
				System.out.println("数据库连接失败，请确认用户名或密码是否准确...");
		}catch (Exception e) {
			e.printStackTrace();//打印错误信息
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