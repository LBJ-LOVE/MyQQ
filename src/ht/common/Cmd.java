package ht.common;

//系统中要用到的命令字--常量

public class Cmd {
	//命令字
	public static final int CMD_ONLINE=1001;//登陆通知
	public static final int CMD_SEND=1002;//发送消息
	public static final int CMD_HIDDEN=1003;//隐身通知
	public static final int CMD_BUSY=1004;//忙碌通知
	public static final int CMD_LEAVE=1005;//下线通知
	public static final int CMD_SHAKE=1006;//抖动
	public static final int CMD_ADDFRIEND=1007;//添加好友
	public static final int CMD_AGREE=1008;//同意添加为好友
	public static final int CMD_REJECT=1009;//拒绝添加好友
	//public static final int CMD_DELFRIEND=1010;//删除好友
	
	
	public static final String STATUS_ONLINE="在线";
	public static final String STATUS_LEAVE="离线";
	public static final String STATUS_HIDDEN="隐身";
	public static final String STATUS_BUSY="忙碌";

	public static final String TYPE_FRIEND="好友";
	public static final String TYPE_FAMILY="家人";
	public static final String TYPE_CLASSMATE="同学";
	public static final String TYPE_BLACK="黑名单";
}