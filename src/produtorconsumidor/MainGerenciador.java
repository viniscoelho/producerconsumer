package produtorconsumidor;

/**
 *
 * @author viniciuscoelho, thaismombach
 */

public class MainGerenciador {

    public static void main(String []args){
        if ( args.length != 2 ){
            System.out.println("Wrong number of arguments!");
            System.exit(1);
        }
        else{
            String ip = args[0];
            Integer port = Integer.parseInt(args[1]);

            Gerenciador g = new Gerenciador(ip, port);
            g.waitForConnections();
        }
        
    }
}
