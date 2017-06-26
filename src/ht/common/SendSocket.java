package ht.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendSocket implements Serializable{
	//������Ϣ
	public void send(SendMsg smg){
		//�ֽ����������
		try {
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(bos);
			oos.writeObject(smg);
			byte[] b=bos.toByteArray();//���紫������Ҫת���ֽ����鴫��
			
			DatagramSocket socket=new DatagramSocket();
			InetAddress addr=InetAddress.getByName(smg.friendAcc.getIp());
			int port=smg.friendAcc.getPort();
			DatagramPacket p=new DatagramPacket(b, 0, b.length, addr, port);
			//����
			socket.send(p);
			oos.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}