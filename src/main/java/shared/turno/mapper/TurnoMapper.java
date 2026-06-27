package shared.turno.mapper;

import shared.cliente.Cliente;
import shared.persistencia.ArchivosMapper;
import shared.turno.Turno;
import shared.turno.TurnoAbandonado;
import shared.turno.TurnoAtendido;
import shared.turno.TurnoEnAtencion;
import shared.turno.TurnoState;

/**
 * Data Mapper del Turno. Mapea el dominio (Turno + patron State) hacia/desde su
 * {@link TurnoDTO}. Las subclases concretas implementan el formato (TXT / JSON / XML).
 */
public abstract class TurnoMapper extends ArchivosMapper<TurnoDTO> {

    public static final String ESPERA = "espera";
    public static final String ATENCION = "atencion";
    public static final String ATENDIDO = "atendido";
    public static final String ABANDONADO = "abandonado";

    @Override
    protected String nombreBaseArchivo() {
        return "turno";
    }

    public TurnoDTO toDto(Turno turno) {
        if (turno == null) {
            return null;
        }
        TurnoDTO dto = new TurnoDTO();
        dto.setDni(turno.getCliente() != null ? turno.getCliente().getDni() : -1);
        if (turno.estaEnEspera()) {
            dto.setEstado(ESPERA);
            dto.setCantLlamados(-1);
            dto.setIdPuesto(null);
        } else if (turno.estaEnAtencion()) {
            dto.setEstado(ATENCION);
            dto.setCantLlamados(turno.getCantLlamados());
            dto.setIdPuesto(turno.getIdPuesto());
        } else if (turno.estaAtendido()) {
            dto.setEstado(ATENDIDO);
            dto.setCantLlamados(-1);
            dto.setIdPuesto(turno.getIdPuesto());
        } else if (turno.estaAbandonado()) {
            dto.setEstado(ABANDONADO);
            dto.setCantLlamados(-1);
            dto.setIdPuesto(turno.getIdPuesto());
        }
        return dto;
    }

    public Turno toDominio(TurnoDTO dto) {
        if (dto == null || dto.getEstado() == null) {
            return null;
        }
        Turno turno = new Turno();
        turno.setCliente(new Cliente(dto.getDni()));
        switch (dto.getEstado()) {
            case ESPERA:
                // El turno nace en espera, no hay que hacer nada mas.
                break;
            case ATENCION:
                TurnoState atencion = new TurnoEnAtencion(turno, dto.getIdPuesto(), dto.getCantLlamados());
                turno.setEstado(atencion);
                break;
            case ATENDIDO:
                turno.setEstado(new TurnoAtendido(turno, dto.getIdPuesto()));
                break;
            case ABANDONADO:
                turno.setEstado(new TurnoAbandonado(turno, dto.getIdPuesto()));
                break;
            default:
                return null;
        }
        return turno;
    }
}
