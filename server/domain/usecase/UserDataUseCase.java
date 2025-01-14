package server.domain.usecase;

import java.io.PrintWriter;

import server.domain.interfaces.GenericRepository;
import server.domain.interfaces.RestInterface;
import server.domain.model.User;
import server.infraestructure.server.UserDataThread;

/*
 * VERSIÓN DE Santiago Rodenas Herráiz, para PSP 22-23
 * SIMULAMOS EL CASO DE USO QUE IMPRIME LOS DATOS DE UN USUARIO
 */
public class UserDataUseCase implements RestInterface{
    private GenericRepository repository;

    public UserDataUseCase (GenericRepository repository){
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

        if (args.length < 1){
            responseHttp("Debes pasar el email", pw);
            return false;
        }
/*
 * Sólo se puede invocar si está registrado.
 */
        if (!((UserDataThread)context).isLogged()){
            responseHttp("Acción no permitidq. Debes estar registrado!!", pw);
            return false;
        }

        User u = (User)repository
        .findByEmail(args[0]);  //Buscamos por email

        if (u == null){
                responseHttp("Ese usuario no exixte ", pw);
        }
        else
            responseHttp("Datos del usuario: " + u.toString(), pw);
        return true;
    }
    
}
