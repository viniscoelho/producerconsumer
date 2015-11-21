package produtorcosumidorrmi;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainFive {

	public static void main(String[] args) {
		try {
			Socket s = new Socket("192.168.25.2", 12347);
			
			PrintWriter outputBuffer = new PrintWriter(s.getOutputStream(), true);
			outputBuffer.println("vini");
			outputBuffer.println("idiota");
			outputBuffer.println("chato pra caralho");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
}
