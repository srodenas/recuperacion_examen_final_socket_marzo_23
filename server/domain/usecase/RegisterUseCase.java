package server.domain.usecase;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import server.domain.interfaces.GenericRepositoryInterface;
import server.domain.interfaces.RestInterface;
import server.domain.model.User;
import server.infraestructure.server.UserDataThread;

/*
 * VERSIÓN DE Santiago Rodenas Herráiz, para PSP 22-23
 * SIMULAMOS EL CASO DE USO DEL REGISTRO
 */
public class RegisterUseCase implements RestInterface{
    private GenericRepositoryInterface repository;

    public RegisterUseCase (GenericRepositoryInterface repository){
        this.repository = repository;
    }

    public void responseHttp(String response, PrintWriter pw){
        pw.println(response);
        pw.flush();
    }



    /*
     * Registra los datos recibidos como parámetro.
     */
    /*
     *  @param pw (flujo salida), args (argumentos del comando), context (hilo que atiende al cliente)
     *  @return boolean true (correcto), false(no correcto)
     */

    @Override
    public boolean execute(PrintWriter pw, String[] args, Thread context) {
        /*
        Deberá comprar si existe ese usuario y en caso negativo, insertarlo.
        Debe comprobar que el número de argumentos sea al menos:
        nombre, email, passwd
        */

        if (args.length < 3){
            responseHttp("Debes pasar el nombre, email y passw", pw);
            return false;
        }

        /*
         * Verificamos si el usuario ya esta registrado comparando su email.
         */
        User u = (User)repository
                .findByEmail(args[1]);  //Buscamos por email

        if (u != null){
            responseHttp("Ese usuario ya está registrado ", pw);
            return false;
        }

        repository.add(new User(args));  //registramos el usuario   

        /*
         * Debemos de añadir su fichero con sus datos. 
         */
        try{


            final String  path="22_23/recuperacion_examen_final_socket_marzo_23/files/";  
            File file = new File(path + args[0] + ".dat");
            final String absolutePathFile = file.getAbsolutePath();
            
            FileWriter userFile = new FileWriter(absolutePathFile); 
            PrintWriter pwf = new PrintWriter(userFile);
            String info = "Nombre: " +
                args[0] + ", Email: " +
                args[1] + ", Passwd: " +
                args[2];

            pwf.println("Datos del usuario " + info);  //escribimos la información en fichero
            pwf.close();
        }catch (Exception e){
            e.printStackTrace();
            responseHttp("Error al crear fichero del usuario en registro", pw);
            return false;
        }

        responseHttp("Usuario registrado correctamente...", pw);
        ((UserDataThread)context).setLogged(true); //No tengo porqué loguearme después
      
        return true;
        
    }
    
}
