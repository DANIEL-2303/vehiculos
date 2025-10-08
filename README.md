# vehiculos


Descripción general


El proyecto Garaje es una aplicación Java EE que permite administrar los vehículos registrados en un garaje.
Implementa una arquitectura multicapa basada en los principios de MVC (Modelo–Vista–Controlador) y capas de negocio (Facade/DAO).

Las principales operaciones incluyen:

* Registrar vehículos.
* Consultar listado de vehículos.
* Actualizar información existente.
* Eliminar registros con reglas de negocio predefinidas.

Toda la lógica de acceso a datos, validación y control se maneja mediante componentes Java EE (`@Stateless`, `Servlets`, `DataSource`).


Arquitectura del proyecto


El sistema está dividido en cuatro capas principales, organizadas en paquetes:

| Paquete                  | Rol                        | Descripción                                                                                      |
| -------------------------| -------------------------- | ------------------------------------------------------------------------------------------------ |
| com.garaje.model       | Modelo                     | Contiene las clases que representan las entidades de la base de datos. Ejemplo: `Vehiculo.java   |
| com.garaje.persistence | Persistencia (DAO)         | Encapsula las operaciones CRUD directas sobre la base de datos MySQL. Ejemplo: `VehiculoDAO.java |
| com.garaje.facade     | Lógica de negocio (Facade) | Aplica las reglas de negocio antes de interactuar con la capa DAO. Ejemplo: `VehiculoFacade.java |
| com.garaje.controller  | Controladores (Servlets)   | Gestiona las peticiones HTTP y redirige las respuestas a las vistas JSP. Ejemplo: `servlets.java |


Base de datos

Estructura de la tabla principal (MySQL):

sql
CREATE TABLE vehiculos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  placa VARCHAR(20) NOT NULL,
  marca VARCHAR(30) NOT NULL,
  modelo VARCHAR(30) NOT NULL,
  color VARCHAR(20),
  propietario VARCHAR(50) NOT NULL
);


Conexión configurada mediante recurso JNDI:
`jdbc/garageDB`



Reglas de negocio implementadas

Todas las validaciones se aplican dentro de la clase VehiculoFacade.java, antes de realizar operaciones en la base de datos:

1. Placas duplicadas:
   No se permite registrar dos vehículos con la misma placa.

2. Propietario:
   El campo propietario no puede estar vacío y debe tener al menos 5 caracteres.

3. Longitud mínima:
   Marca, modelo y placa deben tener mínimo 3 caracteres.

4. Colores permitidos:
   Solo se aceptan los colores: `Rojo`, `Blanco`, `Negro`, `Azul` o `Gris`.

5. Antigüedad del vehículo:
   No se aceptan vehículos con más de 20 años de antigüedad.
   (El campo modelo representa el año de fabricación.)

6. Validación contra SQL Injection:
   Se verifica que los campos no contengan caracteres o palabras peligrosas (`'`, `--`, `DROP`, `DELETE`, `INSERT`, `UPDATE`).

7. Notificación especial:
   Si la marca del vehículo es “Ferrari”, se imprime un mensaje de notificación simulada en la consola.

8. Actualización:
   Solo se permite actualizar un vehículo si realmente existe en la base de datos.

9. Eliminación protegida:
   No se permite eliminar vehículos cuyo propietario sea “Administrador”.


Flujo de ejecución

1. El usuario realiza una solicitud desde el navegador (por ejemplo, agregar o eliminar un vehículo).
2. El Servlet (`servlets.java`) recibe la solicitud y la traduce en una operación CRUD.
3. El Facade (`VehiculoFacade.java`) valida las reglas de negocio.
4. Si todo es correcto, se llama al DAO (`VehiculoDAO.java`) para ejecutar la consulta SQL.
5. El resultado o mensaje se reenvía a la vista JSP, mostrando la respuesta al usuario.


Tecnologías utilizadas

* Java EE / Jakarta EE 10
* Servlets y EJB (@Stateless)
* JDBC / DataSource
* MySQL 8
* NetBeans IDE
* Servidor Payara / GlassFish
* JSP para vistas


Despliegue y ejecución

1. Configurar un recurso JDBC llamado `jdbc/garageDB` en el servidor de aplicaciones (Payara o GlassFish).
2. Crear la base de datos y tabla `vehiculos` en MySQL usando el script proporcionado.
3. Abrir el proyecto en NetBeans.
4. Verificar que el `DataSource` está correctamente configurado.
5. Ejecutar el proyecto desde el IDE o acceder mediante:

   ```
   http://localhost:8080/vehicleDAO
   ```



Equipo y control de versiones

El proyecto se gestiona mediante Git siguiendo buenas prácticas:

* Cada estudiante trabaja en su propia rama (`feature/nombre`, `bugfix/arreglo`).
* Se deben hacer Pull Requests revisadas antes del merge.
* Los commits deben tener mensajes descriptivos, por ejemplo:
  `Implementa validación de color permitido en VehiculoFacade.java`.


Estado final

Todas las clases, validaciones, excepciones y controladores cumplen los requerimientos del taller “Taller Garaje Parte 2 – Persistencia, Fachadas y Controladores”.
El sistema está completamente funcional y validado para entrega.
