package server.domain.usecase;

import java.io.PrintWriter;

import server.domain.interfaces.GenericRepository;
import server.domain.interfaces.RestInterface;
import server.infraestructure.server.UserDataThread;

/*
 * VERSIÓN DE Santiago Rodenas Herráiz, para PSP 22-23
 * SIMULAMOS EL CASO DE USO QUE SIMULA EL DESLOGUEO
 */
public class LogoutUseCase implements RestInterface{
    private GenericRepository repository;

    public LogoutUseCase (GenericRepository repository){
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
        
        if (!((UserDataThread)context).isLogged()){
            responseHttp("Acción no permitidq. Debes estar registrado!!", pw);
            return false;
        }


        //modificamos el contexto con el login a falso.
        ((UserDataThread)context).setLogged(false);
        ((UserDataThread)context).setExit();
        return true;
    }
    
}
