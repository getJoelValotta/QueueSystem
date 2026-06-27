package shared.turno.mapper.txt;

import shared.turno.mapper.TurnoDTO;
import shared.turno.mapper.TurnoMapper;

/**
 * Mapper TXT del Turno. Usa el mismo formato textual que
 * {@code shared.turno.TurnoToStringUtil}: {@code (estado;cantLlamados;idPuesto;dni)},
 * usando {@code -1} para los campos que no aplican.
 */
public class TurnoTXTMapper extends TurnoMapper {

    @Override
    protected String extension() {
        return "txt";
    }

    @Override
    public String serializar(TurnoDTO obj) {
        if (obj == null || obj.getEstado() == null) {
            return "(-1;-1;-1;-1)";
        }
        String idPuesto = (obj.getIdPuesto() == null) ? "-1" : obj.getIdPuesto();
        return "(" + obj.getEstado() + ";" + obj.getCantLlamados() + ";" + idPuesto + ";" + obj.getDni() + ")";
    }

    @Override
    public TurnoDTO deserializar(String data) {
        if (data == null) {
            return null;
        }
        String s = data.trim();
        if (!s.startsWith("(") || !s.endsWith(")")) {
            return null;
        }
        String[] partes = s.substring(1, s.length() - 1).split(";");
        if (partes.length != 4) {
            return null;
        }
        for (int i = 0; i < partes.length; i++) {
            partes[i] = partes[i].trim();
        }
        if (partes[0].equals("-1")) {
            return null;
        }
        TurnoDTO dto = new TurnoDTO();
        dto.setEstado(partes[0]);
        dto.setCantLlamados(Integer.parseInt(partes[1]));
        dto.setIdPuesto(partes[2].equals("-1") ? null : partes[2]);
        dto.setDni(Long.parseLong(partes[3]));
        return dto;
    }
}
