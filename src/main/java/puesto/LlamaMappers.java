package puesto;

import puesto.persistencia.*;

public class LlamaMappers {
    public static void persistir(String modo, Puesto puesto) {
        if (modo.equals("txt")) {
            PuestoTXTMapper.getInstance("puestos.txt").save(puesto);
        } else if (modo.equals("xml")) {
            PuestoXMLMapper.getInstance("puestos.xml").save(puesto);
        } else if (modo.equals("json")) {
            PuestoJSONMapper.getInstance("puestos.json").save(puesto);
        } else {
            throw new IllegalArgumentException("Modo de persistencia no soportado: " + modo);
        }
    }

    public static void persistirConfig(String modo) {
        PuestoConfigTXTMapper.getInstance("puestoConfig.txt").save(modo);
    }

    public static Puesto cargarPuesto(String modo) throws RuntimeException {
        if (modo.equals("txt")) {
            return PuestoTXTMapper.getInstance("puestos.txt").load();
        } else if (modo.equals("xml")) {
            return PuestoXMLMapper.getInstance("puestos.xml").load();
        } else if (modo.equals("json")) {
            return PuestoJSONMapper.getInstance("puestos.json").load();
        } else {
            throw new IllegalArgumentException("Modo de persistencia no soportado: " + modo);
        }
    }

    public static String cargarConfig() throws RuntimeException {
        String modoConfig = PuestoConfigTXTMapper.getInstance("puestoConfig.txt").load();
        System.out.println("Modo de persistencia cargado: " + modoConfig);
        return modoConfig;
    }

}
