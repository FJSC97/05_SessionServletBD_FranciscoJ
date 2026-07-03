package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Seguridad
{
    public static String encriptar(String texto)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(texto.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash)
            {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e)
        {
            return texto;
        }
    }
}
