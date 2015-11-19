package produtorcosumidorrmi;

public class MainFour {
	public static void main(String[] args) {
	    Buffer secondaryBuffer;
	    final int SECONDARY_PORT = 12347;
		
		secondaryBuffer = new Buffer(SECONDARY_PORT, 10);
		
		Thread t2 = new Thread(secondaryBuffer);
		t2.start();
	}
}
