package produtorconsumidor;

/**
 *
 * @author viniciuscoelho, thaismombach
 */

import java.util.Scanner;

public class Tayzuera {
    public static void main(String []args){
        Scanner scan = new Scanner(System.in);
        Produtor p;
        String ip = scan.next();
        Integer port = scan.nextInt();
        
        p = new Produtor(ip, port);
        Thread t1 = new Thread(p);
        
        t1.start();
    }
}
