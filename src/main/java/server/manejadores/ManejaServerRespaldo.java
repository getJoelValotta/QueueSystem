package server.manejadores;

import java.io.IOException;
import java.util.Iterator;

import admin.AdminComunicaServerP;
import server.ListaTurnos;
import server.id.GestorID;
import shared.turno.Turno;
public class ManejaServerRespaldo extends ManejadorDeNodos implements IManejaServidores, IControllerObserver{
    private Object mutex = new Object(); // Auxiliar para el manejo de zonas criticas de los in/out de los sockets.

    
    
    public ManejaServerRespaldo(ManejadorEventListener controllerServer, String id) {
        super(controllerServer, id);
        //TODO Auto-generated constructor stub
    }
    
    
    // Los metodos delegan acciones a los servidores debido a que depende si son de Estado Principal o Respaldo.

    @Override //Corre en el RUN del hilo 
    public void comunicacion() {
        synchronized (mutex){ //Es el enviaHearthbeat
            try {
                out.writeUTF(IManejaServidores.HBOUT);
                String respuesta = in.readUTF();
            } catch (IOException e) {
                try{
                    socket.close();
                    controllerServer.avisarAdmin("Server de Respaldo desconectado.", AdminComunicaServerP.MAL_PRINCIPAL);
                }catch(IOException e1){}
                
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(INTERVALO_HB_MS); // Se va a dormir 2 segundos
        } catch (InterruptedException e) {
            // TODO : Informar al admin (se cayo)
            e.printStackTrace();
        }
    }

    public void comunicaGestor(GestorID gestorID){
        String bufferContTotem = String.valueOf(gestorID.getContadorTotem());
        String bufferContPuesto = String.valueOf(gestorID.getContadorPuesto());
        String bufferContMonitor = String.valueOf(gestorID.getContadorMonitor());
        synchronized (mutex){
            try {
                out.writeUTF(IManejaServidores.GESTOR);
                out.writeUTF(bufferContTotem);
                out.writeUTF(bufferContPuesto);
                out.writeUTF(bufferContMonitor);
            } catch (IOException e) {
                try{
                    socket.close();
                    controllerServer.avisarAdmin("Server de Respaldo desconectado.", AdminComunicaServerP.MAL_PRINCIPAL);
                }catch(IOException e1){}
                e.printStackTrace();
            }

        }
    }

    @Override
    public void comunicaTurno(Turno turno, String tipoTurno) {
        String dni = String.valueOf(turno.getCliente().getDni());
        String idPuesto = turno.getIdPuesto();
        synchronized (mutex){
            try { //TODO : Aca falta discriminar por tipo de turno para enviar o no el IDPUESTO.
                out.writeUTF(tipoTurno);
                String dniEncriptado = controllerServer.encriptar(dni);
                out.writeUTF(dniEncriptado);
                if (!tipoTurno.equals(IManejaServidores.TURNO_ESPERA)){
                    out.writeUTF(idPuesto);
                    if (tipoTurno.equals(IManejaServidores.TURNO_ATENCION)){
                        out.writeUTF(String.valueOf(turno.getCantLlamados()));
                    } 
                }
            } catch (IOException e) {
                try{
                    socket.close();
                    controllerServer.avisarAdmin("Server de Respaldo desconectado.", AdminComunicaServerP.MAL_PRINCIPAL);
                }catch(IOException e1){}
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void comunicaListaTurnos(ListaTurnos turnos, String tipoTurno) {
        Iterator<Turno> it = turnos.devuelveIterator();
        while (it.hasNext()) {
            Turno turno = it.next();
            comunicaTurno(turno, tipoTurno);
        }
    }
    
    @Override
    public void actualizar() {
        // TODO Auto-generated method stub
        
    }

    // Aca tengo un problema: El controller inicia un hilo cuando se conecta el de respaldo a un principal en ambos servidores para que empiecen a mandarse hearthbeats, que es
    // lo que debe correr constantmente el hilo que se comunica server a server.
    // Por otro lado esta la comunicacion de redundancia pasiva: cuando un server envia un hearthbeat no puede tambien solicitar un envio de datos como por ejemplo la lista de espera
    // o el gestor de IDS unicos.
    // Se me ocurrio implementar un mutex, que no es nada mas que un synchronized (Objeto inutil){ manejo de out's e in's }. Ahora esto resuelve el problema de la comunicacion
    // para que no se pisen los hilos, pero no trae problemas a los hearthbeats? cada HB se dispara cada tiempo fijo, si hay un synchronized ese tiempo deberia tener 
    // un umbral de aceptacion para que uno no piense que se cayó cuando unicamente hubo delay, y deberia hacer retry's por si acaso.
    // Esto esta bien asi debido a que lo que se transmite no es costoso (apenas unos pocos bytes), porque si lo fuera el delay que causaria al hearthbeat penalizaria demasiado y 
    // deberia crearse un socket unico para el manejo de hearthbeat (Como servicios de streaming que envian megabytes o incluso mas).
    
}
