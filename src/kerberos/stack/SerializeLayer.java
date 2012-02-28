package kerberos.stack;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kerberos.protocol.dto.ASRequest;
import kerberos.protocol.dto.ASResponse;
import kerberos.protocol.dto.Encrypted;
import kerberos.protocol.dto.ServiceRequest;
import kerberos.protocol.dto.ServiceResponse;
import kerberos.protocol.dto.TGSRequest;
import kerberos.protocol.dto.TGSResponse;
import kerberos.serialize.Serializer;

public class SerializeLayer extends Layer<byte[], Object> {

    private final Map<String, Class<?>> types;

    /**
     * The SerializeLayer is responsible for serialization of objects to xml and
     * deserialization of xml to objects.
     * Constructor registers all supported types.
     */
    public SerializeLayer() {
        types = new HashMap<String, Class<?>>();

        types.put("encrypted", Encrypted.class);

        types.put("asRequest", ASRequest.class);
        types.put("tgsRequest", TGSRequest.class);
        types.put("serviceRequest", ServiceRequest.class);

        types.put("asResponse", ASResponse.class);
        types.put("tgsResponse", TGSResponse.class);
        types.put("serviceResponse", ServiceResponse.class);
    }

    /**
     * Converts a byte-array, that contains a xml-fragment to an object and
     * forwards it to the Layer above.
     * @param object the message from below
     * @throws Exception if the xml-fragment could not be deserialized or 
     *                   the Layer above was not able to process the object
     */
    @Override
    protected void receiveFromLower(byte[] object) throws Exception {
        String message = new String(object);
        System.out.println("SL received from lower: " + message);

        Matcher matcher = Pattern.compile("<[a-zA-Z0-9]+>").matcher(message);
        matcher.find();
        String tag = matcher.group();
        String tagName = tag.substring(1).substring(0, tag.length() - 2);

        Class<?> clazz = types.get(tagName);

        this.sendToUpper(Serializer.getInstance().deserialize(object, clazz));
    }

    /**
     * Converts the received object to a byte-array representation of a 
     * xml-fragment and forwards it to the layer below.
     * @param object the message from above
     * @throws Exception if the xml-fragment could not be serialized or
     *                   the Layer below was not able to process the object
     */
    @Override
    protected void receiveFromUpper(Object object) throws Exception {
        System.out.println("SL received from upper: " + object);
        byte[] data = Serializer.getInstance().serialize(object);
        System.out.println("SL received from upper: " + new String(data));
        this.sendToLower(data);
    }

}
