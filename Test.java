import java.io.PrintWriter;
import java.util.List;

import server.application.Routing;
import server.data.repository.UserRepository;
import server.domain.interfaces.GenericRepository;
import server.domain.model.User;


public class Test {
    static GenericRepository repository;
    static Routing operationManager;
    static final PrintWriter writer = new PrintWriter(System.out);
   
    public static void main(String[] args) {
        repository = new UserRepository();
        userCaseManager = new Routing(repository); 
        insertTest();
        printAllTest();     //imprime todos los usuarios.
        registerTest();     //registra un nuevo usuario
        registerTest();  //registra otra vez para que de error.
        loginTest();    //loguea con un usuario que existe
        loginTestFail();  //loguea con un usuario que no existe.
        printAllTest();     //imprime todos los usuarios.
        userDataTest();     //imprime los datos del usuario solicitado por email
        userDataTestFail(); //imprime con fallo los datos de un usuario que no encuentra.
        listTest();         //muestra la lista de usuarios
        hashMD5Test();      //prueba el hash
        hashMD5TestFail();      //prueba el hash de uno que no existe
    }

    private static void insertTest(){
        userCaseManager.add(new User("Santi", "srodenashe@gmail.com", "santi"));
        userCaseManager.add(new User("Sonia", "smenadel@gmail.com", "sonia"));
        userCaseManager.add(new User("David", "drodehe@gmail.com", "david"));

    }

    private static void printAllTest(){
        List<User> list = userManager.getAll();
        list.stream()
            .forEach((p)-> System.out.println(p.toString()));
        
    }

    private static void registerTest(){
        final String cmd = "reg Guille guille@gmail.com guille";
        operationManager.execute(writer, cmd);
       
    }

    private static void loginTest(){
        final String cmd = "log guille@gmail.com guille";
        operationManager.execute(writer, cmd);
    }

    private static void loginTestFail(){
        final String cmd = "log noexiste@gmail.com noexiste";
        operationManager.execute(writer, cmd);
    }

    private static void userDataTest(){
        final String cmd = "datu guille@gmail.com";
        operationManager.execute(writer, cmd);
       
    }
    private static void userDataTestFail(){
        final String cmd = "datu noexiste@gmail.com";
        operationManager.execute(writer, cmd);
       
    }
    private static void listTest(){
        final String cmd = "list";
        operationManager.execute(writer, cmd);
       
    }

    private static void hashMD5Test(){
        final String cmd = "hash5 Guille";
        operationManager.execute(writer, cmd);
       
    }
    private static void hashMD5TestFail(){
        final String cmd = "hash5 noexiste";
        operationManager.execute(writer, cmd);
       
    }
}
