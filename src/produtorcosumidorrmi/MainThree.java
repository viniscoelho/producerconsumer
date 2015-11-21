package produtorcosumidorrmi;

import java.util.Scanner;

public class MainThree {
	
	public static void main(String[] args) {
		Buffer primaryBuffer;
		
	    Scanner scan = new Scanner(System.in);
	    int primary_port = scan.nextInt();
    	int quantElements = scan.nextInt();
    	
	    primaryBuffer = new Buffer(primary_port, quantElements);

		Thread t1 = new Thread(primaryBuffer);
		t1.start();
	}

}
