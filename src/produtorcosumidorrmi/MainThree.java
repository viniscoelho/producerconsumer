package produtorcosumidorrmi;

public class MainThree {
	
	public static void main(String[] args) {
		Buffer primaryBuffer;
	    final int PRIMARY_PORT = 12346;
		
	    primaryBuffer = new Buffer(PRIMARY_PORT, 10);

		Thread t1 = new Thread(primaryBuffer);
		t1.start();
	}

}
