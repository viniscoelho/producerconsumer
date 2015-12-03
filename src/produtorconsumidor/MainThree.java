package produtorconsumidor;

import java.util.Scanner;

public class MainThree {
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int n = scan.nextInt();
		Buffer buff;
		
		if(n != 12984) {
			String next = scan.next();
			int port = scan.nextInt();
		
			buff = new Buffer(n, 10, next, port);
		}
		else {
			buff = new Buffer(n, 10, null, 0);
		}
		
		Thread t = new Thread(buff); 
		t.start();
	}

}
