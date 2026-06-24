package shared.criptografia;

public class FactoryCriptografia {


    /**
     * Aplica el patrón Simple Factory para retornar la implementación adecuada
     * de ICriptografia basada en el enum solicitado.
     */
    public static ICriptografia getCifrador(String algoritmo) {
        if (algoritmo == null) {
            throw new IllegalArgumentException("El algoritmo criptográfico no puede ser nulo.");
        }
        switch (algoritmo) {
            case ICriptografia.AES:
                return new AES();
            case ICriptografia.CHACHA20:
                return new Chacha20();
            default:
                return null;
        }
    }
    public static void main(String[] args){
        String clave = "";
        String dni = "45031040";
        ICriptografia aes = getCifrador(ICriptografia.AES);
        ICriptografia chacha = getCifrador(ICriptografia.CHACHA20);

        String encriptado1, desencriptado1, encriptado2, desencriptado2;
        encriptado1 = aes.encriptar(dni, clave);
        System.out.println("ENCRIPTADO1 = " + encriptado1);
        desencriptado1 = aes.desencriptar(encriptado1, clave);
        System.out.println("DESENCRIPTADO1 = " + desencriptado1);
        encriptado2 = chacha.encriptar(dni, clave);
        System.out.println("ENCRIPTADO2 = " + encriptado2);
        desencriptado2 = chacha.desencriptar(encriptado2, clave);
        System.out.println("DESENCRIPTADO2 = " + desencriptado2);
    }
}
