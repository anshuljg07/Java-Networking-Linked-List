import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * The Client class extends from JFrame and acts as the interface through which clients can modify the linked list
 * hosted in the Server class. It makes use of Datagrams to transmit packets to the server and receive packets.
 */
public class Client extends JFrame {
    /**
     * The JTextField deleteIndex takes userinput as to the index of the index corresponding to the element in the
     * linkedlist that the client wants to be deleted
     */
    private final JTextField deleteIndex;
    /**
     * The JTextField addIndex takes userinput for the index where the user wishes to insert an element into the
     * linked list
     */
    private final JTextField addIndex;
    /**
     * The JTextField addValue takes userinput for the value that the user wishes to insert as an element into the
     * linked list.
     */
    private final JTextField addValue;
    /**
     * The JButton addSubmitButton is the button the user clicks to submit their add request to the server class
     */
    private final JButton addSubmitButton;
    /**
     * The JButton deleteSubmitButton is the button the user clicks to submit their delete request to the server class
     */
    private final JButton deleteSubmitButton;
    /**
     * The JTextArea editHistory contains all of the packets sent and received by an instance of the Client class.
     */
    private final JTextArea edithistory;
    /**
     * The DatagramSocket socket serves as the funnel through which Dataagram pakcets are sent and received.
     */

    private DatagramSocket socket;

    /**
     * The Client constructor takes no parameters and instantiates all the JComponents while also instantiating a
     * Datagram socket for packet transmission and receiving.
     */
    public Client(){
        super("Linked List Editor (Client-Side)");
        setLayout(new FlowLayout());

        JLabel title = new JLabel("Linked List Viewer");
        JLabel options = new JLabel("Linked List Editing Options:");
        ButtonHandler buttonHandler = new ButtonHandler();

        JLabel deleteOption = new JLabel("delete @ ");
        deleteIndex = new JTextField("index");
        deleteSubmitButton = new JButton("Submit");
        deleteSubmitButton.addActionListener(buttonHandler);

        JLabel addOption = new JLabel("add ");
        addValue = new JTextField("value");
        JLabel atSymbol = new JLabel(" @ ");
        addIndex = new JTextField("index");
        addSubmitButton = new JButton("Submit");
        addSubmitButton.addActionListener(buttonHandler);

        edithistory = new JTextArea(8, 40);
        edithistory.setEditable(false);

        add(title);
        add(options);
        add(deleteOption);
        add(deleteIndex);
        add(deleteSubmitButton);
        add(addOption);
        add(addValue);
        add(atSymbol);
        add(addIndex);
        add(addSubmitButton);
        add(new JScrollPane(edithistory));

        setSize(520, 300);
        setVisible(true);

        try{
            socket = new DatagramSocket();
        }
        catch (SocketException socketException){
            socketException.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * The waitForPackets() method waits for transmission packets from the Datagram socket instantiated in the constructor.
     * It then adds the received packet to the editHistory TextArea.
     */
    public void waitForPackets(){
        while(true){
            try{
                byte[] data = new byte[150];
                DatagramPacket toReceivepacket = new DatagramPacket(data, data.length);

                socket.receive(toReceivepacket);

                String log = "PACKET FROM SERVER" + toReceivepacket.getAddress() + ", Host Port: " + toReceivepacket.getPort() + ", Data: " + new String(toReceivepacket.getData());edithistory.append(log + "\n");
                addToHistory(log);

            } catch (IOException ex) {
                addToHistory(ex.toString());
                ex.printStackTrace();
            }
        }
    }

    /**
     * The addToHistory() method appends a String s to the editHistory textArea object as a Runnable object
     * to make a thread-safe GUI modification
     * @param s is the String to be appended
     */
    public void addToHistory(String s){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                edithistory.append(s + "\n");
            }
        });
    }

    /**
     * The private ButtonHandler class implements ActionListener and serves as the ActionListener for the Client class.
     * and it sends packets if the deleteSubmitButton or the addSubmitButton are pressed
     */
    private class ButtonHandler implements ActionListener{

        /**
         * The overriden actionPerformed class with parameters ActionEvent e, create the API message based on the button clicked
         * and then sends it as a DatagramPacket to the Server class. It logs this API request in the historyLog.
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                String message;
                if(e.getSource() == addSubmitButton){
                    message = "ADD " + addValue.getText() + " " + addIndex.getText() + "!";
                }
                else if(e.getSource() == deleteSubmitButton){
                    message = "DEL " + deleteIndex.getText() + "!";
                }
                else{
                    message = "";
                }

                byte[] data = message.getBytes();

                //TODO: could be wrong IP Address and Port number
                DatagramPacket toSendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 23692);
                socket.send(toSendPacket);

                String logRequest = "CLIENT PACKET, From Client: " + toSendPacket.getAddress() + ", Host Port: " + toSendPacket.getPort() + ", Data: " + new String(toSendPacket.getData());
                addToHistory(logRequest);

            } catch (IOException ex) {
                addToHistory(ex.toString());
                ex.printStackTrace();
            }
        }
    }
}
