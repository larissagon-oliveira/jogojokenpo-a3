import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    

    public static void main(String[] args) throws IOException {
    
        try{
            Scanner sc = new Scanner(System.in);
            
            System.out.println("Digite a IP do servidor: ");
            final String IP = sc.nextLine();

            System.out.println("Digite a porta do servidor: ");
            final int Porta = sc.nextInt();

            Socket socketClient = new Socket(IP, Porta);
            
            //Aqui é a Saída de dados do cliente
            PrintWriter saidasDados = new PrintWriter(socketClient.getOutputStream(), true);
            
            //E entrada de dados vinda direto do servidor
            BufferedReader entradaDados = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            
            while (true){
                // Requisição do servidor: 
                String entrada = entradaDados.readLine();
                if(entrada == null){
                    break;
                }
                System.out.println(entrada);

                /* Se entrada de dados vindo do servidor terminar com "Escolha", ativamos o buffer de digitação de saidas de dados do cliente*/

                if(entrada.endsWith("Escolha:")){
                    String saida = console.readLine();
                    saidasDados.println(saida);
                }
            }

            saidasDados.close();
            entradaDados.close();
            console.close();
            socketClient.close();

        }
        catch (Exception e) {
            System.out.println("Erro no cliente ao se conectado ao servidor: " + e.getMessage());
            return;
        }
    }
}
