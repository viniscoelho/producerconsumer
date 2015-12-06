package produtorconsumidor;

/**
 *
 * @author viniciuscoelho, thaismombach
 */

public class MainGerenciadorBuffer {

    public static void main(String[] args) {
        if ( args.length < 3 ){
            System.out.println("Wrong number of arguments!");
            System.exit(1);
        }
        else{
            int n = Integer.parseInt(args[0]);
            String [] addresses = new String[args.length/2];
            int [] ports = new int[args.length/2];
            for ( int i = 1; i < args.length; i += 2 ) {
                addresses[i-1] = args[i];
                ports[i-1] = Integer.parseInt(args[i+1]);
            }
            GerenciadorBuffer g = new GerenciadorBuffer(n, addresses, ports);
            g.waitForConnections();
        }
    }

}
