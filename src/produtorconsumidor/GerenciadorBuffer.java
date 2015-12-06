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
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class GerenciadorBuffer {

    private List<Socket> listBuffers;
    private ServerSocket serverSocket = null;
    private final int PORT = 12346;

    public GerenciadorBuffer() {
        try {
            serverSocket = new ServerSocket(PORT);
            listBuffers = new LinkedList<Socket>();

            System.out.println("Quantidade de Buffers:");
            Scanner scan = new Scanner(System.in);
            int n = scan.nextInt();

            for (int i = 0; i < n; i++) {
                System.out.print("Endereco IP: ");
                String address = scan.next();
                System.out.print("Porta: ");
                int portBuff = scan.nextInt();

                listBuffers.add(new Socket(address, portBuff));

                VerificaBuffer vb = new VerificaBuffer(listBuffers.get(i));
                vb.start();
            }

        } catch (IOException e) {
        }
    }

    public void waitForConnections() {
        try {
            System.out.println("Waiting for connections...");

            Socket connection = serverSocket.accept();
            System.out.println("Connected!");
            waitForMessages(connection);
        } catch (IOException e) {
        }
    }

    public void waitForMessages(Socket connection) {
        try {
            System.out.println("Waiting for Msgs...");

            BufferedReader input = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String msg;

            while (true) {
                char[] buffy = new char[32];
                int sz = input.read(buffy);
                msg = "";
                if (sz != -1) {
                    msg = new String(buffy, 0, sz - 1);
                }

                if (msg.equals("getBuff")) {
                    String buffAddress = listBuffers.get(0)
                            .getRemoteSocketAddress().toString();
                    PrintWriter outputClient = new PrintWriter(
                            connection.getOutputStream(), true);
                    outputClient.println(buffAddress);
                }
            }

        } catch (IOException e) {
        }
    }

    class VerificaBuffer extends Thread {

        private Socket connection;

        public VerificaBuffer(Socket connection) {
            this.connection = connection;
        }

        public void run() {
            String msg;

            while (true) {

                try {
                    PrintWriter outputClient = new PrintWriter(
                            connection.getOutputStream(), true);
                    String info;
                    int index = listBuffers.indexOf(connection);

                    if (index != listBuffers.size() - 1) {
                        info = listBuffers.get(index + 1)
                                .getRemoteSocketAddress().toString();
                    } else {
                        info = "/empty";
                    }

                    outputClient.println(info);

                    char[] buffy = new char[32];
                    BufferedReader input = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    int sz = input.read(buffy);
                    msg = "";
                    if (sz != -1) {
                        msg = new String(buffy, 0, sz - 1);
                    } else {
                        listBuffers.remove(connection);
                        System.out.println("end " + connection.getRemoteSocketAddress().toString());
                        break;
                    }
                    // System.out.println("Msg: " + msg);
                } catch (SocketException se) {

                } catch (IOException e) {
                }
            }

            Thread.currentThread().interrupt();

        }
    }

}
