package puesto;

import puesto.persistencia.*;

public class LlamaMappers { // TODO: PERSISTIR TAMBIEN TIPO ENCRIPTACION

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

    public static void persistirConfig(String modoPersistencia, String modoEncriptacion) {
        ConfigPuesto config = new ConfigPuesto(modoPersistencia, modoEncriptacion);
        PuestoConfigTXTMapper.getInstance("puestoConfig.txt").save(config);
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

    public static ConfigPuesto cargarConfig() throws RuntimeException {
        ConfigPuesto config = PuestoConfigTXTMapper.getInstance("puestoConfig.txt").load();
        System.out.println(
                "Configuración cargada: " + config.getModoPersistencia() + ", " + config.getModoEncriptacion());
        return config;
    }

}
