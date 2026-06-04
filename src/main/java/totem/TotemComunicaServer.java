package totem;

import java.io.IOException;

import shared.conexion_server.ComunicaServer;

public class TotemComunicaServer extends ComunicaServer {
    TotemEventListener escuchadorDeEventos;

    public void setEscuchadorDeEventos(TotemEventListener escuchadorDeEventos) {
        this.escuchadorDeEventos = escuchadorDeEventos;
    }
    // El totem envia el DNI al socket de comunicacion con el server que este conectado y si ya estaba en el sistema retorna false.
    public boolean enviarDNI(long dni) {
        boolean validacion = false;
        try {
            out.writeUTF(String.valueOf(dni)); // TODO : encriptar
            // TODO : Informar al ADMIN (server-side)
            validacion = Boolean.parseBoolean(in.readUTF());
        } catch (IOException e) {
            escuchadorDeEventos.mensajeError("Error de protocolo de conexion"); // TODO : Manejar retry???
            e.printStackTrace();
        }
        return validacion;
    }
}
