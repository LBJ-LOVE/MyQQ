package ht.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Vector;

import ht.bean.Account;
import ht.common.Cmd;


//所有数据库操作的函数

public class DBOper {
	//注册用户
	public int addAccount(Account acc){
		int bok=0;
		Connection conn= DBConn.opendb();
		String sql="insert into account values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			int i=1;
			pstmt.setInt(i++, acc.getQQNum());
			pstmt.setString(i++, acc.getTrueName());
			pstmt.setString(i++, acc.getNickName());
			pstmt.setString(i++, acc.getPassword());
			pstmt.setString(i++, acc.getSex());
			pstmt.setInt(i++, acc.getAge());
			pstmt.setString(i++, acc.getNation());
			pstmt.setString(i++, acc.getFaceImage());
			pstmt.setString(i++, acc.getAddress());
			pstmt.setString(i++, acc.getEmail());
			pstmt.setString(i++, acc.getIp());
			pstmt.setInt(i++, acc.getPort());
			pstmt.setString(i++, acc.getStatus());
			bok=pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bok;
	}
	//登录
	public Account login(Account acc){
		Connection conn= DBConn.opendb();
		String sql="select * from account where QQNum=? and password=?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, acc.getQQNum());
			pstmt.setString(2, acc.getPassword());
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				acc.setTrueName(rs.getString("trueName").trim());
				acc.setNickName(rs.getString("nickName").trim());
				acc.setSex(rs.getString("sex").trim());
				acc.setAge(rs.getInt("age"));
				acc.setNation(rs.getString("nation").trim());
				acc.setFaceImage(rs.getString("faceImage").trim());
				acc.setAddress(rs.getString("address").trim());
				acc.setEmail(rs.getString("email").trim());
				acc.setIp(rs.getString("ip").trim());
				int port = rs.getInt("port");
				System.out.println("port="+port);
				acc.setPort(port); 
				acc.setStatus(Cmd.STATUS_ONLINE);
				changeStatus(acc.getQQNum(), Cmd.STATUS_ONLINE);
			}else{
				acc=null;
			}
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return acc;
	}
	//生成端口
	public int getPort(int qqcode){
		Random ran = new Random();
		int port = ran.nextInt(50000)+8000;
//		boolean exist = isExistsPort(qqcode,port);
//		//循环判断，如果QQ号码已经存在，重新生成新的号码，再查询，直到号码不存在为止
//		while(exist){
//			port = ran.nextInt(50000)+8000;
//			exist = isExistsPort(qqcode,port);
//		}
		return port;
	}
	//更改状态
	public boolean changeStatus(int qqcode,String status){
		boolean bok=false;
		Connection conn= DBConn.opendb();
		String sql="update account set status=? where QQNum=?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setString(1,status);
			pstmt.setInt(2,qqcode);
			if(pstmt.executeUpdate()>0){
				bok=true;
			}
			pstmt.close();
			conn.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return bok;
	}
	//返回头像
	public String getFace(int qqcode){
		String face="";
		Connection conn= DBConn.opendb();
		String sql="select * from account where QQNum=?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, qqcode);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				face=rs.getString("faceImage").trim();
			}
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return face;
	}
	//返回所有好友，家人，同学，黑名单资料
	public Vector<Account> getAllinfo(Account acc){
		Vector<Account> allInfo=new Vector<Account>();
		Connection conn= DBConn.opendb();
		String sql="select a.*,f.groupName from account a right outer join friend f "
				+ "on a.QQNum=f.friendQQNum where f.myQQNum = ?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, acc.getQQNum());
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				Account a=new Account();
				a.setQQNum(rs.getInt("QQNum"));
				a.setTrueName(rs.getString("trueName").trim());
				a.setNickName(rs.getString("nickName").trim());
				a.setSex(rs.getString("sex").trim());
				a.setAge(rs.getInt("age"));
				a.setNation(rs.getString("nation").trim());
				a.setFaceImage(rs.getString("faceImage").trim());
				a.setAddress(rs.getString("address").trim());
				a.setEmail(rs.getString("email").trim());
				a.setIp(rs.getString("ip").trim());
				a.setPort(rs.getInt("port")); 
				a.setStatus(rs.getString("status").trim());
				a.setGroupName(rs.getString("groupName").trim());
				allInfo.add(a);
			}
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allInfo;
	}
	//查找好友   即查找符合条件的用户信息
	public Vector find(Account acc){
		Vector vdata = new Vector();
		Connection conn = DBConn.opendb();
		try{
			String sql = "select * from account where sex='"+acc.getSex()+"'";
			if(acc != null){
				if(acc.getQQNum()!=0)
					sql += " and qqnum="+acc.getQQNum();
				if(acc.getNickName() !=null && !acc.getNickName().equals(""))
					sql += " and nickname like '%"+acc.getNickName() +"%' ";
				if(acc.getAge()!=0)
					sql+="and age="+acc.getAge();
			}
			System.out.println(sql);			//获取数据库连接
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery(sql);
			while(rs.next()){
				//产生一条数据
				Vector row = new Vector();
				row.addElement(rs.getInt("qqnum"));
				//row.addElement(rs.getString("trueName").trim());
				row.addElement(rs.getString("nickName").trim());
				row.addElement(rs.getString("sex").trim());
				row.addElement(rs.getInt("age"));
				row.addElement(rs.getString("nation").trim());
				row.addElement(rs.getString("faceImage").trim());
				row.addElement(rs.getString("ip").trim());
				row.addElement(rs.getInt("port"));
				row.addElement(rs.getString("status"));
				vdata.addElement(row);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}	
		return vdata;
	}
	//用过QQ查找好友资料
	public Account findByQQcode(int qqcode) {
		Connection conn= DBConn.opendb();
		String sql="select * from account where QQNum=?";
		Account acc=new Account();
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, qqcode);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				acc.setQQNum(rs.getInt("QQNum"));
				acc.setTrueName(rs.getString("trueName").trim());
				acc.setNickName(rs.getString("nickName").trim());
				acc.setSex(rs.getString("sex").trim());
				acc.setAge(rs.getInt("age"));
				acc.setNation(rs.getString("nation").trim());
				acc.setFaceImage(rs.getString("faceImage").trim());
				acc.setAddress(rs.getString("address").trim());
				acc.setEmail(rs.getString("email").trim());
				acc.setIp(rs.getString("ip").trim());
				acc.setPort(rs.getInt("port")); 
				acc.setStatus(rs.getString("status").trim());
			}else{
				acc=null;
			}
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return acc;
	}
	//添加好友
	public void addFriend(Account acc,int friendQQ ){
		Connection conn= DBConn.opendb();
		String sql="insert into friend values(?,?,?,?)";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			int i=1;
			pstmt.setInt(i++, acc.getQQNum());
			pstmt.setInt(i++, friendQQ);
			pstmt.setString(i++, Cmd.TYPE_FRIEND);
			pstmt.setInt(i++, 0);
			pstmt.executeUpdate();
			pstmt.close();
			i=1;
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(i++, friendQQ); 
			pstmt.setInt(i++, acc.getQQNum());
			pstmt.setString(i++, Cmd.TYPE_FRIEND);
			pstmt.setInt(i++, 0);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}