## Configurando

Primeiramente, os buffers devem ser inicializados; na linha de comando, será perguntado quantos buffers você deseja ter na aplicação. Em seguida, janelas aparecerão perguntando onde deseja executar os buffers, solicitando uma conexão ssh. Após feito isso, será solicitando que seja fornecido um comando via terminal para a execução do buffer. A chamada do método de inicialização é a mesma para todas as classes; o único diferencial é a ordem em que devem ser executados. Seus parâmetros de execução são: porta e quantidade de elementos.

Em seguida, os gerenciadores devem ser inicializados (o número "2" deve ser fornecido no prompt de comando); primeiramente o de buffer, e depois o gerenciador (que receberá as requisições do produtor e consumidor). Os parâmetros de execução do gerenciador de buffer são: quantidade de buffers, pares de endereço ip e porta de cada um dos buffers. Já para o gerenciador, deve ser fornecido o endereço ip do gerenciador de buffer e a porta que ele estará ouvindo.

Por fim, será solicitado para fornecer a quantidade de produtores e consumidores. A ordem em si não importa. Os parâmetros de execução de ambos são: endereço ip do gerenciador e a porta que ele estará ouvindo.

Após o término de execução do configurador, é possível que se adicione mais produtores e consumidores.
