import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient extends JFrame implements Runnable{
    Socket socket;
    JTextArea textArea;
    Thread thread;
    DataInputStream din;
    DataOutputStream dout;
    String loginName;
    JButton send, logout;
    JTextField textField;
    ChatClient(String login)throws IOException{
        super(login);
        loginName = login;
        textArea = new JTextArea(18,50);
        textField = new JTextField(50);
        send = new JButton("Send");
        logout = new JButton("Logout");
        socket = new Socket("localhost",52);
        din = new DataInputStream(socket.getInputStream());
        dout = new DataOutputStream(socket.getOutputStream());
        dout.writeUTF(loginName);
        dout.writeUTF(loginName+" "+"LOGIN");
        send.addActionListener(l->performAction("DATA"));
        logout.addActionListener(l->performAction("LOGOUT"));
        thread = new Thread(this);
        thread.start();
        setup();
    }
    private void setup(){
        setSize(600,400);
        JPanel jPanel = new JPanel();
        jPanel.add(new JScrollPane(textArea));
        jPanel.add(textField);
        jPanel.add(send);
        jPanel.add(logout);
        add(jPanel);
        setVisible(true);

    }
    private void performAction(String action){
        try {
            dout.writeUTF(loginName+ action+textField.getText());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        while(true){
            try{
                textArea.append("\n"+din.readUTF());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws IOException{
        ChatClient chatClient = new ChatClient("User1");
    }
}
