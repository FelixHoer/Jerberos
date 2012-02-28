package kerberos.stack;

import java.io.Closeable;
import java.io.IOException;

public abstract class Layer<LowerT, UpperT> implements Closeable {

    private Layer<UpperT, ?> upper;
    private Layer<?, LowerT> lower;

    /**
     * This method will be called with messages from the Layer above.
     * @param o the message from above
     * @throws Exception if the message could not be processed
     */
    protected void receiveFromUpper(UpperT o) throws Exception{};

    /**
     * This method will be called with messages from the Layer below.
     * @param o the message from below
     * @throws Exception if the message could not be processed
     */
    protected void receiveFromLower(LowerT o) throws Exception{};

    /**
     * Sends a message to the Layer above.
     * @param o the message to send
     * @throws Exception if the Layer above could not process the message
     */
    public void sendToUpper(UpperT o) throws Exception{
        if(upper != null)
            upper.receiveFromLower(o);
    }

    /**
     * Sends a message to the Layer below.
     * @param o the message to send
     * @throws Exception if the Layer below could not process the message
     */
    public void sendToLower(LowerT o) throws Exception{
        if(lower != null)
            lower.receiveFromUpper(o);
    }

    /**
     * Closes all opened resources.
     */
    @Override
    public void close() throws IOException {}

    public Layer<UpperT, ?> getUpper() {
        return upper;
    }
    public void setUpper(Layer<UpperT, ?> upper) {
        this.upper = upper;
    }

    public Layer<?, LowerT> getLower() {
        return lower;
    }
    public void setLower(Layer<?, LowerT> lower) {
        this.lower = lower;
    }

}
