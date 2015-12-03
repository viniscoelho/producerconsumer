package produtorconsumidor;

/**
 *
 * @author viniciuscoelho, thaismombach
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

public class Buffer implements Runnable, Status {

	private List<Integer> elements = new LinkedList<Integer>();
	private int N, nextPort;
	private String nextAddress;
	private ServerSocket serverSock;
	private Socket connection;
	private static final Object countLock = new Object();

	public Buffer(int port, int quantElements, String nextAddress, int nextPort) {
		try {
			serverSock = new ServerSocket(port);
			this.nextPort = nextPort;
			this.nextAddress = nextAddress;
			N = quantElements;
		} catch (IOException e) {
		}
	}

	public void run() {
		try {
			while (true) {
				System.out.println("Waiting for connections...");
				connection = serverSock.accept();
				System.out.println("Connected!");

				BufferMsg buffer = new BufferMsg(connection);
				buffer.start();
			}
		} catch (IOException e) {
		}
	}

	class BufferMsg extends Thread {
		private Socket connection;

		public BufferMsg(Socket connection) {
			this.connection = connection;
		}

		private synchronized void getElement() throws IOException {
			PrintWriter output = new PrintWriter(connection.getOutputStream(),
					true);

			if (elements.isEmpty()) {
				System.out.println("Buffer empty!");
				output.println(IS_EMPTY);
			} else {
				Integer value = elements.remove(0);
				System.out.println("Consuming: " + value);
				output.println(IS_CONSUMING);
			}
		}

		private synchronized void putElement(String element) throws IOException {
			PrintWriter output = new PrintWriter(connection.getOutputStream(),
					true);
			Integer tmp = new Integer(element);
			if (elements.size() == N) {
				System.out.println("Buffer is full!");
				output.println(IS_FULL);
			} else {
				elements.add(tmp);
				System.out.println("Producing: " + element);
				output.println(IS_PRODUCING);
			}
		}

		@Override
		public void run() {
			try {

				BufferedReader input = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));

				while (true) {
					char[] buffy = new char[32];
					int sz = input.read(buffy);
					String msg = "";
					if (sz != -1) {
						msg = new String(buffy, 0, sz - 1);
					}

					System.out.println("Element sent: " + msg);

					System.out
							.println("-----------------------------------------");
					System.out.println("List (Before Request): "
							+ elements.toString());
					System.out
							.println("Number of elements: " + elements.size());

					if (msg.equals("get")) {
						getElement();
						synchronized (countLock) {
							if (nextAddress != null) {
								Socket connectionNext = new Socket(nextAddress,
										nextPort);
								PrintWriter outputNext = new PrintWriter(
										connectionNext.getOutputStream(), true);
								outputNext.println(msg);
							}
						}
					} else if (msg.equals("ok")) {
						System.out.println("ok");
						PrintWriter output = new PrintWriter(
								connection.getOutputStream(), true);
						output.println("ok");
					} else if (msg != "" && msg.charAt(0) == '/') {
						synchronized (countLock) {
							int pos = msg.indexOf(':');
							nextAddress = msg.substring(1, pos);
							nextPort = Integer.parseInt(msg.substring(pos + 1));
							System.out.println(nextAddress + " " + nextPort);
						}
					} else if (!msg.equals("")) {
						putElement(msg);
						synchronized (countLock) {
							if (nextAddress != null) {
								Socket connectionNext = new Socket(nextAddress,
										nextPort);
								PrintWriter outputNext = new PrintWriter(
										connectionNext.getOutputStream(), true);
								outputNext.println(msg);
							}
						}
					}

					System.out
							.println("-----------------------------------------");
					System.out.println("List (After Request): "
							+ elements.toString());
					System.out
							.println("Number of elements: " + elements.size());
				}
			} catch (SocketException se) {
				System.out.println("Vini tapado!");
				Thread.currentThread().interrupt();
			} catch (IOException e) {
			}
		}
	}

}
