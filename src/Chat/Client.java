package Chat;

import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        System.out.println("-----Client-----");
        Socket client=new Socket("localhost",10005);
        new Thread(new Send(client)).start();
        new Thread(new Receive(client)).start();
    }
}
