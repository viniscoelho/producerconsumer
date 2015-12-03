package produtorconsumidor;

/**
 *
 * @author viniciuscoelho, thaismombach
 */

public class Main {

    public static void main(String[] args) {
        Gerenciador gerenciador = new Gerenciador();
        gerenciador.waitForConnections();
    }

}
