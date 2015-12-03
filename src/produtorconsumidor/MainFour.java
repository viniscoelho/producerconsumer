package produtorconsumidor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MainFour {
	public static void main(String[] args) {
		Consumidor c;
		/*Scanner scan = new Scanner(System.in);
		String ip = scan.next();
		int port = scan.nextInt();*/
		
		try {
			c = new Consumidor(InetAddress.getLocalHost().getHostAddress(), 12345);
			c.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
