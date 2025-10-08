package com.garaje.controller;

import com.garaje.facade.VehiculoFacade;
import com.garaje.model.Vehiculo;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controlador web para la gestión de vehículos.
 * Recibe peticiones HTTP y las traduce en operaciones CRUD.
 * Debe mostrar mensajes claros en error de negocio.
 * 
 * @author danie
 * @version 1.0
 */
@WebServlet("/vehiculos")
public class servlets extends HttpServlet {

    @EJB
    private VehiculoFacade facade;

    /**
     * Maneja peticiones GET para listar y eliminar vehículos.
     * 
     * @param request Objeto HttpServletRequest con los datos de la petición
     * @param response Objeto HttpServletResponse para enviar la respuesta
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if (action == null || action.equals("listar")) {
                listarVehiculos(request, response);
            } else if (action.equals("eliminar")) {
                eliminarVehiculo(request, response);
            }
        } catch (Exception e) {
            manejarError(request, response, "Error al cargar los vehículos", e);
        }
    }

    /**
     * Maneja peticiones POST para agregar y actualizar vehículos.
     * 
     * @param request Objeto HttpServletRequest con los datos del formulario
     * @param response Objeto HttpServletResponse para enviar la respuesta
     * @throws ServletException Si ocurre un error en el servlet
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if (action != null && action.equals("agregar")) {
                agregarVehiculo(request, response);
            } else if (action != null && action.equals("actualizar")) {
                actualizarVehiculo(request, response);
            }
        } catch (Exception e) {
            manejarError(request, response, "Error en la operación", e);
        }
    }

    /**
     * Lista todos los vehículos de la base de datos.
     * Obtiene la lista desde el Facade y la envía a la vista JSP.
     * 
     * @param request Objeto para establecer atributos de la petición
     * @param response Objeto para enviar la respuesta
     * @throws Exception Si hay error al consultar la base de datos
     */
    private void listarVehiculos(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<Vehiculo> vehiculos = facade.listar();
        request.setAttribute("vehiculos", vehiculos);
        request.getRequestDispatcher("vehiculos.jsp").forward(request, response);
    }

    /**
     * Agrega un nuevo vehículo al sistema.
     * Obtiene los datos del formulario, crea el objeto Vehiculo y lo envía al Facade.
     * Si hay error de validación, muestra el mensaje en la vista.
     * 
     * @param request Objeto que contiene los parámetros del formulario
     * @param response Objeto para enviar la respuesta (redirect si éxito)
     * @throws Exception Si hay error al agregar el vehículo
     */
    private void agregarVehiculo(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // Obtener datos del formulario
        String placa = request.getParameter("placa");
        String marca = request.getParameter("marca");
        String modelo = request.getParameter("modelo");
        String color = request.getParameter("color");
        String propietario = request.getParameter("propietario");

        // Crear objeto Vehiculo con los datos
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(placa);
        vehiculo.setMarca(marca);
        vehiculo.setModelo(modelo);
        vehiculo.setColor(color);
        vehiculo.setPropietario(propietario);

        try {
            // Intentar agregar (aquí se validan las reglas de negocio)
            facade.agregar(vehiculo);
            response.sendRedirect("vehiculos");
        } catch (Exception e) {
            // Si hay error de validación, mostrar mensaje en la vista
            request.setAttribute("error", e.getMessage());
            listarVehiculos(request, response);
        }
    }

    /**
     * Actualiza un vehículo existente en el sistema.
     * Obtiene los datos del formulario incluyendo el ID y actualiza el registro.
     * Redirige a la lista de vehículos después de actualizar.
     * 
     * @param request Objeto que contiene los parámetros del formulario
     * @param response Objeto para enviar la respuesta (redirect)
     * @throws Exception Si hay error al actualizar o el vehículo no existe
     */
    private void actualizarVehiculo(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // Obtener ID y datos del formulario
        int id = Integer.parseInt(request.getParameter("id"));
        String placa = request.getParameter("placa");
        String marca = request.getParameter("marca");
        String modelo = request.getParameter("modelo");
        String color = request.getParameter("color");
        String propietario = request.getParameter("propietario");

        // Crear objeto Vehiculo con los datos actualizados
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(id);
        vehiculo.setPlaca(placa);
        vehiculo.setMarca(marca);
        vehiculo.setModelo(modelo);
        vehiculo.setColor(color);
        vehiculo.setPropietario(propietario);

        // Actualizar (aquí se validan las reglas de negocio)
        facade.actualizar(vehiculo);
        response.sendRedirect("vehiculos");
    }

    /**
     * Elimina un vehículo del sistema por su ID.
     * Valida que no sea del Administrador antes de eliminar.
     * Si hay error, muestra el mensaje en la vista.
     * 
     * @param request Objeto que contiene el ID del vehículo a eliminar
     * @param response Objeto para enviar la respuesta (redirect si éxito)
     * @throws Exception Si hay error al eliminar o no se puede eliminar
     */
    private void eliminarVehiculo(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        int id = Integer.parseInt(request.getParameter("id"));

        try {
            // Intentar eliminar (valida regla del Administrador)
            facade.eliminar(id);
            response.sendRedirect("vehiculos");
        } catch (Exception e) {
            // Si hay error (ej: es del Administrador), mostrar mensaje
            request.setAttribute("error", e.getMessage());
            listarVehiculos(request, response);
        }
    }

    /**
     * Maneja errores de forma centralizada.
     * Captura excepciones y muestra mensajes amigables al usuario.
     * No muestra stack traces técnicos, solo mensajes comprensibles.
     * 
     * @param request Objeto para establecer el mensaje de error
     * @param response Objeto para enviar la respuesta
     * @param mensajeError Mensaje descriptivo del error
     * @param e Excepción capturada
     * @throws ServletException Si ocurre un error al forwarding
     * @throws IOException Si ocurre un error de entrada/salida
     */
    private void manejarError(HttpServletRequest request, HttpServletResponse response,
            String mensajeError, Exception e)
            throws ServletException, IOException {
        // Mostrar solo mensaje amigable, sin stack trace técnico
        request.setAttribute("error", mensajeError + ": " + e.getMessage());
        request.getRequestDispatcher("vehiculos.jsp").forward(request, response);
    }
}