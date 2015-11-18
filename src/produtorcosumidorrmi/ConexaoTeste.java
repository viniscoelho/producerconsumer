/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package produtorcosumidorrmi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author viniciuscoelho
 */
public class ConexaoTeste {
    
    private Buffer primaryBuffer;
    private Buffer secondaryBuffer;
    private Socket primaryConn;
    private Socket secondaryConn;
    private ServerSocket serverSocket = null;
    private final int PORT = 12345;
    private final int PRIMARY_PORT = 12346;
    private final int SECONDARY_PORT = 12347;
    
    public ConexaoTeste() {
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
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }
    public static void main(String[] args) {
        ConexaoTeste c = new ConexaoTeste();
        c.waitForConnections();
    }
}
