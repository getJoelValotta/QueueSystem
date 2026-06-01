package shared;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class VistasUtils {

    private VistasUtils() {}  // no se instancia

    public static void ejecutarNoBloqueante(Runnable tarea) { // Estoy en EDT y necesito codigo externo (no bloquear el EDT)
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                tarea.run();
                return null;
            }
        }.execute();
    }

    public static void enEDT(Runnable tarea) {          //Estoy en el EDT pero encolo las solicitudes para sincronizar.
        if (SwingUtilities.isEventDispatchThread()) {
            tarea.run();
        } else {
            SwingUtilities.invokeLater(tarea);
        }
    }
}

//EJEMPLO DE UTILIZACION Y EXPLICACION:
/* 

        SwingUtilities.invokeLater(() -> vistaConexion.appendLogError(mensaje));

        VistasUtils.ejecutarNoBloqueante(() ->
            comunicaServer.conectaServidor(vistaConexion.getIP(), Integer.parseInt(vistaConexion.getPuerto()), ComunicaServer.TOTEM)
        );

        ¿Por que se habla de una ejecucion no bloqueante?
        Un proceso se diverge en diversos hilos de ejecucion. En este caso, del main
        se dibuja una ventana que deriva en un hilo llamado EVENT DISPATCHER THREAD, el
        cual se encarga de dibujar y hacer todo relacionado a las ventanas (incluyendo 
        action listeners).
        El problema esta en el que si un hilo que no es el EDT ejecuta sentencias de codigo
        del EDT, por condiciones de carrera y seccion critica este produce indeterminaciones,
        generando un problema de sincronizacion (se freezean ventanas).
        El principio para no incurrir en estos bloqueos es: Si algo de un hilo que no es
        el EDT ejecuta sentencias de codigos del EDT, debe encolar las solicitudes al EDT para que lo
        resuelva sin paralelismo. Si el codigo del EDT invoca funciones de otro hilo no hay problema
        a no ser que haya esperas por I/O (sockets o lecturas/escrituras)
        Basicamente si algo va a trabajar en paralelo con el EDT, encolarlo en un invokeLater.

        ¿Que es esa expresion dentro de parentesis rara?
        Se llama expresion lambda.
*/
