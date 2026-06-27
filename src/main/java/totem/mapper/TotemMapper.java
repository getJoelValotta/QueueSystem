package totem.mapper;

import shared.cliente.Cliente;
import shared.persistencia.ArchivosMapper;
import totem.Totem;

/**
 * Data Mapper del Totem. Mapea el dominio Totem hacia/desde {@link TotemDTO}.
 * Las subclases concretas implementan el formato (TXT / JSON / XML).
 */
public abstract class TotemMapper extends ArchivosMapper<TotemDTO> {

    @Override
    protected String nombreBaseArchivo() {
        return "totem";
    }

    public TotemDTO toDto(Totem totem) {
        if (totem == null) {
            return null;
        }
        long dni = (totem.getCliente() != null) ? totem.getCliente().getDni() : -1;
        return new TotemDTO(totem.getId(), dni);
    }

    public Totem toDominio(TotemDTO dto) {
        if (dto == null) {
            return null;
        }
        Cliente cliente = (dto.getDni() >= 0) ? new Cliente(dto.getDni()) : null;
        return new Totem(dto.getId(), cliente);
    }
}
