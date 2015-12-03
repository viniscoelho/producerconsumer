package produtorconsumidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Gerenciador {
	private ServerSocket serverSocket = null;
	private Socket connectionSend;
	private final int PORT = 12345;

	public Gerenciador() {
		try {
			serverSocket = new ServerSocket(PORT);
			connectionSend = new Socket(InetAddress.getLocalHost()
					.getHostAddress(), 12346);
		} catch (IOException e) {
		}
	}

	public void waitForConnections() {
		while (true) {
			try {
				System.out.println("Waiting for connections...");

				Socket connection = serverSocket.accept();
				System.out.println("Connected!");
				GerenciadorRequisicoes gc = new GerenciadorRequisicoes(
						connection);
				gc.start();
			} catch (IOException e) {
			}
		}
	}

	public String getBufferAddress() {
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(
					connectionSend.getInputStream()));
			PrintWriter outputClient = new PrintWriter(
					connectionSend.getOutputStream(), true);
			String msg;

			System.out
					.println("---------------------------------------------------------------------");
			outputClient.println("getBuff");

			char[] buffy = new char[32];
			int sz = input.read(buffy);
			msg = "";
			if (sz != -1) {
				msg = new String(buffy, 0, sz - 1);
				System.out.println("Message: " + msg);
			}

			return msg;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	class GerenciadorRequisicoes extends Thread {
		private Socket connection;

		public GerenciadorRequisicoes(Socket connection) {
			this.connection = connection;

		}

		public void run() {
			BufferedReader input;
			try {
				input = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				PrintWriter output = new PrintWriter(
						connection.getOutputStream(), true);

				while (true) {
					System.out
							.println("---------------------------------------------------------------------");
					char[] buffy = new char[32];
					int sz = input.read(buffy);

					if (sz != -1) {
						String msg = new String(buffy, 0, sz - 1);
						System.out.println("Message: " + msg);
						String buffAddress = getBufferAddress();
						int pos = buffAddress.indexOf(':');
						
						System.out.println(buffAddress.substring(1, pos) + " " +Integer.parseInt(buffAddress.substring(pos+1)));
						
						Socket conn = new Socket(buffAddress.substring(1, pos), Integer.parseInt(buffAddress.substring(pos+1)));
						
						PrintWriter outputClient = new PrintWriter(
								conn.getOutputStream(), true);
						outputClient.println(msg);
						
						BufferedReader inputClient = new BufferedReader(new InputStreamReader(
								conn.getInputStream()));
						buffy = new char[32];
						sz = inputClient.read(buffy);

						if (sz != -1) {
							msg = new String(buffy, 0, sz - 1);
							System.out.println("Message: " + msg);
							output.println(msg);
						}
						
						conn.close();
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
