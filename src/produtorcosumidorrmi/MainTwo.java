package produtorcosumidorrmi;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MainTwo {

	public static void main(String[] args) {
		Produtor p;
		Scanner scan = new Scanner(System.in);
		String ip = scan.next();
		int port = scan.nextInt();
		
		p = new Produtor(ip, port);
		p.start();
	}

}
