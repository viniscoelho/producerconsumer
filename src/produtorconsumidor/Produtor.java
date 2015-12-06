package produtorconsumidor;

/**
 *
 * @author viniciuscoelho, thaismombach
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Produtor extends Thread implements Status {

    private Socket connection;

    public Produtor(String host, int port) {
        try {
            connection = new Socket(host, port);
        } catch (UnknownHostException e) {
        } catch (IOException e) {
        }
    }

    @Override
    public void run() {
        try {
            PrintWriter output = new PrintWriter(connection.getOutputStream(),
                    true);
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            Integer value = (int) (Math.random() * 10000);

            while (true) {
                int wait = (int) (Math.random() * 1000);
                Thread.sleep(wait);

                System.out.println("Producing: " + value);
                System.out.println(connection.getRemoteSocketAddress()
                        .toString());
                output.println(value);

                System.out.println("Waiting...");
                char[] buffy = new char[32];
                int sz = input.read(buffy);
                String answer = new String(buffy, 0, sz - 1);

                if (answer.equals(IS_FULL)) {
                    System.out.println("FULL");
                    wait = (int) (Math.random() * 1000);
                    Thread.sleep(wait);
                } else if (answer.equals("error")) {
                    System.out.println("ERROR");
                } else {
                    System.out.println("OK");
                    value = (int) (Math.random() * 10000);
                }
            }

        } catch (IOException e) {
        } catch (InterruptedException e) {
        }
    }
}
