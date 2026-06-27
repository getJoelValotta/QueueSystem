package server.mapper.txt;

import server.mapper.ConfigDTO;
import server.mapper.ConfigMapper;

/**
 * Mapper TXT de la configuracion. Formato {@code clave=valor} por linea.
 */
public class ConfigTXTMapper extends ConfigMapper {

    @Override
    protected String extension() {
        return "txt";
    }

    @Override
    public String serializar(ConfigDTO obj) {
        if (obj == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("metodoPersistencia=").append(nullToEmpty(obj.getMetodoPersistencia())).append("\n");
        sb.append("metodoEncriptacion=").append(nullToEmpty(obj.getMetodoEncriptacion())).append("\n");
        sb.append("claveEncriptacion=").append(nullToEmpty(obj.getClaveEncriptacion())).append("\n");
        return sb.toString();
    }

    @Override
    public ConfigDTO deserializar(String data) {
        if (data == null) {
            return null;
        }
        ConfigDTO dto = new ConfigDTO();
        for (String linea : data.split("\\r?\\n")) {
            int idx = linea.indexOf('=');
            if (idx < 0) {
                continue;
            }
            String clave = linea.substring(0, idx).trim();
            String valor = linea.substring(idx + 1);
            switch (clave) {
                case "metodoPersistencia":
                    dto.setMetodoPersistencia(valor);
                    break;
                case "metodoEncriptacion":
                    dto.setMetodoEncriptacion(valor);
                    break;
                case "claveEncriptacion":
                    dto.setClaveEncriptacion(valor);
                    break;
            }
        }
        return dto;
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
