package produtorconsumidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author viniciuscoelho, thaismombach
 */

public class Main {

	public static void main(String[] args) {
		Gerenciador gerenciador = new Gerenciador();
		gerenciador.waitForConnections();
	}

}
