package produtorconsumidor;

/**
 *
 * @author viniciuscoelho, thaismombach
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Buffer implements Runnable, Status {

    private List<Integer> elements = new LinkedList<Integer>();
    private int N;
    private ServerSocket serverSock;
    private Socket connection;

    public Buffer(int port, int quantElements) {
        try {
            serverSock = new ServerSocket(port);
            N = quantElements;
        }
        catch (IOException e) {
        }
    }

    public void waitForConnections() {
        try {
            System.out.println("Waiting for connections...");
            connection = serverSock.accept();
            System.out.println("Connected!");
        } catch (IOException e) {
        }
    }

    private synchronized void getElement() throws IOException {
        PrintWriter output = new PrintWriter(connection.getOutputStream(),
                true);

        if (elements.isEmpty()) {
            System.out.println("Buffer empty!");
            output.println(IS_EMPTY);
        }
        else {
            Integer value = elements.remove(0);
            System.out.println("Consuming: " + value);
            output.println(IS_CONSUMING);
        }
    }

    private synchronized void putElement(String element) throws IOException {
        PrintWriter output = new PrintWriter(connection.getOutputStream(),
                true);
        System.out.print("oiiii hue " + element);
        Integer tmp = new Integer(element);
        System.out.print("oiiii " + tmp);
        if (elements.size() == N) {
            System.out.println("Buffer is full!");
            output.println(IS_FULL);
        }
        else {
            elements.add(tmp);
            System.out.println("Producing: " + element);
            output.println(IS_PRODUCING);
        }
    }

    @Override
    public void run() {
        try {
            this.waitForConnections();

            BufferedReader input = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));

            while (true) {
                char [] buffy = new char[32];
                int sz = input.read(buffy);
                String msg = new String(buffy, 0, sz-1);
                
                System.out.println("Buffer " + msg);
                
                System.out.println("-----------------------------------------");
                System.out.println("List (Before Request): "
                        + elements.toString());
                System.out.println("Number of elements: " + elements.size());
                
                if (msg.equals("get")) {
                    getElement();
                }
                else {
                    putElement(msg);
                }

                System.out.println("-----------------------------------------");
                System.out.println("List (After Request): "
                        + elements.toString());
                System.out.println("Number of elements: " + elements.size());
            }
        }
        catch (IOException e) {
        }
    }

}
