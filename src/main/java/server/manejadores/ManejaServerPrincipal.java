package server.manejadores;

import java.io.IOException;
import java.net.SocketException;

import server.ListaTurnos;
import server.id.GestorID;
import shared.cliente.Cliente;
import shared.cliente.ClienteDniInvalidoException;
import shared.cliente.ClienteDniVacioException;
import shared.turno.*;
import admin.AdminComunicaServerP;

public class ManejaServerPrincipal extends ManejadorDeNodos implements IManejaServidores, IControllerObserver {
    private int cantErrores;
    //private ManejadorEventListener controllerServer;

    public ManejaServerPrincipal(ManejadorEventListener controllerServer, String id) {
        super(controllerServer, id);
        this.cantErrores = 0;
    }

    @Override
    public void comunicacion() {
        String dni; 
        String idPuesto = "-1";
        int cantLlamados = -1;
        Turno turno;
        try {
            System.out.println("ENTRE A ESCUCHAR AL PRINCIPAL");
            socket.setSoTimeout(IManejaServidores.TIMEOUT_CAIDA_MS); // Es el tiempo que espera a que le llegue algo y determinar si se cayo o no (hearthbeat)                              
            String respuesta = in.readUTF();
            System.out.println("RESPUESTA = " + respuesta);
            switch (respuesta) {

                case IManejaServidores.GESTOR:
                    String totem, puesto, monitor;
                    totem = in.readUTF();
                    puesto = in.readUTF();
                    monitor = in.readUTF();
                    System.out.println("TOTEM = " + totem);
                    System.out.println("PUESTO = " + puesto);
                    System.out.println("MONITOR = " + monitor);
                    System.out.println("TERMINE DE LEER LAS ID DE NODOS");
                    controllerServer.recibeYPersisteGestor(totem, puesto, monitor);
                    System.out.println("TERMINE DE RECIBIR Y PERSISTIR GESTOR");
                    break;

                case IManejaServidores.TURNO_ESPERA:
                    dni = in.readUTF();
                    turno = new Turno();
                    try {
                        turno.setCliente(new Cliente(dni));
                    } catch (ClienteDniVacioException | ClienteDniInvalidoException e) {}
                    controllerServer.recibeTurnoEnRespaldo(turno);
                    break;

                case IManejaServidores.TURNO_ATENCION:
                    dni = in.readUTF();
                    idPuesto = in.readUTF();
                    cantLlamados = Integer.parseInt(in.readUTF());
                    turno = new Turno();
                    turno.setEstado(new TurnoEnAtencion(turno, idPuesto,cantLlamados));
                    try {
                        turno.setCliente(new Cliente(dni));
                    } catch (ClienteDniVacioException | ClienteDniInvalidoException e) {}
                    controllerServer.recibeTurnoEnRespaldo(turno);
                    break;

                case IManejaServidores.TURNO_ATENDIDO:
                    dni = in.readUTF();
                    idPuesto = in.readUTF();
                    turno = new Turno();
                    turno.setEstado(new TurnoAtendido(turno, idPuesto));
                    try {
                        turno.setCliente(new Cliente(dni));
                    } catch (ClienteDniVacioException | ClienteDniInvalidoException e) {}
                    controllerServer.recibeTurnoEnRespaldo(turno);
                    break;

                case IManejaServidores.TURNO_ABANDONADO:
                    dni = in.readUTF();
                    idPuesto = in.readUTF();
                    turno = new Turno();
                    turno.setEstado(new TurnoAbandonado(turno, idPuesto));
                    try {
                        turno.setCliente(new Cliente(dni));
                    } catch (ClienteDniVacioException | ClienteDniInvalidoException e) {}
                    
                    controllerServer.recibeTurnoEnRespaldo(turno);
                    break;
                case IManejaServidores.HBOUT:
                    out.writeUTF(IManejaServidores.HBIN);
                    // TODO : Informarle al admin 
                    break;
            }
        } catch (SocketException e) {
            this.cantErrores += 1;
            controllerServer.avisarAdmin("Fallo en Server Principal.", AdminComunicaServerP.EVENTO_RESPALDO);
            if (cantErrores == 2){
                System.out.println("\n\n2 ERRORES\n\n");
                controllerServer.avisarAdmin("Cambiando estado a Server Principal.", AdminComunicaServerP.EVENTO_RESPALDO);
                controllerServer.cambiaEstadoServer();
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("\n\nNUNCA ENTRO AL SOCKETEXCEPTION\n\n");
            controllerServer.cambiaEstadoServer();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void comunicaGestor(GestorID gestorID) {
    }

    @Override
    public void comunicaTurno(Turno turno, String tipo) {
    }

    @Override
    public void comunicaListaTurnos(ListaTurnos turnos, String tipo) {
    }

    public ManejadorEventListener getControllerServer() {
        return controllerServer;
    }

    @Override
    public void actualizar() {
    }

}
