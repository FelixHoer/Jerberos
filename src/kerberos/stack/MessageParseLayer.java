package kerberos.stack;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParseLayer extends Layer<byte[], byte[]> {

    private byte[] bytes = null;

    /**
     * The MessageParseLayer is responsible for extracting whole xml-fragments
     * from the incoming bytes.
     */
    public MessageParseLayer() {}

    /**
     * Forwards the message from above to the Layer below.
     * @param object the message from above
     * @throws Exception if the Layer below was not able to process the object
     */
    @Override
    protected void receiveFromUpper(byte[] object) throws Exception {
        System.out.println("MPL received from upper: " + object);
        this.sendToLower(object);
    }

    /**
     * Extracts whole xml-fragments from the incoming bytes.
     * Buffers the incoming bytes and sends the extracted xml to the layer 
     * above.
     * @param object the message from below
     * @throws Exception if the Layer above was not able to process the object
     */
    @Override
    protected void receiveFromLower(byte[] object) throws Exception {
        System.out.println("MPL received from lower: " + object);
        processReceivedBytes(object);
    }

    /**
     * Extracts xml-fragments and sends them to the layer above.
     * @param data received data
     * @throws Exception if the Layer above was not able to process the object
     */
    private void processReceivedBytes(byte[] data) throws Exception {

        if (bytes != null) {
            byte[] newBytes = new byte[bytes.length + data.length];
            System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
            System.arraycopy(data, 0, newBytes, bytes.length, data.length);
            bytes = newBytes;
        } else {
            bytes = data;
        }

        String input = new String(bytes);

        Matcher startMatcher = Pattern.compile("<[a-zA-Z0-9]+>").matcher(input);
        boolean hasStart = startMatcher.find();
        if (!hasStart)
            return;

        String startTag = startMatcher.group();
        int startIndex = startMatcher.start();
        String tagName = startTag.substring(1).substring(0, startTag.length() - 2);
        String endTag = "</" + tagName + ">";

        LinkedList<Integer> starts = findIndexes(input, startTag, startIndex);
        LinkedList<Integer> ends = findIndexes(input, endTag, startIndex);

        int endIndex = 0;
        for(Integer end: ends){
            int removed = removePreceding(starts, end);
            if(removed == -1)
                throw new Exception("message corrupt");
            if(starts.size() == 0){
                endIndex = end;
                break;
            }
        }
        if(endIndex == 0)
            return;

        endIndex += endTag.length();

        String remains = input.substring(endIndex);
        bytes = remains.getBytes();

        String message = input.substring(startMatcher.start(), endIndex);
        this.sendToUpper(message.getBytes());
    }

    /**
     * Removes the opening tag for the end tag at given position.
     * @param starts indexes of all opening tags
     * @param end the position of the end tag
     * @return the index of the removed tag
     */
    private int removePreceding(LinkedList<Integer> starts, int end){
        int prev = -1;
        for(Integer start: starts){
            if(start >= end)
                break;
            prev = start;
        }
        if(prev != -1)
            starts.remove((Object) (Integer) prev);
        return prev;
    }

    /**
     * Returns all indexes, that match the given pattern and are after the
     * start index.
     * @param input the string
     * @param pattern the pattern
     * @param startIndex the index, after which will be searched
     * @return a list of indexes, that match the pattern
     */
    private LinkedList<Integer> findIndexes(String input, String pattern, int startIndex){
        LinkedList<Integer> matches = new LinkedList<Integer>();
        int fromIndex = startIndex;
        int index = 0;
        while((index = input.indexOf(pattern, fromIndex)) != -1){
            matches.add(index);
            fromIndex = index + 1;
        }
        return matches;
    }

}
