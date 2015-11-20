package produtorcosumidorrmi;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainTwo {

	public static void main(String[] args) {
		Produtor p;
		try {
			p = new Produtor(InetAddress.getLocalHost().getHostAddress(), 12345);
			p.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
