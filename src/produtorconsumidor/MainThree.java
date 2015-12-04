package produtorconsumidor;

import java.util.Scanner;

public class MainThree {
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int n = scan.nextInt();
		Buffer buff;
		
		buff = new Buffer(n, 10);
		
		Thread t = new Thread(buff); 
		t.start();
	}

}
