package produtorconsumidor;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MainFive {

	public static void main(String[] args) {
		GerenciadorBuffer gb = new GerenciadorBuffer(); 
		gb.waitForConnections();
		
	}
	
}
