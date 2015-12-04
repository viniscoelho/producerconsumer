package produtorconsumidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Consumidor extends Thread implements Status{
    private Socket connection;

    public Consumidor(String host, int port) {
        try {
            connection = new Socket(host, port);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            PrintWriter output = new PrintWriter(connection.getOutputStream(),
                    true);
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            while (true) {
            	int waitt = (int) (Math.random() * 5000);
                Thread.sleep(waitt);
                System.out.println("Consuming");
                output.println("get");

                char [] buffy = new char[32];
                int sz = input.read(buffy);
                String answer = new String(buffy, 0, sz-1);
                
                System.out.println("Consumidror " + answer);
                
                if (answer.equals(IS_EMPTY)) {
                    int wait = (int) (Math.random() * 1000);
                    Thread.sleep(wait);
                } else if (answer.equals("error")) {
                	System.out.println("ERROR");
                }
            }
        } catch (IOException e) {
        } catch (InterruptedException e) {
        }
    }

}
