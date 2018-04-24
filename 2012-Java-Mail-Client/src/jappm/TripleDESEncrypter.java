/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jappm;

import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Ivan
 */
public class TripleDESEncrypter {

    private Cipher ecipher;
    private Cipher dcipher;
    private final String KEYWORD = "JAPPMSecurityWordIIIIIII";

    public TripleDESEncrypter() {
        try {

            byte[] keyB = KEYWORD.getBytes();  // a Triple DES key is a byte[24] array

            SecretKey key = new SecretKeySpec(keyB, "DESede");

            ecipher = Cipher.getInstance("DESede");
            dcipher = Cipher.getInstance("DESede");
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);

        } catch (javax.crypto.NoSuchPaddingException e) {
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch (java.security.InvalidKeyException e) {
        }
    }

    public String encrypt(String str) {
        try {

            if (str == null || str.isEmpty()) {
                return "";
            }

            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            return new sun.misc.BASE64Encoder().encode(enc);

        } catch (javax.crypto.BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (java.io.IOException e) {
        }
        return null;
    }

    public String decrypt(String str) {
        try {

            if (str == null || str.isEmpty()) {
                return "";
            }

            // Decode base64 to get bytes
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);

            // Decode using utf-8
            return new String(utf8, "UTF8");
        } catch (javax.crypto.BadPaddingException e) {
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (java.io.IOException e) {
        }
        return "";
        // return null;
    }
}
