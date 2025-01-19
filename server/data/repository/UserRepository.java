package server.data.repository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import server.domain.interfaces.GenericRepositoryInterface;
import server.domain.model.User;

/*
 * ES LA IMPLEMENTACIÓN DEL REPOSITORIO SOBRE UN TIPO DE DATOS EN PARTICULAR.
 * NUESTRA CLASE DEBE IMPLEMENTAR LOS MÉTODOS PROPUESTOS EN EL CONTRATO DE LA INTERFAZ.
 */

public class UserRepository<T> implements GenericRepositoryInterface<T>{

    // Necesitamos una clave para el User
    private AtomicInteger automaticId;

    //Aquí tenemos nuestra lista de Users.
    private List<User> userList;


    /*
     * Constructor que creará nuestro objeto AtomicInteger, para que nos dé una clave diferente.
     * Nuestra lista será un ArrayList de Usuarios.
     */
    public UserRepository(){
        automaticId = new AtomicInteger(0);
        userList = new ArrayList<User>();
     //   initialize();
    }




    /*
     * Implementamos nuestra operación de insertar un usuario. Al trabajar con genéricos, debemos
     * de castear a usuarios, siempre y cuando sea una instancia del mismo. Una mejora, sería tratar
     * una excepción en caso de que no fuera un User.
     * Deberá estar sincronizado, para asegurar la EXCLUSIÓN MÚTUA al recurso compartido.
     */
    @Override
    synchronized public void add(T u) {
        // TODO Auto-generated method stub
       if (u instanceof User){
        User user = (User) u;
        user.setId(automaticId.incrementAndGet());
        userList.add(user);
       }    
    }




    /*
     * A partir de un entero, devolvemos el User.
     * Es importante darse cuenta, que filtraremos por id, encontrando
     * la primera instancia que coincida y en caso de que no se encuentre, 
     * devolver un null. Utilizamos una expresión lambda. El método debe
     * devolver un T, por tanto lo generalizamos a T.
     * Aseguramos la EXCLUSIÓN MÚTUA.
     */

    @Override
    synchronized  public T get(int i) {
        // TODO Auto-generated method stub
        User user = userList
            .stream()
            .filter(u -> u.getId()==i)
            .findFirst()
            .orElse(null);
        return (T)user;
    }




    /*
     * Devolvemos todos los usuarios que tengamos.
     * Aseguramos la EXCLUSIÓN MÚTUA
     */
    @Override
    synchronized  public List<T> getAll() {
        // TODO Auto-generated method stub
       return (List<T>) userList;
    }





    /*
     * Devolvemos un User, donde coincida su email.
     * Utilizamos expresión lambda.
     * Aseguramos la EXCLUSIÓN MÚTUA.
     */
    @Override
    synchronized  public T findByEmail(String email) {
        // TODO Auto-generated method stub
        User user = userList
            .stream()
            .filter(u -> u.getEmail().equals(email))
            .findFirst()
            .orElse(null);
        
        return (T) user;
    }





    /*
     * Lo mismo que el anterio método, sólo que añadimos una operación
     * and en la expresión lambda
     */
    @Override
    synchronized  public T findByEmailAndPassw(String email, String pass) {
        // TODO Auto-generated method stub
        User user = userList.stream()
        .filter(
            (u) -> u.getEmail().equals(email) && 
                    u.getPasswd().equals(pass))
        .findFirst()
        .orElse(null);

        return (T) user;
    }

    
    


    /*
     * Para pruebas
     */

   /*  private void initialize(){
        userList.add(new User("Santi", "srodenashe@gmail.com", "santi"));
        userList.add(new User("Sonia", "smenadel@gmail.com", "sonia"));
        userList.add(new User("David", "drodehe@gmail.com", "david"));
    }
*/
    
    
}
