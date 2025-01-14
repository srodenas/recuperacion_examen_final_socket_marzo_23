package server.domain.usecase;

import java.io.PrintWriter;

import server.domain.interfaces.GenericRepository;
import server.domain.interfaces.RestInterface;
import server.domain.model.User;
import server.infraestructure.server.UserDataThread;

/*
 * VERSIÓN DE Santiago Rodenas Herráiz, para PSP 22-23
 * SIMULAMOS EL CASO DE USO QUE SE OCUPA DEL LOGUEO
 */
public class LoginUseCase implements RestInterface{
    private GenericRepository repository;
   
    /*
     *  @param pw (flujo salida), args (argumentos del comando), context (hilo que atiende al cliente)
     *  @return boolean true (correcto), false(no correcto)
     */

     public LoginUseCase (GenericRepository repository){
        this.repository = repository;
     }

     public void responseHttp(String response, PrintWriter pw){
        pw.println(response);
        pw.flush();
    }


     
    @Override
    public boolean execute(PrintWriter pw, String[] args,   Thread context) {

        if (args.length < 2){
            responseHttp("Debes pasar el email y passw", pw);
            return false;
        }


        User user = (User)repository
                .findByEmailAndPassw(args[0], args[1]);  //Buscamos por email

        
        if(user == null){
            responseHttp("Usuario no encontrado", pw);
            return false;
        }
        else{
            responseHttp("Usuario logueado correctamente ", pw);
            ((UserDataThread)context).setLogged(true);
            return true;
        }
               
        
    }
    
}
