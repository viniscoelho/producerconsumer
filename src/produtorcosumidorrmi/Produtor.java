package produtorcosumidorrmi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Produtor extends Thread implements Status {
    private Socket connection;

    public Produtor(String host, int port) {
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
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));

            while (true) {
                int wait = (int) (Math.random() * 3000);
                Thread.sleep(wait);

                Integer value = (int) (Math.random() * 10000);

                System.out.println("Producing: " + value);
                System.out.println(connection.getRemoteSocketAddress()
                        .toString());
                output.println(value);
                
                char [] buffy = new char[32];
                int sz = input.read(buffy);
                String answer = new String(buffy, 0, sz-1);

                System.out.println("Produtor " + answer);
                
                if (answer.equals(IS_FULL)) {
                    wait = (int) (Math.random() * 1000);
                    Thread.sleep(wait);
                }

            }

        } catch (IOException e) {
        } catch (InterruptedException e) {
        }
    }
}