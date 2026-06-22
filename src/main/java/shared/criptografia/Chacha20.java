package shared.criptografia;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

public class Chacha20 implements ICriptografia {

    // Registro del Security Provider de BouncyCastle a nivel de JVM
    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private static final String ALGORITHM = "ChaCha20-Poly1305";
    private static final int NONCE_LENGTH = 12;

    private SecretKeySpec getSecretKey(String password) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, "ChaCha20");
    }

    @Override
    public String encriptar(String cadena, String clave){
        try {
            SecretKeySpec secretKey = getSecretKey(clave);
            // Ahora la JVM encontrará el algoritmo a través de BouncyCastle
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            
            byte[] nonce = new byte[NONCE_LENGTH];
            new SecureRandom().nextBytes(nonce);
            IvParameterSpec ivSpec = new IvParameterSpec(nonce);
            
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] cipherText = cipher.doFinal(cadena.getBytes(StandardCharsets.UTF_8));
            
            ByteBuffer byteBuffer = ByteBuffer.allocate(nonce.length + cipherText.length);
            byteBuffer.put(nonce);
            byteBuffer.put(cipherText);
            
            return Base64.getEncoder().encodeToString(byteBuffer.array());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Nota técnica: Enmascarar fallos criptográficos con null es un antipatrón de seguridad.
    }

    @Override
    public String desencriptar(String cadena, String clave) {
        try {
            SecretKeySpec secretKey = getSecretKey(clave);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            
            byte[] cipherTextWithNonce = Base64.getDecoder().decode(cadena);
            ByteBuffer byteBuffer = ByteBuffer.wrap(cipherTextWithNonce);

            byte[] nonce = new byte[NONCE_LENGTH];
            byteBuffer.get(nonce);
            byte[] cipherText = new byte[byteBuffer.remaining()];
            byteBuffer.get(cipherText);
        
            IvParameterSpec ivSpec = new IvParameterSpec(nonce);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

            byte[] plainText = cipher.doFinal(cipherText);
            return new String(plainText, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}