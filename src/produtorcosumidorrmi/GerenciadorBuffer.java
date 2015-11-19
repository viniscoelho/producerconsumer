package produtorcosumidorrmi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GerenciadorBuffer {
    private Socket primaryConn;
    private Socket secondaryConn;
    private Boolean primaryActive; 
    private Boolean secondaryActive; 
    private ServerSocket serverSocket = null;
    private final int PORT = 12345;
    private final int PRIMARY_PORT = 12346;
    private final int SECONDARY_PORT = 12347;

    public GerenciadorBuffer() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void waitForConnections() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        System.out.println("Waiting for connections...");
                        
                        Socket connection = serverSocket.accept();
                        System.out.println("Connected!");
                        GerenciadorCliente gc = new GerenciadorCliente(
                                connection);
                        gc.start();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    public void createScenario() {
        

        try {
//            for (int i = 0; i < 2; i++) {
//                Produtor p = new Produtor(InetAddress.getLocalHost()
//                        .getHostAddress(), PORT);
//                p.start();
//                Consumidor c = new Consumidor(InetAddress.getLocalHost()
//                        .getHostAddress(), PORT);
//                c.start();
//            }

            primaryConn = new Socket(InetAddress.getLocalHost()
                    .getHostAddress(), PRIMARY_PORT);
            primaryActive = true;
            secondaryConn = new Socket(InetAddress.getLocalHost()
                    .getHostAddress(), SECONDARY_PORT);
            secondaryActive = true;
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    class GerenciadorCliente extends Thread {

        private Socket connection;
        private String msg, answer;
        
        public GerenciadorCliente(Socket connection) {
            this.connection = connection;
        }

        public void waitAnswer(Socket c) {
            PrintWriter outputBuffer, outputClient;
            try {
                outputBuffer = new PrintWriter(c.getOutputStream(), true);
                outputBuffer.println(msg);

                BufferedReader inputPrimary = new BufferedReader(
                        new InputStreamReader(c.getInputStream()));

                char [] buffy = new char[32];
                int sz = inputPrimary.read(buffy);
                answer = new String(buffy, 0, sz-1);
                System.out.println("Resposta Second: " + answer);
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        public void waitAnswerReply(Socket c) {
            PrintWriter outputBuffer, outputClient;
            try {
                outputBuffer = new PrintWriter(c.getOutputStream(), true);
                outputBuffer.println(msg);

                BufferedReader inputPrimary = new BufferedReader(
                        new InputStreamReader(c.getInputStream()));

                char [] buffy = new char[32];
                System.out.println("Resposta First: Esperando");
                int sz = inputPrimary.read(buffy);
                answer = new String(buffy, 0, sz-1);
                System.out.println("Resposta First: " + answer);
                
                outputClient = new PrintWriter(connection.getOutputStream(), true);
                outputClient.println(answer);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                
                while (true) {
                    System.out.println("entrou");
                    char [] buffy = new char[32];
                    int sz = input.read(buffy);
                    if ( sz != -1 ){
                        msg = new String(buffy, 0, sz-1);
                        System.out.println(msg);
                    }
                     
                    Thread t1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            waitAnswerReply(primaryConn);
                        }
                    });
                    t1.start();

                    Thread t2 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                        	waitAnswer(secondaryConn);
                        }
                    });
                    t2.start();
                }
            } catch (IOException e) {
            }
        }
    }

}
