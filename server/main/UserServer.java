package server.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import server.application.Routing;
import server.infraestructure.server.UserDataThread;

/*
 * VERSIÓN DE Santiago Rodenas Herráiz, para PSP 22-23
 * 
 * Creará un hilo por petición de cliente.
 * Existirá un único objeto que compartirán todos los hilos, que será el administrador
 * de operaciones Rest. Dicho objeto, creará el recurso compartido que son la lista de
 * Usuarios y sus operaciones. 
 */


public class UserServer {
    //Sólo tendremos un único objeto admnistrador de operaciones Rest. 
    private static Routing routing;  //Gestor de operaciones.
    int port;
    public static void main(String[] args) {
        int port = -1; 
        //Recurso Compartido de los hilos. Trabaja directamente con la lista de Usuarios.
      //  final GenericRepository userManager; 


        //Probamos que se le pase un parámetro puerto.
        if (args.length == 0) {
            System.out.println("Debes pasar el puerto a escuchar");
            System.exit(1);  //Cerramos conexión con errores.
        }


        //Comprobamos que el puerto sea un entero.
        try{
            port = Integer.parseInt(args[0]);
        }catch(NumberFormatException e){
            System.out.println("Error en el tipo puerto");
            System.exit(2);  //Cerramos conexión con errores.
        }

        //Gestor operaciones Rest. Este objeto es el principal y creará el recurso compartido.
        routing = new Routing();  

        System.out.println("Servidor a la escucha del puerto  " + port);
        System.out.println("Esperando conexión ......");
    
        //Creamos nuestro socket de servidor, para servir a clientes.
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {

                //Aceptamos conexión con cliente
                Socket socketClient = serverSocket.accept();
                System.out.printf("Establecida conexión con %s:%d%n",
                    socketClient.getInetAddress(),
                    socketClient.getPort()
                );
    
                //Creamos el hilo pasándole el administrador de servicios Rest
                new UserDataThread(socketClient,  routing).start();
            }
        } catch (IOException e) {
             e.printStackTrace();
        }
    }
 
}
