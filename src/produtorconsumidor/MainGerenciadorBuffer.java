package produtorconsumidor;

/**
 *
 * @author viniciuscoelho, thaismombach
 */

/*
thaismombach@192.168.25.6
java produtorconsumidor/MainGerenciadorBuffer 3 192.168.25.10 12347 192.168.25.6 12348 192.168.25.10 12349
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
            for ( int i = 0, j = 1; i < n; i++, j += 2 ) {
                addresses[i] = args[j];
                ports[i] = Integer.parseInt(args[j+1]);
            }
            GerenciadorBuffer g = new GerenciadorBuffer(n, addresses, ports);
            g.waitForConnections();
        }
    }

}
