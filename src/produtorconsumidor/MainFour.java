package produtorcosumidorrmi;

import java.util.Scanner;

public class MainFour {
	public static void main(String[] args) {
		Consumidor c;
		Scanner scan = new Scanner(System.in);
		String ip = scan.next();
		int port = scan.nextInt();
		
		c = new Consumidor(ip, port);
		c.start();
	}
}
