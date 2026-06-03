package server.manejadores;

import server.ListaTurnos;
import server.id.GestorID;
import shared.turno.Turno;

public class ManejaServerPrincipal extends ManejadorDeNodos implements IManejaServidores{
    private Object mutex = new Object(); // Auxiliar para el manejo de zonas criticas con los sockets.

    @Override
    public void comunicacion() {
        synchronized (mutex){
            //server.hearthbeat(in, out);
        }
    }

    @Override
    public void comunicaGestor(GestorID gestorID){
        synchronized (mutex){
        //server.comunicaGestor(in, out); //Si algo no funciona, pasarle el gestorID por las dudas, aunque el server ya lo tiene
        }
    }

    @Override
    public void comunicaTurnoEspera(Turno turno) {
        synchronized (mutex){
        // TODO
        }
    }

    @Override
    public void comunicaListaTurnosEspera(ListaTurnos turnos) {
        synchronized (mutex){
        // TODO
        }
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
