package server.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import server.ListaTurnos;
import server.Server;
import server.id.GestorID;
import shared.persistencia.ArchivosMapper;
import shared.turno.Turno;
import shared.turno.mapper.TurnoDTO;
import shared.turno.mapper.TurnoMapper;

/**
 * Data Mapper del Server. Mapea el estado del servidor (4 listas de turnos +
 * contadores del GestorID) hacia/desde {@link ServerDTO}, componiendo un
 * {@link TurnoMapper} del mismo formato.
 *
 * <p>Ofrece ademas la optimizacion "primero todo completo, luego appends":
 * {@link #agregaTurnoEnEspera(ServerDTO, TurnoDTO)} agrega un unico turno nuevo
 * al final del archivo cuando el formato lo permite (TXT). Los turnos que
 * cambian de lista (atencion/atendido/abandonado) disparan una reescritura
 * completa via {@link #templateGrabar(Object)}.</p>
 */
public abstract class ServerMapper extends ArchivosMapper<ServerDTO> {

    public static final String CAT_GESTOR = "GESTOR";
    public static final String CAT_ESPERA = "ESPERA";
    public static final String CAT_ATENCION = "ATENCION";
    public static final String CAT_ABANDONADO = "ABANDONADO";
    public static final String CAT_ATENDIDO = "ATENDIDO";

    protected TurnoMapper turnoMapper;

    @Override
    protected String nombreBaseArchivo() {
        return "server";
    }

    public void setTurnoMapper(TurnoMapper turnoMapper) {
        this.turnoMapper = turnoMapper;
    }

    // ----------------------------------------------------------------------
    // Mapeo dominio <-> DTO
    // ----------------------------------------------------------------------

    public ServerDTO toDto(Server server, GestorID gestor) {
        ServerDTO dto = new ServerDTO();
        dto.setEnEspera(aDtos(server.getEnEspera()));
        dto.setEnAtencion(aDtos(server.getEnAtencion()));
        dto.setAbandonados(aDtos(server.getAbandonados()));
        dto.setAtendidos(aDtos(server.getAtendidos()));
        if (gestor != null) {
            dto.setContadorTotem(gestor.getContadorTotem());
            dto.setContadorPuesto(gestor.getContadorPuesto());
            dto.setContadorMonitor(gestor.getContadorMonitor());
        }
        return dto;
    }

    /** Vuelca el DTO leido sobre el Server y el GestorID existentes. */
    public void cargarEnServer(ServerDTO dto, Server server, GestorID gestor) {
        if (dto == null) {
            return;
        }
        server.inicializaListas(aLista(dto.getEnEspera()), aLista(dto.getEnAtencion()),
                aLista(dto.getAbandonados()), aLista(dto.getAtendidos()));
        if (gestor != null) {
            gestor.setContadorTotem(dto.getContadorTotem());
            gestor.setContadorPuesto(dto.getContadorPuesto());
            gestor.setContadorMonitor(dto.getContadorMonitor());
        }
    }

    private List<TurnoDTO> aDtos(ListaTurnos lista) {
        List<TurnoDTO> out = new ArrayList<>();
        if (lista != null) {
            Iterator<Turno> it = lista.devuelveIterator();
            while (it.hasNext()) {
                TurnoDTO dto = turnoMapper.toDto(it.next());
                if (dto != null) {
                    out.add(dto);
                }
            }
        }
        return out;
    }

    private ListaTurnos aLista(List<TurnoDTO> dtos) {
        ConcurrentLinkedQueue<Turno> cola = new ConcurrentLinkedQueue<>();
        if (dtos != null) {
            for (TurnoDTO dto : dtos) {
                Turno turno = turnoMapper.toDominio(dto);
                if (turno != null) {
                    cola.offer(turno);
                }
            }
        }
        return new ListaTurnos(cola);
    }

    // ----------------------------------------------------------------------
    // Optimizacion de append
    // ----------------------------------------------------------------------

    /**
     * Agrega de forma eficiente un turno nuevo a la lista de espera. Si el
     * formato soporta append y ya hay archivo, agrega solo una linea; en caso
     * contrario (primer grabado o formato estructurado) reescribe el snapshot
     * completo del server.
     *
     * @param server      server (para reconstruir el snapshot de fallback)
     * @param gestor      gestor de ids (contadores del snapshot)
     * @param turnoNuevo  turno nuevo que entra en espera
     */
    public void agregaTurnoEnEspera(Server server, GestorID gestor, Turno turnoNuevo) {
        if (soportaAppend() && existeArchivo()) {
            appendLinea(lineaTurno(CAT_ESPERA, turnoMapper.toDto(turnoNuevo)));
        } else {
            templateGrabar(toDto(server, gestor));
        }
    }

    /**
     * Serializa un unico turno como linea apta para append. Solo lo implementa
     * el mapper TXT; los formatos estructurados nunca llaman a este metodo
     * porque {@link #soportaAppend()} es false.
     */
    protected String lineaTurno(String categoria, TurnoDTO dto) {
        return null;
    }
}
