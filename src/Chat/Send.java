package Chat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Send implements Runnable {
    private BufferedReader console;
    private DataOutputStream dos;
    private Socket client;
    private boolean isRunning;
    public Send(Socket client) {
        this.client=client;
        console=new BufferedReader(new InputStreamReader(System.in));
        try {
            dos=new DataOutputStream(client.getOutputStream());
            isRunning=true;
        } catch (IOException e) {
            e.printStackTrace();
            release();
        }
    }


    @Override
    public void run() {
        while (isRunning) {
            String msg=getStrFromConsole();
            if (!msg.equals("")) {
                send(msg);
            }
        }
    }
    //发送消息
    private void send(String msg){
        try {
            dos.writeUTF(msg);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("---cfasong---");
            release();
        }
    }
    private String getStrFromConsole(){
        try {
            return console.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    //释放资源
    private void release(){
        this.isRunning=false;
        ClUtils.close(dos,client);
    }
}
