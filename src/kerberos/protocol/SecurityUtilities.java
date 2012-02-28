package kerberos.protocol;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SecurityUtilities {

    /**
     * Generates a new AES-Key.
     * @return the AES-Key
     */
    public static byte[] generateKey(){
        try {
            // Get the KeyGenerator
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128); // 192 and 256 bits may not be available
            //kgen.init(256); // needs unlimited jurisdiction files

            // Generate the secret key specs.
            SecretKey skey = kgen.generateKey();
            byte[] key = skey.getEncoded();

            return key;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Encrypts or decrypts the given data with the given Secret-Key.
     * @param data the data
     * @param key the Secret-Key
     * @param mode which operation to perform 
     *     (Cipher.DECRYPT_MODE or Cipher.ENCRYPT_MODE)
     * @return the encrypted or decrypted data
     * @throws Exception if the en/decryption failed
     */
    public static byte[] cipher(byte[] data, byte[] key, int mode) throws Exception {
        // generate secret key specs, provider independent
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

        // instantiate cipher
        Cipher cipher = Cipher.getInstance("AES");

        // encrypt/decrypt
        cipher.init(mode, keySpec);
        byte[] encrypted = cipher.doFinal(data);

        return encrypted;
    }

}
