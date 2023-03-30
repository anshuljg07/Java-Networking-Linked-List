import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * The Server class extends JFrame and houses the enhanced linked list which the Client class will send API requests to modify.
 */
public class Server extends JFrame {
    /**
     * The JTextArea is a JTextArea where the LinkedListDisplay String representation will be output to, so changes
     * to the linked list can be observed
     */
    private final JTextArea LinkedListDisplay; //could be a JLabel?
    /**
     * The JText Area edithistory serves as a log for all of the API requests made by the Server and received by the
     * Server
     */
    private final JTextArea edithistory;
    /**
     * The EnhacnedLinkedList linkedList instance variables is the linkedlist that the client will send API requests to
     * modify.
     */
    private final EnhancedLinkedList linkedList;
    /**
     * The DatagramSocket socket is the socket rhough with the server will send a receive Datagram packets to and from the client
     */

    private DatagramSocket socket;

    /**
     * The Server() constructor takes no parameters and instantiates all of the JComponent instance variables, while also instantaiting
     * the DatagramSocket at a specified port
     */
    public Server(){
        super("Linked List Editor (Server-Side)");
        setLayout(new FlowLayout());

        JLabel title = new JLabel("Linked List Viewer");
        JLabel historylabel = new JLabel("CLIENT / SERVER REQUEST HISTORY:");
        LinkedListDisplay = new JTextArea(2, 25);
        LinkedListDisplay.setEditable(false);
        edithistory = new JTextArea(8, 30);
        edithistory.setEditable(false);

        add(title);
        add(new JScrollPane(LinkedListDisplay));
        add(historylabel);
        add(new JScrollPane(edithistory)); //make the edithistory scrollable, since it will grow as clients edit the list
        setSize(400, 300);
        setVisible(true);

        linkedList = new EnhancedLinkedList();

        try{
            socket = new DatagramSocket(23692); // TODO: check and make sure that this is the correct port, could be 23690 - 23694 too
        }
        catch (SocketException socketException){
            socketException.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * The waitForPackets() is a wait loop where the server will wait for Datagram Packets from the client class and it will take
     * the requests to log them, read them, and then echo the request back to the client as confirmation.
     */
    public void waitForPackets(){
        while(true){
            try{
                byte[] data = new byte[150]; //could be larger/smaller if needed
                DatagramPacket toReceivePacket = new DatagramPacket(data, data.length);

                socket.receive(toReceivePacket); //wait to receive packet from client, contains a wait()?
                String logRequest = "CLIENT PACKET, From Client: " + toReceivePacket.getAddress() + ", Host Port: " + toReceivePacket.getPort() + ", Data: " + new String(toReceivePacket.getData());
                addToHistory(logRequest);

                readPacket(toReceivePacket); //read request from CLIENT using LL API, then modify/display new LL, and add to log

                sendPacketToClient(toReceivePacket); // echoes the received packet back to the client as confimation for recieved packet

            } catch (IOException e) {
                e.printStackTrace();
                displayLinkedList(e.toString()); // modify the gui to show that an error occured
                addToHistory(e.toString());
            }
        }
    }

    /**
     * The readPacket() method takes a packet recieved from the Client containg an API request to modify the linked list. Then using
     * the API language it makes the corresponding modfiication of the linkedlist
     * @param packet the DatagramPacket received from the client
     */
    public void readPacket(DatagramPacket packet){
        String dataTemp = new String(packet.getData());
        String[] data = dataTemp.split("!");
        data = data[0].split(" ");


        if(data.length > 1 && data.length < 4){
            if(data[0].equals("ADD") && data.length == 3){
                int newval = Integer.parseInt(data[1]);
                int index = Integer.parseInt(data[2]);
                linkedList.insertAtIndex(newval, index);
                displayLinkedList(linkedList.toString());
            }
            else if(data[0].equals("DEL") && data.length == 2){
                int index = Integer.parseInt(data[1]);
                linkedList.deleteAtIndex(index);
                displayLinkedList(linkedList.toString());
            }
            else {
                String logRequest = "API ERROR, From Client: " + packet.getAddress() + ", Host Port: " + packet.getPort() + ", Data: " + new String(packet.getData());
                addToHistory(logRequest);
            }
        }
        else{
            String logRequest = "API ERROR, From Client: " + packet.getAddress() + ", Host Port: " + packet.getPort() + ", Data: " + new String(packet.getData());
            addToHistory(logRequest);
        }
    }

    /**
     * The sendPacketToClient() method echos the packet received from the client back to the client
     * @param packet the DatagramPacket that will be sent to the Server class
     * @throws IOException the socket.send() throws IOExceptions
     */
    public void sendPacketToClient(DatagramPacket packet) throws IOException{
        DatagramPacket echoPacket = new DatagramPacket(packet.getData(), packet.getLength(), packet.getAddress(), packet.getPort());
        socket.send(echoPacket); //will enter a waiting period

        String logEcho = "ECHO PACKET SENT TO CLIENT" + echoPacket.getAddress() + ", Host Port: " + echoPacket.getPort() + ", Data: " + new String(echoPacket.getData());
        addToHistory(logEcho);
    }

    /**
     * The displayLinkedList() method defines a thread-safe GUI modification to display the current state of the linked
     * list
     * @param s
     */
    public void displayLinkedList(String s){ //creates a runnable to modify the swing gui, so that a lock is created
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LinkedListDisplay.setText(s + "\n");
            }
        });
    }

    /**
     * The addToHistory defines a thread-sfae GUI modification to append a request sent or received by the server
     * @param s
     */
    public void addToHistory(String s){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                edithistory.append(s + "\n");
            }
        });
    }
}
