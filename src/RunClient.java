import javax.swing.*;

/**
 * The RunClient class serves as the driver class for the client class. IT instantiates a Client class and enters a wait loop
 * to wait for packets from the Server.
 */
public class RunClient {
    /**
     * The main method for the RunClient driver class.
     * @param args arguments provided to the main method
     */
    public static void main(String[] args){
        Client application = new Client();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.waitForPackets(); //defined this function in Client.java
    }
}
