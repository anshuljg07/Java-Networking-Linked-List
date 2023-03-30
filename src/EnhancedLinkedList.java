/**
 * The EnhancedLinkedList class is the LinkedList representation that is made up of Node classes and methods to
 * edit the LinkedList
 */
public class EnhancedLinkedList {
    /**
     * The Node instance variable head and tail serve as pointers to the head and tail of the LinkedList
     */
    private Node head, tail;
    /**
     * The int size is the number of Nodes that are contained in the LinkedList.
     */
    int size;

    // This was made a static class so that main cand directly access it.

    /**
     * The static class node was made a static class so that main can directly access it without having to instantiate
     * the Node class.
     */
    static class Node {
        /**
         * The int data is the data stored in Node object
         */
        int data;
        /**
         * The Node next is the reference to the next Node in the linked list
         */
        Node next;

        /**
         * The Node constructor with parameter int data initializes the data instance variable and sets the next reference
         * to null.
         * @param data
         */
        public Node(int data){
            this.data = data;
            next = null;
        }

    }

    /**
     * The EnhancedLinkedList() constructor with no parameters sets the head and tail references to null and initializes the size
     * instance variable to 0
     */
    public EnhancedLinkedList(){
        head = null;
        tail = head;
        size = 0;
    }

    /**
     * The overriden toString method creates the String representation of the EnchancedLinkedList class. It places the
     * node's data instance vars in a comma seperated sentece.
     * @return
     */
    @Override
    public String toString(){
        int counter = 0;
        Node temp = head;
        StringBuilder output = new StringBuilder();

        while(counter < size){
            output.append(temp.data).append(", ");
            temp = temp.next;
            counter++;
        }
        return output.toString();
    }

    /**
     * The insertatBack() method inserts int newData at the back of the linked list
     * @param newData int new data to be added
     */
    public void insertAtBack(int newData){
        tail.next = new Node(newData);
        tail = tail.next;
        size++;
    }

    /**
     * The insertAtFront() method inserts int newData at the front of the linked list.
     * @param newData int new data to be added
     */
    public void insertAtFront(int newData){
        Node temp = new Node(newData);
        temp.next = head;
        head = temp;
        size++;
    }

    /**
     * the deleteAtBack() method deletes the node at the back of the linkedlist
     */
    public void deleteAtBack(){
        if(size == 1){
            head = null;
            tail = null;
        }
        else{
            Node currptr = head;
            for(int i = 0; i < size - 1; i++){
                currptr = currptr.next;
            }
            currptr.next = null;
            tail = currptr;
        }
        size--;
    }

    /**
     * The deleteAtFront() method deletes the node at the back of the linked list
     */
    public void deleteAtFront(){
        if(size == 1){
            head = null;
            tail = null;
        }
        else{
            head = head.next;
        }
        size--;
    }

    /**
     * The insertAtIndex() method makes user of helper functions insertAtFront() and insertAtBack() but then defines
     * the functionality to add a Node at a specified index and shifting the remaining nodes to the right
     * @param newData int new data that is to be added to the list
     * @param index the index of where the new Node should go in the linked lsit
     */
    public void insertAtIndex(int newData, int index){
        if(size == 0){
            head = new Node(newData);
            tail = head;
            size++;
            return;
        }
        if(index == 0){
            insertAtFront(newData);
        }
        else if(index == size){
            insertAtBack(newData);
        }
        else{
            Node tobeadded = new Node(newData);
            Node laggingcurrptr = head;
            Node currptr = head.next;
            int counter = 1;

            while(laggingcurrptr.next != null){
                if(counter == index){
                    laggingcurrptr.next = tobeadded;
                    tobeadded.next = currptr;
                    size++;
                }
                if(currptr.next != null){
                    currptr = currptr.next;
                }
                laggingcurrptr = laggingcurrptr.next;
                counter++;
            }
        }
    }

    /**
     * The deleteAtIndex() method makes use of helper functions deleteAtFront() and deleteAtBack() and defines the functionality
     * to delete at other indexes. It deletes the Node at the specified index and then shifts the nodes past the delted index
     * to the left.
     * @param index the index corresponding to the Node that will be deleted in the linked list
     */
    public void deleteAtIndex(int index){
        if(size == 0){
            return;
        }
        if(index == 0){
            deleteAtFront();
        }
        else if(index == size - 1){
            deleteAtBack();
        }
        else {
            Node laggingcurrptr = head;
            Node currptr = head.next;
            int counter = 1;

            while(counter != index) {
                counter++;
                laggingcurrptr = laggingcurrptr.next;
                currptr = currptr.next;
            }

            laggingcurrptr.next = currptr.next; //skip the value in between them
            size--;
        }
    }
}
