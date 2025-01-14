package server.domain.interfaces;
import java.io.PrintWriter;

//SIMULAMOS LA OPERACIÓN REST A REALIZAR

/*
 * VERSIÓN DE Santiago Rodenas Herráiz, para PSP 22-23
 * 
 * La idea es tener una Interfaz, que defina un método execute con los parámetros siguiente:
 * 
 * PrintWriter pw (Flujo de salida para el intercambio de datos) con el cliente.
 * String[] args (comando que reciba del cliente). 
 * ObjectManagerInterface  (objeto que implemente las operaciones sobre la lista de genericos). 
 * Será nuestro recurso compartido.
 * Thread context (Hilo que atiende al cliente).
 * 
 * CADA OPERACIÓN REST, DEBERÁ IMPLEMENTAR DE ESTA INTERFAZ.
 */


public interface RestInterface {
    /*
     *  @param pw (flujo salida), args (argumentos del comando), context (hilo que atiende al cliente)
     *  @return boolean true (correcto), false(no correcto)
     * 
     */
    public boolean execute(PrintWriter pw, String [] args, Thread context);
}
