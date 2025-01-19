package server.domain.usecase;

import java.io.PrintWriter;
import java.util.List;

import server.domain.interfaces.GenericRepositoryInterface;
import server.domain.interfaces.RestInterface;
import server.domain.model.User;
import server.infraestructure.server.UserDataThread;

/*
 * VERSIÓN DE Santiago Rodenas Herráiz, para PSP 22-23
 * CASO DE USO QUE IMPLEMENTA LOS DATOS DE TODOS LOS USUARIOS
 */
public class UsersListUseCase implements RestInterface {
    private GenericRepositoryInterface repository;


    public UsersListUseCase (GenericRepositoryInterface repository){
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

        /*
         * Sólo se puede invocar a este servicio si está logueado.
         */
        if (!((UserDataThread)context).isLogged()){
            responseHttp("Acción no permitidq. Debes estar registrado!!", pw);
            return false;
        }

        /*
         * Recuperamos todos los usuarios y recuperamos todos los datos
         * de una forma sencilla.
         */
        List <User> list = repository.getAll();

        String msg="";
        for (User user : list )
            msg+="Id: " + user.getId()
                + " Nombre: " + user.getName()
                + " Email: " + user.getEmail()
                + " Passw: " + user.getPasswd()
                + "\n";
       
        
        responseHttp(msg, pw);
        return true;
    }
    
}
