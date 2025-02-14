package main;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Seguridad
{
    public static String hashPassword(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            return converToHex(digest);
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }

    private static String converToHex(byte[] digest){
        StringBuilder hexString = new StringBuilder();
        for(byte b : digest){
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1){
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }
}
