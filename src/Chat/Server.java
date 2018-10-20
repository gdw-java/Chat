package Chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private static CopyOnWriteArrayList<Channel> allcl =new CopyOnWriteArrayList<>();
    public static void main(String[] args) throws IOException {
        ServerSocket ss=new ServerSocket(10005);
        System.out.println("---服务端启动了---");
        while (true){
            Socket client = ss.accept();
            System.out.println("一个客户端建立了连接");
            String clip=client.getInetAddress().toString();
            System.out.println(clip);
            clip =clip.substring(1);
            Channel c = new Channel(client,clip);
            allcl.add(c);
            new Thread(c).start();

        }
    }
     static class Channel implements Runnable{
        private DataInputStream dis;
        private DataOutputStream dos;
        private Socket client;
        private boolean isRunning;
        private String clientIp;
        public Channel(Socket client,String clientIp){
            this.client=client;
            this.clientIp=clientIp;
            try {
                dis=new DataInputStream(client.getInputStream());
                dos=new DataOutputStream(client.getOutputStream());
                isRunning=true;
                if (!clientIp.equals("")) {
                    this.send("欢迎你的到来,你的ip:"+clientIp);
                    sendOthers("欢迎"+clientIp+"进入聊天室",true);
                }
            } catch (IOException e) {
                release();
            }
        }
        //群聊
         private void sendOthers(String msg,boolean isSys){
             for (Channel c : allcl) {
                if (c==this)
                    continue;
                 if (!isSys) {
                     c.send(clientIp + "对所有人说：" + msg);
                 } else {
                     c.send(msg);
                 }
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

        //发送消息
        private void send(String msg) {
            try {
                dos.writeUTF(msg);
            } catch (IOException e) {
                release();
                System.out.println("--fasong--");
            }
        }
        //释放资源
        private void release(){
            this.isRunning=false;
            ClUtils.close(dis,dos,client);
            allcl.remove(this);
            sendOthers(this.clientIp+"离开了聊天室",true);
        }

         @Override
         public void run() {
             while (isRunning) {
                 String msg=receive();
                 if (!msg.equals("")) {
                     sendOthers(msg,false);
                 }
             }
         }
     }

}
