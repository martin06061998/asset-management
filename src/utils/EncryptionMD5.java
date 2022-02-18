/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.security.MessageDigest;

/**
 *
 * @author nguye
 */
public class EncryptionMD5 {

    public static String encryptionMD5(String pwd) throws Exception{
        String result = "";
        try {
            String algorithm = "MD5";
            byte[] bytesBuffer = pwd.getBytes();
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(bytesBuffer);
            byte[] hashedBytes = digest.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < hashedBytes.length; ++i) {
                sb.append(Integer.toHexString((hashedBytes[i] & 0xFF) | 0x100).substring(1, 3));
            }
            result = sb.toString();
        } catch (Exception ex) {
            throw ex;
        }
        return result;
    }
}
