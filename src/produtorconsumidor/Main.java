package produtorconsumidor;

/**
 *
 * @author viniciuscoelho, thaismombach
 */

public class Main {

    public static void main(String[] args) {
        GerenciadorBuffer gerenciador = new GerenciadorBuffer();
        gerenciador.createScenario();
        gerenciador.waitForConnections();
    }

}
