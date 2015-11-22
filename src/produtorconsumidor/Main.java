package produtorcosumidorrmi;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        GerenciadorBuffer gerenciador = new GerenciadorBuffer();
        gerenciador.createScenario();
        gerenciador.waitForConnections();
    }

}
