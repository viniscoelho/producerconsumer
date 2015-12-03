package produtorconsumidor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MainTwo {

	public static void main(String[] args) {
		Produtor p;
		Scanner scan = new Scanner(System.in);
		//String ip = scan.next();
		//int port = scan.nextInt();
		
		try {
			p = new Produtor(InetAddress.getLocalHost().getHostAddress(), 12345);
			p.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
