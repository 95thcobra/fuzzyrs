package com.rs.utils.file;

import java.security.MessageDigest;

/**
 * Handles the encryption of player passwords.
 *
 * @author Apache Ah64
 */
public class Encrypt {

    /**
     * Encrypt the string using the SHA-1 encryption algorithm.
     *
     * @param string The string.
     * @return The encrypted string.
     */
    public static String encryptSHA1(final String string) {
        String hash = null;
        try {
            hash = byteArrayToHexString(hash(string));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return hash;
    }

    /**
     * Encrypt the string to a SHA-1 hash.
     *
     * @param x The string to encrypt.
     * @return The byte array.
     * @throws Exception when an exception occurs.
     */
    public static byte[] hash(final String x) throws Exception {
        MessageDigest string;
        string = MessageDigest.getInstance("SHA-1");
        string.reset();
        string.update(x.getBytes());
        return string.digest();
    }

    /**
     * Converts a byte array to hex string.
     *
     * @param b The byte array.
     * @return The hex string.
     */
    public static String byteArrayToHexString(final byte[] b) {
        final StringBuffer string = new StringBuffer(b.length * 2);
        for (final byte element : b) {
            final int v = element & 0xff;
            if (v < 16) {
                string.append('0');
            }
            string.append(Integer.toHexString(v));
        }
        return string.toString();
    }
}