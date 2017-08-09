import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class ChatServer {
    static ArrayList<Socket> clientSockets;
    static ArrayList<String> loginNames;
    ChatServer() throws IOException{
        ServerSocket server = new ServerSocket(52);
        clientSockets = new ArrayList<>();
        loginNames = new ArrayList<>();

        while (true){
            Socket client  = server.accept();
            AcceptClient acceptClient = new AcceptClient(client);
        }
    }
    class AcceptClient extends Thread{
        Socket clientSocket;
        DataInputStream din;
        DataOutputStream dout;
        AcceptClient(Socket client) throws IOException{
            clientSocket = client;
            din = new DataInputStream(clientSocket.getInputStream());
            dout = new DataOutputStream(clientSocket.getOutputStream());
            String loginName = din.readUTF();
            loginNames.add(loginName);
            clientSockets.add(clientSocket);
            start();
        }

        @Override
        public void run() {
            while (true){
                try{
                    String msgFromClient = din.readUTF();
                    StringTokenizer st = new StringTokenizer(msgFromClient);
                    String loginName = st.nextToken();
                    String msgType = st.nextToken();
                    for (Socket socket: clientSockets
                         ) {
                        DataOutputStream pOut = new DataOutputStream(socket.getOutputStream());
                        pOut.writeUTF(loginName + " has logged in.");
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) throws IOException{
        ChatServer chatServer = new ChatServer();
    }
}
