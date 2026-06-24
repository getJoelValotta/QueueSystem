package shared.criptografia;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES implements ICriptografia {

    private static final String ALGORITMO = "AES";
    private static final String TRANSFORMACION = "AES/ECB/PKCS5Padding";

    @Override
    public String encriptar(String cadena, String clave) {
        try {
            SecretKeySpec claveSecreta = generarClave(clave);

            Cipher cipher = Cipher.getInstance(TRANSFORMACION);
            cipher.init(Cipher.ENCRYPT_MODE, claveSecreta);

            byte[] textoEncriptado = cipher.doFinal(
                cadena.getBytes(StandardCharsets.UTF_8)
            );

            return Base64.getEncoder().encodeToString(textoEncriptado);

        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar con AES", e);
        }
    }

    @Override
    public String desencriptar(String cadena, String clave) {
        try {
            System.out.println("\n\ncadena = " + cadena);
            System.out.println("clave = "+ clave);
            System.out.println("\n");
            SecretKeySpec claveSecreta = generarClave(clave);

            Cipher cipher = Cipher.getInstance(TRANSFORMACION);
            cipher.init(Cipher.DECRYPT_MODE, claveSecreta);

            byte[] textoDecodificado = Base64.getDecoder().decode(cadena);
            byte[] textoDesencriptado = cipher.doFinal(textoDecodificado);

            return new String(textoDesencriptado, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar con AES", e);
        }
    }

    private SecretKeySpec generarClave(String clave) {
        byte[] keyBytes = new byte[16]; // AES-128 = 16 bytes
        byte[] claveOriginal = clave.getBytes(StandardCharsets.UTF_8);

        int longitud = Math.min(claveOriginal.length, 16);
        System.arraycopy(claveOriginal, 0, keyBytes, 0, longitud);

        return new SecretKeySpec(keyBytes, ALGORITMO);
    }
}