package puesto;

import puesto.persistencia.*;

public class LlamaMappers {
    public static void Persistir(String modo, Puesto puesto) {
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

}
