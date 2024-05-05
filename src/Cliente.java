import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Cliente implements ChatClient {

    private String nome;

    public String getNome() {
        return nome;
    }

    public Cliente(String nome) throws RemoteException{
        this.nome = nome;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String servidorIP = "192.168.168.25";
        try{

            Registry registro = LocateRegistry.getRegistry(servidorIP, 1099);
            ChatServer stub = (ChatServer) registro.lookup("Chat");
            System.out.println("Qual o seu nome?");
            Cliente cliente = new Cliente(in.nextLine());


            ChatClient clienteStub = (ChatClient) UnicastRemoteObject.exportObject(cliente, 0);

            // Adiciona o cliente ao servidor
            stub.adicionarCliente(cliente);

            while (true){

                stub.enviarMsg(cliente.getNome(), in.nextLine());
                System.out.println("\033[1A\033[K");
                System.out.flush();
            }
        }
        catch (Exception e){
            System.out.println("Erro cliente :" + e.getMessage());
        }
    }

    @Override
    public void receberMsg(String nome, String msg) throws RemoteException {
        msg = (nome.equalsIgnoreCase(this.getNome()) ? "Você: " + msg : nome + ":" + msg);
        System.out.println(msg);
    }
}
