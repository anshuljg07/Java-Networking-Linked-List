import javax.swing.*;

/**
 * The RunServer class serves as the driver class for the Server class. It instantiates a Server class while also entering
 * a wait loop to wait for incoming packets from the client class.
 */
public class RunServer {
    public static void main(String[] args){
//        EnhancedLinkedList LL = new EnhancedLinkedList(10);
        Server application  = new Server();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.waitForPackets(); //defined this function in Server.java

    }
}
