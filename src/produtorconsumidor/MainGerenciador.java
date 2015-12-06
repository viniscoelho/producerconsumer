package produtorconsumidor;

public class MainGerenciador {

    public static void main(String []args){
        if ( args.length != 2 ){
            System.out.println("Wrong number of arguments!");
            System.exit(1);
        }
        else{
            String ip = args[0];
            Integer port = Integer.parseInt(args[1]);

            Produtor p = new Produtor(ip, port);
            Thread t1 = new Thread(p);

            t1.start();
        }
    }
}
