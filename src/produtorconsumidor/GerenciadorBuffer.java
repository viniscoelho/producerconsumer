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
import java.net.UnknownHostException;
import java.util.Scanner;

public class GerenciadorBuffer {

    private Socket primaryConn;
    private Socket secondaryConn;
    private Boolean primaryActive;
    private Boolean secondaryActive;
    private ServerSocket serverSocket = null;
    private final int PORT = 12345;
    private static final Object lock = new Object();

    public GerenciadorBuffer() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
        }
    }

    public void waitForConnections() {
        while (true) {
            try {
                System.out.println("Waiting for connections...");

                Socket connection = serverSocket.accept();
                System.out.println("Connected!");
                GerenciadorCliente gc = new GerenciadorCliente(connection);
                gc.start();
            } catch (IOException e) {
            }
        }
    }

    public void createScenario() {

        try {
            int primary_port;
            int secondary_port;

            Scanner scan = new Scanner(System.in);
            String ipBufferUm = scan.next();
            primary_port = scan.nextInt();
            String ipBufferDois = scan.next();
            secondary_port = scan.nextInt();

            primaryConn = new Socket(ipBufferUm, primary_port);
            primaryActive = true;
            
            secondaryConn = new Socket(ipBufferDois, secondary_port);
            secondaryActive = true;
        }
        catch (UnknownHostException e) {
        }
        catch (IOException e) {
        }

    }

    class GerenciadorCliente extends Thread {

        private Socket connection;
        private String msg, answer;

        public GerenciadorCliente(Socket connection) {
            this.connection = connection;
        }

        public Boolean waitAnswer(Socket c, String msg) {
            synchronized (lock) {
                PrintWriter outputBuffer;
                try {
                    outputBuffer = new PrintWriter(c.getOutputStream(), true);
                    outputBuffer.println(msg);

                    BufferedReader inputPrimary = new BufferedReader(
                            new InputStreamReader(c.getInputStream()));

                    char[] buffy = new char[32];
                    try {
                        int sz = inputPrimary.read(buffy);
                        if (sz != -1) {
                            System.out.println("Resposta Buffer 01: " + buffy);
                            answer = new String(buffy, 0, sz - 1);
                        }
                        System.out.println("Resposta Buffer 02: " + answer);
                    }
                    catch (SocketException e) {
                        System.out.println("ERROR!");
                        return false;
                    }

                } catch (IOException e) {
                }

                return true;
            }
        }

        public Boolean waitAnswerReply(Socket c, String msg) {
            synchronized (lock) {
                PrintWriter outputBuffer, outputClient;
                try {
                    outputBuffer = new PrintWriter(c.getOutputStream(), true);
                    outputBuffer.println(msg);

                    BufferedReader inputPrimary = new BufferedReader(
                            new InputStreamReader(c.getInputStream()));

                    char[] buffy = new char[32];

                    try {
                        int sz = inputPrimary.read(buffy);
                        if (sz != -1) {
                            answer = new String(buffy, 0, sz - 1);
                            System.out.println("Resposta First: " + answer);
                        }

                        outputClient = new PrintWriter(
                                connection.getOutputStream(), true);
                        outputClient.println(answer);
                    }
                    catch (SocketException e) {
                        System.out.println("ERROR!");
                        outputClient = new PrintWriter(
                                connection.getOutputStream(), true);
                        outputClient.println(Status.ERROR_MSG);
                        return false;
                    }

                }
                catch (IOException e) {
                }
                return true;
            }
        }

        @Override
        public void run() {
            try {
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                while (true) {
                    System.out
                            .println("---------------------------------------------------------------------");
                    char[] buffy = new char[32];
                    int sz = input.read(buffy);
                    if (sz != -1) {
                        msg = new String(buffy, 0, sz - 1);
                        System.out.println("Message: " + msg);
                    }

                    if (primaryActive) {
                        Thread t1 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                primaryActive = waitAnswerReply(primaryConn,
                                        msg);
                                Thread.currentThread().interrupt();
                            }

                        });

                        if (secondaryActive) {
                            Thread t2 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    secondaryActive = waitAnswer(secondaryConn,
                                            msg);
                                    Thread.currentThread().interrupt();
                                }
                            });
                            t1.start();
                            t2.start();
                        }
                        else {
                            t1.start();
                        }
                    }
                    else {
                        Thread t1 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                secondaryActive = waitAnswerReply(
                                        secondaryConn, msg);
                                Thread.currentThread().interrupt();
                            }
                        });

                        t1.start();
                    }

                    System.out.println("---------------------------------------------------------------------");
                }
            }
            catch (IOException e) {
            }
        }
    }

}
