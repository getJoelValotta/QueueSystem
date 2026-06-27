package totem.mapper.txt;

import totem.mapper.TotemDTO;
import totem.mapper.TotemMapper;

/**
 * Mapper TXT del Totem. Formato: {@code id;dni} (dni = -1 si no hay cliente).
 */
public class TotemTXTMapper extends TotemMapper {

    @Override
    protected String extension() {
        return "txt";
    }

    @Override
    public String serializar(TotemDTO obj) {
        if (obj == null) {
            return null;
        }
        String id = (obj.getId() == null) ? "" : obj.getId();
        return id + ";" + obj.getDni();
    }

    @Override
    public TotemDTO deserializar(String data) {
        if (data == null) {
            return null;
        }
        String[] partes = data.trim().split(";", 2);
        TotemDTO dto = new TotemDTO();
        dto.setId(partes[0].isEmpty() ? null : partes[0].trim());
        if (partes.length > 1 && !partes[1].trim().isEmpty()) {
            dto.setDni(Long.parseLong(partes[1].trim()));
        } else {
            dto.setDni(-1);
        }
        return dto;
    }
}
