package kerberos.stack;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;

import kerberos.protocol.client.SocketAddress;
import kerberos.stack.WriteQ.LockedOperation;

/**
 * The Stack builds upon the connection to the communication partner and 
 * provides the possibility to read and write whole objects.
 */
public class Stack implements Closeable {

    private BinaryStreamLayer binaryStreamLayer;
    private MessageParseLayer messageParseLayer;
    private SerializeLayer serializeLayer;
    private QueueLayer queneLayer;

    private ReadThread readThread;
    private WriteThread writeThread;

    /**
     * Connects to the given Address:Port and sets up the Stack.
     * @param address the address and port combination
     * @throws UnknownHostException can't happen, no host lookup used
     * @throws IOException if an I/O error occurs when creating the socket
     */
    public Stack(SocketAddress address) throws UnknownHostException, IOException {
        this(new Socket(address.getAddress(), address.getPort()));
    }

    /**
     * Sets up the Stack for a already established connection.
     * @param socket the established connection
     */
    public Stack(Socket socket) {
        binaryStreamLayer = new BinaryStreamLayer(socket);
        messageParseLayer = new MessageParseLayer();
        serializeLayer = new SerializeLayer();
        queneLayer = new QueueLayer();

        this.connectLayers(binaryStreamLayer, messageParseLayer);
        this.connectLayers(messageParseLayer, serializeLayer);
        this.connectLayers(serializeLayer, queneLayer);

        readThread = new ReadThread();
        writeThread = new WriteThread();
    }

    /**
     * Connects two Layers, one as lower and one as upper Layer.
     * @param lower the Layer below
     * @param upper the Layer above
     */
    private <T> void connectLayers(Layer<?, T> lower, Layer <T, ?> upper){
        lower.setUpper(upper);
        upper.setLower(lower);
    }

    /**
     * Reads and removes one received object or blocks until one arrives. 
     * @return a received object
     * @throws Exception if an exception occurs during blocking
     */
    public Object read() throws Exception{
        return queneLayer.getReadQ().take();
    }

    /**
     * Reads and removes all received objects. If no new objects have been 
     * received, a empty collection is returned.
     * @return a collection of received objects
     */
    public Collection<Object> readAvailable(){
        return queneLayer.getReadQ().takeAvailable();
    }

    /**
     * Sends an object to the communication partner and blocks until the 
     * transmission succeeds or fails.
     * @param o the object, that should be transmitted
     * @throws Exception if the object could not be sent
     */
    public void write(Object o) throws Exception{
        queneLayer.getWriteQ().add(o);
    }

    /**
     * Closes the connection.
     */
    @Override
    public void close() throws IOException {
        this.shutdown();

        binaryStreamLayer.close(); // should close socket
        messageParseLayer.close();
        serializeLayer.close();
        queneLayer.close(); // interrupt quene-waiters
    }

    /**
     * Stops the reading and writing of messages.
     */
    private void shutdown(){
        readThread.close();
        writeThread.close();
    }

    private class ReadThread extends Thread{

        private boolean open = true;

        /**
         * Creates and starts a Thread, that continuously reads messages,
         * transforms them to objects and buffers them.
         */
        public ReadThread() {
            this.start();
        }

        /**
         * Stops the reading of messages and interrupts the Read-Thread if it is
         * currently blocking.
         */
        public void close(){
            if(open){
                open = false;
                this.interrupt();
            }
        }

        /**
         * Reads binary messages and lets them go through the Layers from 
         * bottom to top.
         */
        @Override
        public void run(){
            try{
                while(open){
                    binaryStreamLayer.receive();
                }
            }catch(Exception e){
                if(open){
                    System.out.println("controlled read exception: " + e);
                    queneLayer.getReadQ().add(e);
                    Stack.this.shutdown();
                }
            }
        }

    }

    private class WriteThread extends Thread{

        private boolean open = true;

        /**
         * Creates and starts a Thread, that continuously transforms objects
         * to binary messages and sends them.
         */
        public WriteThread() {
            this.start();
        }

        /**
         * Stops the writing of messages and interrupts the Write-Thread if it 
         * is currently blocking.
         */
        public void close(){
            if(open){
                open = false;
                this.interrupt();
            }
        }

        /**
         * Lets the objects, that should be sent, go through the Layers from top
         * to bottom, where they will be transmitted.
         * Also notifies the Threads, that issued the write operation.
         */
        @Override
        public void run(){
            try{
                while(open){
                    LockedOperation lockOperation = queneLayer.getWriteQ().take();
                    try{
                        queneLayer.send(lockOperation.getData());
                        lockOperation.resume();
                    }catch(Exception e){
                        lockOperation.interrupt(e);
                        throw e;
                    }
                }
            }catch(Exception e){
                if(open){
                    System.out.println("controlled write exception: " + e);
                    Stack.this.shutdown();
                }
            }
        }

    }

}
