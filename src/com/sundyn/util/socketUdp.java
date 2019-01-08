package com.sundyn.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class socketUdp
{
    private DatagramSocket cli;
    private byte[] ip;
    private int port;
    private byte[] msg;
    
    public socketUdp() throws SocketException {
        this.cli = new DatagramSocket(10003);
    }
    
    public socketUdp(final int port) throws SocketException {
        this.cli = new DatagramSocket(port);
    }
    public static String sendMessage(final String sip, final int port, String msg) {
        MulticastSocket socket = null;
         try{
             socket = new MulticastSocket(port);
             ByteArrayOutputStream ostream = new ByteArrayOutputStream();
             DataOutputStream dataStream = new DataOutputStream(ostream);
             dataStream.writeUTF(msg);
             dataStream.close();

             byte[] data = ostream.toByteArray();

             InetAddress address = InetAddress.getByName(sip);
             socket.joinGroup(address);
             DatagramPacket dp = new DatagramPacket(data, data.length, address,port);
             socket.send(dp);
         }
         catch (Exception e) {
             e.printStackTrace();
         }

        try{
            if (socket == null)
                return null;
            byte[] data2 = new byte[1024];
            DatagramPacket packet2 = new DatagramPacket(data2, data2.length);
            socket.setSoTimeout(1000);
            socket.receive(packet2);
            String reply = new String(data2, 0, packet2.getLength());
            System.out.println("我是客户端，服务器说：" + reply);
            return reply;
        }
        catch (Exception e) {
            System.out.println("我是客户端，服务器走丢了：");
            return null;
        }
        finally {
            if(null!=socket)
                socket.close();
        }
    }

    public String send(final String sip, final int port, final String msg) {
        try {
            final String[] temp = sip.split("\\.");
            (this.ip = new byte[4])[0] = (byte)Integer.parseInt(temp[0]);
            this.ip[1] = (byte)Integer.parseInt(temp[1]);
            this.ip[2] = (byte)Integer.parseInt(temp[2]);
            this.ip[3] = (byte)Integer.parseInt(temp[3]);
           /* ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            DataOutputStream dataStream = new DataOutputStream(ostream);
            dataStream.writeUTF(msg);
            dataStream.close();
            byte[] data = ostream.toByteArray();
            */

            byte[] data = msg.getBytes();

            final DatagramPacket pac = new DatagramPacket(data, data.length, InetAddress.getByAddress(this.ip), port);
            this.cli.send(pac);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try{
            byte[] data2 = new byte[1024];
            DatagramPacket packet2 = new DatagramPacket(data2, data2.length);
            cli.setSoTimeout(1000);
            cli.receive(packet2);
            String reply = new String(data2, 0, packet2.getLength());
            System.out.println("我是客户端，服务器说：" + reply);
            return reply;
        }
        catch (Exception e) {
            System.out.println("我是客户端，服务器走丢了：");
            return null;
        }
        finally {
            close();
        }
    }
    
    public void close() {
        if(null!=this.cli)
            this.cli.close();
        this.cli = null;
    }
    
    public static void main(final String[] args) throws Exception {
        final socketUdp s = new socketUdp();
        //s.send("192.168.1.117", 8001, "S01E");//欢迎光临
        //s.send("192.168.1.117", 8001, "S03E");//暂停
        //s.send("192.168.1.117", 8001, "S06E");//取消暂停
        //s.send("192.168.1.117", 8001, "S02E");//请评价
        //s.send("192.168.1.117", 8001, "S09123E");//语音播报
        s.send("192.168.1.122", 8001, "S04qzh_wyE");//登录
        //s.send("192.168.1.122", 8001, "S05E");//退出
        s.close();
    }
    
    public byte[] intToByte(final int i) {
        final byte[] bt = { (byte)(0xFF & i), (byte)((0xFF00 & i) >> 8), (byte)((0xFF0000 & i) >> 16), (byte)((0xFF000000 & i) >> 24) };
        return bt;
    }
}
