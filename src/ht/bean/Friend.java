package ht.bean;

import java.io.Serializable;

public class Friend implements Serializable{
	
	private int id;
	private int myQQNum;
	private int friendQQNum;
	private String group;
	private int invalid;//0不是黑名单，1是黑名单
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMyQQNum() {
		return myQQNum;
	}
	public void setMyQQNum(int myQQNum) {
		this.myQQNum = myQQNum;
	}
	public int getFriendQQNum() {
		return friendQQNum;
	}
	public void setFriendQQNum(int friendQQNum) {
		this.friendQQNum = friendQQNum;
	}
	public String getGroupName() {
		return group;
	}
	public void setGroupName(String group) {
		this.group = group;
	}
	public int getInvalid() {
		return invalid;
	}
	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}
}