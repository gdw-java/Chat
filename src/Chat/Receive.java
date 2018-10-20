package Chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Receive implements Runnable {
    private DataInputStream dis;
    private Socket client;
    private boolean isRunning;

    public Receive(Socket client) {
        this.client=client;
        try {
            dis=new DataInputStream(client.getInputStream());
            isRunning=true;
        } catch (IOException e) {
            release();
        }
    }
    //接收消息
    private String receive(){
        String msg="";
        try {
            msg=dis.readUTF();
        } catch (IOException e) {
            release();
            System.out.println("--jieshou--");
        }
        return msg;
    }
    @Override
    public void run() {
        while (isRunning) {
            String msg=receive();
            if (!msg.equals("")) {
                System.out.println(msg);
            }
        }
    }
    //释放资源
    private void release(){
        this.isRunning=false;
        ClUtils.close(dis,client);
    }
}
