package server.infraestructure.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import server.application.Routing;

/*
 * VERSIÓN DE Santiago Rodenas Herráiz, para PSP para PSP 24-25
 * 
 * El hilo se encargará de invocar a los servicios que pida el cliente.
 * Al crearse el hilo, se le pasa el recurso compartido a partir de Administrador
 * de operaciones Rest. Recordamos que dicho objeto, TIENE ACCESO AL RECURSO COMPARTIDO UserManager.
 */
public class UserDataThread extends Thread{

    private Socket socket;  //socker que conecta con el cliente.
    private PrintWriter pw; //Para salida de datos hacia el cliente
    private Scanner sc;     //Para lectura de datos desde el cliente
    private Routing routing;
    private boolean logged  = false;  //El hilo no está logueado.
    private boolean exit = false;  //Para el estado de cierre de conexión

    public UserDataThread (Socket socket, Routing routing){
        this.socket = socket;
        this.routing = routing;  //gestor de operaciones Rest
    }

    @Override
    public void run() {
        try{
           
            /*
             * Creamos los flujos de E/S para conectar con Cliente.
             */
            pw = new PrintWriter(this.socket.getOutputStream());
            sc = new Scanner(this.socket.getInputStream());
            System.out.println("Esperando respuesta cliente");

            while (this.sc.hasNextLine()) {
              String line = this.sc.nextLine();
              InetAddress address = this.socket.getInetAddress();
              System.out.printf("Recibida conexión desde %s:%d: %s%n", address.getHostAddress(), socket.getPort(), line);
      
              //Ejecutamos el comando recibido del cliente.  
              this.routing.execute(pw, line, this);
              if (isExit()){
                socket.close();
                pw.close();
                sc.close();
                System.exit(0);  //cerramos conexión sin errores.
              }
             
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("Error inesperado de E/S por el servidor");
            }
        }
         
    }

    /*
     * Método que setea cuando se loguea el usuario.
     */
    public boolean isLogged() {
        return this.logged;
    }

    /*
     * Método que devuelve si un usuario está logueado o no.
     */
    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public boolean isExit(){
        return this.exit;
    }

    public void setExit(){
        this.exit = true;
    }
}