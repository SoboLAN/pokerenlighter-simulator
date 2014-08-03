package org.javafling.pokerenlighter.main;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Provides functionality for hashing Strings using the MD5 hashing algorithm.
 * 
 * @author Radu Murzea
 * 
 * @version 1.0
 */
public class MD5Hasher
{
    private static final String emptyString = "";
    
    /**
     * Computes the MD5 hash of the specified String. This method assumes that the content is encoded
     * as UTF-8.
     * 
     * @param content the String to be hashed. Can be any length (including 0).
     * 
     * @return the MD5 hash. If the content was null or if there was a problem, an empty String is returned.
     */
    public static String hash(String content)
    {
        if (content == null) {
            return emptyString;
        }
    
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return emptyString;
        }

        md.reset();

        try {
            md.update(content.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return emptyString;
        }
        
        byte[] digest = md.digest();
        BigInteger bigint = new BigInteger(1, digest);
        String hashtext = bigint.toString(16);

        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }

        return hashtext;
    }
}
