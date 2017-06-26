package ht.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendSocket implements Serializable{
	//发送消息
	public void send(SendMsg smg){
		//字节数组输出流
		try {
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(bos);
			oos.writeObject(smg);
			byte[] b=bos.toByteArray();//网络传输总是要转成字节数组传送
			
			DatagramSocket socket=new DatagramSocket();
			InetAddress addr=InetAddress.getByName(smg.friendAcc.getIp());
			int port=smg.friendAcc.getPort();
			DatagramPacket p=new DatagramPacket(b, 0, b.length, addr, port);
			//发送
			socket.send(p);
			oos.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}