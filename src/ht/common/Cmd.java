package ht.common;

//ϵͳ��Ҫ�õ���������--����

public class Cmd {
	//������
	public static final int CMD_ONLINE=1001;//��½֪ͨ
	public static final int CMD_SEND=1002;//������Ϣ
	public static final int CMD_HIDDEN=1003;//����֪ͨ
	public static final int CMD_BUSY=1004;//æµ֪ͨ
	public static final int CMD_LEAVE=1005;//����֪ͨ
	public static final int CMD_SHAKE=1006;//����
	public static final int CMD_ADDFRIEND=1007;//��Ӻ���
	public static final int CMD_AGREE=1008;//ͬ�����Ϊ����
	public static final int CMD_REJECT=1009;//�ܾ���Ӻ���
	//public static final int CMD_DELFRIEND=1010;//ɾ������
	
	
	public static final String STATUS_ONLINE="����";
	public static final String STATUS_LEAVE="����";
	public static final String STATUS_HIDDEN="����";
	public static final String STATUS_BUSY="æµ";

	public static final String TYPE_FRIEND="����";
	public static final String TYPE_FAMILY="����";
	public static final String TYPE_CLASSMATE="ͬѧ";
	public static final String TYPE_BLACK="������";
}