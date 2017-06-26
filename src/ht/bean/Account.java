package ht.bean;

import java.io.Serializable;

//对于Account表，一个字段对应一个成员变量
public class Account implements Serializable{
	
	private int 	QQNum;//QQ号
	private String 	trueName;//真实名字
	private String 	nickName;//昵称
	private String 	password;//密码
	private String 	sex;//性别
	private int 	age;//年龄
	private String 	nation;//--民族
	private String 	faceImage;//--头像
	private String 	address;//--地址
	private String 	email;//邮箱
	private String 	ip;//地址
	private int 	port;//端口号
	private String 	status;//状态(在线,离线,隐身,忙碌)
	private String groupName;
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getQQNum() {
		return QQNum;
	}
	public void setQQNum(int QQNum) {
		this.QQNum = QQNum;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getFaceImage() {
		return faceImage;
	}
	public void setFaceImage(String faceImage) {
		this.faceImage = faceImage;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}	