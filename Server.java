import java.net.Socket;
import java.net.ServerSocket;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;

public class Server{
    private static int PORTA= 1234;
    private static int MAXIMO_JOGADERES= 2;


    public static void main(String[] args) throws IOException {
        //Criando um servidor: 

        System.out.println("Inicio metodo main");
   
        ServerSocket serverSocket = new ServerSocket(PORTA);

        try{
            System.out.println("Servidor disponível na porta: " + PORTA);
            
        } catch (Exception e) {
            System.out.println("Erro na inicialização do servidor");
            System.out.println("Erro: " + e.getMessage());
            return;
        }


        // Aqui utilizamos velotes, pois são multiplas conexões
        Socket[] socketsClient = new Socket[MAXIMO_JOGADERES];
        PrintWriter[] saidasDados = new PrintWriter[MAXIMO_JOGADERES];
        BufferedReader[] leiturasDados = new BufferedReader[MAXIMO_JOGADERES];
        int clientesConectadosMultiplayer = 0;

        while(true) {
            Socket clienteNovo = serverSocket.accept();
            PrintWriter saidaDados = new PrintWriter(clienteNovo.getOutputStream(), true);
            BufferedReader leituraDados = new BufferedReader(new InputStreamReader(clienteNovo.getInputStream()));

            saidaDados.println("--------------------Bem vindo ao jogo JOKENPÔ (Anhembi Morumbi)----------------------\n");

            int escolhaClient = -1;
            while(true) {
                saidaDados.println("Você gostaria de jogar contra o computador, ou contra um jogador?\n0 -> computador\n1 -> jogador");
                saidaDados.println("Escolha:");

                try {
                    escolhaClient = Integer.parseInt(leituraDados.readLine());
                } catch (IOException e) {
                    System.out.println("Jogador digitou uma escolha inválida.");
                }
                if (escolhaClient < 0 || escolhaClient > 1) {
                    saidaDados.println("\nEscolha inválida, tente novamente.\n");
                }
                else {
                    break;
                }
            }

            if (escolhaClient == 0) {
                System.out.println("Carregando jogo singleplayer...");
                Jogo jogo = new Jogo(clienteNovo, saidaDados, leituraDados);
                jogo.run();
            }
            
            else if (escolhaClient == 1) {
                // Laços de repetições = que espera até que o número máximo de jogadores se conecte
                try {
                    //Inclusão do novo cliente que aceitou no vetor de clientes
                    socketsClient[clientesConectadosMultiplayer] = clienteNovo;
                    
                    /* PrintWriter e BufferedReader são, classes para escrita e leitura de dados
                    * via console ou arquivos. Nessa aplicação vamos usar interação via console 
                    * carregando nos vetores de saida e leitura de dados dos jogadores */ 
                    saidasDados[clientesConectadosMultiplayer] = saidaDados;
                    leiturasDados[clientesConectadosMultiplayer] = leituraDados;

                    clientesConectadosMultiplayer++;
                }catch (Exception e) {
                    System.out.println("Erro na conexão dos clientes ao servidor");
                }

                /*E criamos a Linha responsavel pelo controle do jogo
                * nessa passamos os parametros dos clientes carregados nós vetores*/
                if (clientesConectadosMultiplayer == MAXIMO_JOGADERES) {
                    System.out.println(socketsClient[0]);
                    System.out.println(saidasDados[0]);
                    System.out.println(leiturasDados[0]);
                    System.out.println("Carregando jogo multiplayer...");
                    Jogo jogo = new Jogo(socketsClient, saidasDados, leiturasDados);
                    jogo.run();
                }
            }
        }
        
    }
}