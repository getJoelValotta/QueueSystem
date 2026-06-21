package puesto;

import puesto.persistencia.PuestoTXTMapper;

public class LlamaMappers {
    public static void Persistir(String modo, Puesto puesto) {
        if (modo.equals("txt")) {
            PuestoTXTMapper.getInstance("puestos.txt").save(puesto);
        } // else if(modo.equals("xml")){
          // new PuestoXMLMapper("puestos.xml").save(puesto);

        // }else if(modo.equals("json")){
        // return new PersistenciaJson();
        // }
        else {
            throw new IllegalArgumentException("Modo de persistencia no soportado: " + modo);
        }
    }

}
