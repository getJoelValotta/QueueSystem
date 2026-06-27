package puesto.mapper;

import puesto.Puesto;
import shared.persistencia.ArchivosMapper;
import shared.turno.mapper.TurnoMapper;

/**
 * Data Mapper del Puesto. Compone un {@link TurnoMapper} (mismo formato) para
 * mapear el turno que el puesto esta atendiendo.
 */
public abstract class PuestoMapper extends ArchivosMapper<PuestoDTO> {

    protected TurnoMapper turnoMapper;

    @Override
    protected String nombreBaseArchivo() {
        return "puesto";
    }

    public void setTurnoMapper(TurnoMapper turnoMapper) {
        this.turnoMapper = turnoMapper;
    }

    public PuestoDTO toDto(Puesto puesto) {
        if (puesto == null) {
            return null;
        }
        return new PuestoDTO(puesto.getId(), turnoMapper.toDto(puesto.getTurno()));
    }

    public Puesto toDominio(PuestoDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Puesto(dto.getId(), turnoMapper.toDominio(dto.getTurno()));
    }
}
