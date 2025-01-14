package server.application;

import java.io.PrintWriter;
import java.util.HashMap;

import server.data.repository.UserRepository;
import server.domain.interfaces.GenericRepository;
import server.domain.interfaces.RestInterface;
import server.domain.usecase.GetHashUseCase;
import server.domain.usecase.LoginUseCase;
import server.domain.usecase.LogoutUseCase;
import server.domain.usecase.RegisterUseCase;
import server.domain.usecase.UserDataUseCase;
import server.domain.usecase.UsersListUseCase;
import server.infraestructure.server.UserDataThread;


/*
 *  VERSIÓN DE Santiago Rodenas Herráiz, para PSP 22-23
 * 
 * PODEMOS DECIR QUE ES EL ROUTING 
 * 'Esta clase es la gestora de todas las operaciones a servir. 
 * 
 * Se encargará de llamar a las diferentes operaciones mediante objetos que implementen el
 * servicio a emular.
 * 
 * Los objetos de operaciones Rest, los contiene dentro de un HashMap, cuya clave será el Verbo.
 * 
 * Esta clase contiene un único método llamado execute, que se encargará de seleccionar el objeto
 * que emulará el servicio REST mediante su caso de uso, dependiendo del verbo. Para llevarlo a cabo, necesita tener acceso al recurso compartido,
 * y el contexto del hilo que invoca a este objeto.
 */

public class Routing {
    
    private final HashMap<String, RestInterface> maganerEndPoints;  //Simulamos el gestor de endpoint que el servidor puede tratar. 
    private final GenericRepository repository; //aquí tenemos el repositorio. Todos los métodos sobre el acceso a datos.



/*
 * Crea cada uno de los objetos que implementen los servicios.
 * Por cada servicio, un objeto.
 */
    public Routing(){
        this.repository = new UserRepository<>();  //creamos el repositorio de usuarios.
        maganerEndPoints = new HashMap<>(); //Creamos un hashMap para insertar "verbo" => Caso de Uso que implementa el servicio.
        maganerEndPoints.put("reg" , new RegisterUseCase(this.repository));
        maganerEndPoints.put("log", new LoginUseCase(this.repository));
        maganerEndPoints.put("datu", new UserDataUseCase(this.repository));
        maganerEndPoints.put("list", new UsersListUseCase(this.repository));
        maganerEndPoints.put("hash5", new GetHashUseCase(this.repository));
        maganerEndPoints.put("fin", new LogoutUseCase(this.repository));
       
        
      
    }

    public void responseHttp(String response, PrintWriter pw){
        pw.println(response);
        pw.flush();
    }



    /*
     * Dependiendo de la línea recibida por el Cliente, deberá sacar el verbo
     * y los argumentos en un array. Despues llamar a la operación solicitada.
     * PrintWriter pw (el flujo de salida)
     * String body (la línea recibida con el verbo y los argumentos)
     * UserDataThread (Es el contexto o el hilo que invoca a este this)
     */
    public boolean execute(PrintWriter pw, String body, UserDataThread context){
        String [] args = body.split(" ");  //separamos en líneas lo recibido como dato.
        
        try{
            String verb = args[0]; //extraemos el verbo del servicio.
            RestInterface endPoint = maganerEndPoints.get(verb);  //Seleccionamos el Servicio. Cada servicio será un caso de uso.

            /*
             * Comprobamos si existe un servicio invocado, en caso contrario informamos.
             */
            if (endPoint == null){
                responseHttp("Error, debes pasar un comando válido", pw);
                return false;
            }

            /*
             * Separamos tanto el verbo (servicio), como sus argumentos.
             */
            String [] operationsArgs = new String[args.length - 1];  //copiamos sólo argumentos.
            //array origen, posición de inicio origen, array destino, posición de inicio destino, cantidad de valores.
            System.arraycopy(args, 1,  operationsArgs, 0, args.length - 1);

            /*
             * Invocamos al objeto cuyo verbo corresponde con el servicio, pasándole tanto el flujo de salida,
             * como los parámetros, el recurso compartido y el contexto de quien invoca el servicio.
             * Devolvemos true/false, dependiendo de si se ha obtenido un resultado OK.
             * Podríamos haberlo complicado algo más, con objetos de código 400 o de código 200.
             * 
             * Aplico polimorfismo.
             */
            return(endPoint.execute(pw, operationsArgs, context));


        }catch(ArrayIndexOutOfBoundsException e){
            responseHttp("Error, debes pasar la acción", pw);
            return false;
        }
       
    }

}
