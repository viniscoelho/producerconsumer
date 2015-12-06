package produtorconsumidor;

/**
 *
 * @author viniciuscoelho, thaismombach
 */

public class MainGerenciador {

    public static void main(String []args){

        Gerenciador g = new Gerenciador();
        g.waitForConnections();
    }
}
