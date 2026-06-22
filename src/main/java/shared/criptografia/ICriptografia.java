package shared.criptografia;

public interface ICriptografia {
    public static final String AES = "AES", CHACHA20 = "CHACHA20";

    public String encriptar(String cadena, String clave);
    public String desencriptar(String cadena, String clave);
}
