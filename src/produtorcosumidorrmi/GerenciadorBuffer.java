package produtorcosumidorrmi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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

        public Boolean waitAnswer(Socket c) {
            PrintWriter outputBuffer, outputClient;
            try {
                outputBuffer = new PrintWriter(c.getOutputStream(), true);
                outputBuffer.println(msg);

                BufferedReader inputPrimary = new BufferedReader(
                        new InputStreamReader(c.getInputStream()));

                char [] buffy = new char[32];
                try {
                int sz = inputPrimary.read(buffy);
                answer = new String(buffy, 0, sz-1);
                System.out.println("Resposta Second: " + answer);
                } catch(SocketException e) {
                	System.out.println("ERROR!");
                	return false;
                } 
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            return true;
        }
        
        public Boolean waitAnswerReply(Socket c) {
            PrintWriter outputBuffer, outputClient;
            try {
                outputBuffer = new PrintWriter(c.getOutputStream(), true);
                outputBuffer.println(msg);

                BufferedReader inputPrimary = new BufferedReader(
                        new InputStreamReader(c.getInputStream()));

                char [] buffy = new char[32];
                System.out.println("Resposta First: Esperando");
                
                try {	
                	int sz = inputPrimary.read(buffy);
                	answer = new String(buffy, 0, sz-1);
                    System.out.println("Resposta First: " + answer);
                    
                    outputClient = new PrintWriter(connection.getOutputStream(), true);
                    outputClient.println(answer);
                } catch(SocketException e) {
                	System.out.println("ERROR!");
                	outputClient = new PrintWriter(connection.getOutputStream(), true);
                    outputClient.println(Status.ERROR_MSG);
                	return false;
                }
 
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
			return true; 
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
                    
                    if(primaryActive) {
                    	Thread t1 = new Thread(new Runnable() {
                    		@Override
                    		public void run() {
                    			primaryActive = waitAnswerReply(primaryConn);
                    		}
                    	});
                    	
                    	t1.start();
                    	
                    	if(secondaryActive) {
                    		Thread t2 = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                	secondaryActive = waitAnswer(secondaryConn);
                                }
                            });
                            t2.start();
                    	}
                    }
                    else { 
                    	Thread t1 = new Thread(new Runnable() {
                    		@Override
                    		public void run() {
                    			secondaryActive = waitAnswerReply(secondaryConn);
                    		}
                    	});
                    	
                    	t1.start();
                    }
                }
            } catch (IOException e) {
            }
        }
    }

}
