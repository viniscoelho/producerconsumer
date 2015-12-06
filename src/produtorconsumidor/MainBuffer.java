package produtorconsumidor;

/**
 *
 * @author viniciuscoelho, thaismombach
 */

public class MainBuffer {

    // its port and elements' quantity
    public static void main(String []args){
        if ( args.length != 2 ){
            System.out.println("Wrong number of arguments!");
            System.exit(1);
        }
        else{
            Integer port = Integer.parseInt(args[0]);
            Integer elements = Integer.parseInt(args[1]);

            Buffer b = new Buffer(port, elements);
            Thread t1 = new Thread(b);

            t1.start();
        }
    }

}
