package server.domain.usecase;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import server.domain.interfaces.GenericRepository;
import server.domain.interfaces.RestInterface;
import server.infraestructure.server.UserDataThread;
/*
 * VERSIÓN DE Santiago Rodenas Herráiz, para PSP 22-23
 * 
 * Esta clase, crea un proceso que devolverá el hash de un fichero
 * previamente existente.
 */

 //SIMULAMOS EL CASO DE USO QUE IMPLEMENTA DEVOLVER EL HASH DE UN FICHERO.

public class GetHashUseCase implements RestInterface{
    private GenericRepository repository;

    public GetHashUseCase (GenericRepository repository){
        this.repository = repository;
     }

     public void responseHttp(String response, PrintWriter pw){
        pw.println(response);
        pw.flush();
    }


    /*
     *  @param pw (flujo salida), args (argumentos del comando), context (hilo que atiende al cliente)
     *  @return boolean true (correcto), false(no correcto)
     */
    @Override
    public boolean execute(PrintWriter pw, String[] args,  Thread context) {

        // CertUtil -hashfile file.ext MD5
        if (args.length < 1){
            responseHttp("Debes pasar el nombre del usuario", pw);
            return false;
        }

        /*
         * Este servicio, sólo se puede invocar si el usuario está logueado
         * Esto lo controlamos mediante el contexto.
         */
        if (!((UserDataThread)context).isLogged()){
            responseHttp("Acción no permitidq. Debes estar registrado!!", pw);
            return false;
        }

        final String path="22_23/recuperacion_examen_final_socket_marzo_23/files/";  
        File file = new File(path + args[0] + ".dat");
        final String absolutePathFile = file.getAbsolutePath();

        /*
         * Si el fichero existe y no es un Directorio, ejecuta el comando
         * que devuelve el hash de ese fichero.
         */
        if (file.exists() && !file.isDirectory()){
            final String [] cmd = {"CertUtil", "-hashfile", absolutePathFile, "MD5"};
           
            String  msg = "";
            ProcessBuilder pb = new ProcessBuilder(cmd);
    
            try{
                Process p = pb.start();
                
                try{
                    int codRet = p.waitFor();
                    InputStream is = p.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String lineReciber = "";
                    while ( (lineReciber = br.readLine()) != null){
                        msg+=lineReciber;
                    }
                } catch(InterruptedException ex) {
                    responseHttp("Error inesperado. Finalización interrumpida", pw);
                    return false;
                }
            }
           
            catch (IOException e){
                e.printStackTrace();
                responseHttp("Error de E/S.", pw);
                return false;
            }
    
            String [] hash = msg.split(":");
            String [] hash1 = hash[2].split("CertUtil");
            responseHttp("Codigo Hash MD5 " + hash1[0], pw);
            return true;
        }
        responseHttp("No se ha encontrado el fichero", pw);
        return false;
       
    }

   
    
}
