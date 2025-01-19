# Solución Examen Ejercicio Usuario Hash
## PSP 24-25

Este repositorio contiene la solución al examen de la asignatura PSP (Programación de Servicios y Proyectos) del curso 2024-2025. El ejercicio consiste en implementar un sistema que gestione usuarios, permita registrar, iniciar y cerrar sesión, obtener datos de usuario y gestionar el hash de contraseñas.

La solución está basada en una arquitectura orientada a objetos, donde se utilizan interfaces y clases concretas para manejar las operaciones relacionadas con los usuarios y sus datos.

## Descripción del Sistema

### Estructura General

El sistema implementa una arquitectura en capas, donde la capa de presentación (el servidor que maneja las solicitudes HTTP) interactúa con los casos de uso de los usuarios. Cada uno de estos casos de uso se implementa como una clase que implementa la interfaz `RestInterface`.

Las operaciones que se pueden realizar sobre los usuarios incluyen:

- **Login**: Permite iniciar sesión verificando las credenciales del usuario.
- **Logout**: Permite cerrar la sesión de un usuario.
- **Register**: Permite registrar un nuevo usuario.
- **UserData**: Muestra los datos de un usuario.
- **UsersList**: Muestra una lista de todos los usuarios registrados.
- **GetHash**: Permite obtener el hash de la contraseña de un usuario.

El sistema está diseñado para ser extensible, por lo que puedes añadir nuevos casos de uso o cambiar el repositorio de datos sin afectar a otras partes del sistema.

### Componentes Principales

1. **`RestInterface`**: Define el contrato que deben seguir todas las clases que implementan los casos de uso. Cada clase que implementa esta interfaz debe definir el método `execute`, que recibe los parámetros necesarios para ejecutar el caso de uso.
2. **`GenericRepositoryInterface`**: Define las operaciones básicas de acceso a datos, como agregar un usuario, obtenerlo por ID, obtener todos los usuarios, y buscar por email o email y contraseña.
3. **`UserRepository`**: Implementación concreta de `GenericRepositoryInterface`, especializada en gestionar objetos `User`.
4. **`User`**: Representa un usuario, con atributos como `id`, `name`, `email`, y `passwd`.
5. **`Routing`**: La clase encargada de gestionar los endpoints (casos de uso) y dirigir las solicitudes a los servicios correspondientes.
6. **`UserDataThread`**: Representa un hilo que procesa las solicitudes de los usuarios.
7. **`UserServer`**: La clase principal que crea tanto el objeto `Routing` como los hilos `UserDataThread` y maneja la ejecución del servidor.

### Relaciones entre las Clases

- **Composición de `UserServer` hacia `Routing`**: Un `UserServer` contiene un `Routing` para gestionar los servicios de usuario.
- **Composición de `UserServer` hacia `UserDataThread`**: Un `UserServer` crea y contiene múltiples instancias de `UserDataThread`.
- **Dependencia de `UserDataThread` hacia `Routing`**: Cada hilo (`UserDataThread`) depende de `Routing` para ejecutar los servicios correspondientes.
- **Composición de `Routing` hacia `GenericRepositoryInterface` y `RestInterface`**: `Routing` utiliza tanto el repositorio genérico como los casos de uso implementados por las clases que implementan `RestInterface`.

### Funcionalidad

El servidor funciona recibiendo solicitudes de los usuarios, que se gestionan a través de hilos. Cada hilo (instancia de `UserDataThread`) recibe la solicitud, la procesa y dirige la ejecución al servicio correspondiente, invocando el método `execute` del caso de uso adecuado.

El flujo general es el siguiente:

1. El **servidor** recibe una solicitud del cliente.
2. El **servidor** crea un hilo (`UserDataThread`) para procesar la solicitud.
3. El **hilo** procesa la solicitud, identifica el verbo (comando) y llama al caso de uso correspondiente.
4. El **caso de uso** interactúa con el repositorio de datos para obtener o modificar información.
5. El **servidor** envía una respuesta al cliente.

### Ejecución

Hay que crear el fichero launch.json para crear los agumentos tanto en el cliente como en el servidor. Pongo el mio de ejemplo
El fichero se debe crear en la ejecución y depuración.

1. En el cliente, añadimos "args" : ["localhost", "3000"]
2. En el servidor, añadimos "args" : ["3000"]

## Diagrama de Clases UML

A continuación se presenta el diagrama de clases que refleja la estructura de las clases y sus relaciones en la solución:

```plantuml
@startuml
interface RestInterface {
    +execute(pw: PrintWriter, args: String[], context: Thread): boolean
}

class GetHashUseCase {
    -repository: GenericRepositoryInterface
    +execute(pw: PrintWriter, args: String[], context: Thread): boolean
}

class LoginUseCase {
    -repository: GenericRepositoryInterface
    +execute(pw: PrintWriter, args: String[], context: Thread): boolean
}

class LogoutUseCase {
    -repository: GenericRepositoryInterface
    +execute(pw: PrintWriter, args: String[], context: Thread): boolean
}

class RegisterUseCase {
    -repository: GenericRepositoryInterface
    +execute(pw: PrintWriter, args: String[], context: Thread): boolean
}

class UserDataUseCase {
    -repository: GenericRepositoryInterface
    +execute(pw: PrintWriter, args: String[], context: Thread): boolean
}

class UsersListUseCase {
    -repository: GenericRepositoryInterface
    +execute(pw: PrintWriter, args: String[], context: Thread): boolean
}

interface GenericRepositoryInterface {
    +add(o: T): void
    +get(i: int): T
    +getAll(): List<T>
    +findByEmail(email: String): T
    +findByEmailAndPassw(email: String, pass: String): T
}

class UserRepository {
    -repository: GenericRepositoryInterface
    +add(o: User): void
    +get(i: int): User
    +getAll(): List<User>
    +findByEmail(email: String): User
    +findByEmailAndPassw(email: String, pass: String): User
}

class User {
    -id: int
    -name: String
    -email: String
    -passwd: String
    +getId(): int
    +getName(): String
    +getEmail(): String
    +getPasswd(): String
}

class Routing {
    -magerEndPoints: HashMap<String, RestInterface>
    -repository: GenericRepositoryInterface
    +responseHttp(response: String, pw: PrintWriter): void
    +execute(pw: PrintWriter, body: String, context: UserDataThread): boolean
}

class UserDataThread {
    -routing: Routing
    +run(): void
}

class UserServer {
    -routing: Routing
    -userDataThreads: List<UserDataThread>
    +start(): void
}

RestInterface <|-- GetHashUseCase
RestInterface <|-- LoginUseCase
RestInterface <|-- LogoutUseCase
RestInterface <|-- RegisterUseCase
RestInterface <|-- UserDataUseCase
RestInterface <|-- UsersListUseCase

RestInterface <.. GenericRepositoryInterface

GenericRepositoryInterface <|-- UserRepository

UserRepository *-- "0..*" User : contains

Routing *-- "1..*" RestInterface : uses
Routing *-- "1..*" GenericRepositoryInterface : uses

UserDataThread *-- Routing : contains

RestInterface ..> UserDataThread : dependency

UserServer *-- Routing : creates
UserServer *-- "0..*" UserDataThread : creates
UserDataThread ..> Routing : dependency

@enduml
