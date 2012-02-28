package kerberos.stack;

public class QueueLayer extends Layer<Object, Object> {

    private final ReadQ readQ;
    private final WriteQ writeQ;

    /**
     * The QueueLayer provides read and write access to the Stack for external 
     * Threads. 
     */
    public QueueLayer() {
        readQ = new ReadQ();
        writeQ = new WriteQ();
    }

    /**
     * Adds all received objects to the Read-Queue, where external Threads can 
     * pick them up.
     * @param object the message from below
     */
    @Override
    protected void receiveFromLower(Object o){
        System.out.println("QL received from lower: " + o);
        readQ.add(o);
    }

    /**
     * Sends a message to the Layer below.
     * @param o the message to send
     * @throws Exception if the Layer below could not process the message
     */
    public void send(Object object) throws Exception{
        this.sendToLower(object);
    }

    /**
     * Terminates the Read- and Write-Queue.
     */
    @Override
    public void close(){
        readQ.terminate(new Exception("closed"));
        writeQ.terminate(new Exception("closed"));
    }

    public ReadQ getReadQ() {
        return readQ;
    }

    public WriteQ getWriteQ() {
        return writeQ;
    }

}
