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
 * Maneja errores y muestra mensajes amigables al usuario.
 */
@WebServlet("/vehiculos")
public class servlets extends HttpServlet {
    
    @EJB
    private VehiculoFacade facade;
    
    /**
     * Maneja peticiones GET para listar vehículos.
     * @param request
     * @param response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        
        try {
            if (accion == null || accion.equals("listar")) {
                listarVehiculos(request, response);
            } else if (accion.equals("eliminar")) {
                eliminarVehiculo(request, response);
            } else if (accion.equals("editar")) {
                mostrarFormularioEditar(request, response);
            }
        } catch (Exception e) {
            manejarError(request, response, e.getMessage());
        }
    }
    
    /**
     * Maneja peticiones POST para agregar o actualizar vehículos.
     * @param request
     * @param response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        
        try {
            if (accion == null || accion.equals("agregar")) {
                agregarVehiculo(request, response);
            } else if (accion.equals("actualizar")) {
                actualizarVehiculo(request, response);
            }
        } catch (Exception e) {
            manejarError(request, response, e.getMessage());
        }
    }
    
    /**
     * Lista todos los vehículos y los envía a la vista.
     */
    private void listarVehiculos(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        List<Vehiculo> vehiculos = facade.listar();
        request.setAttribute("vehiculos", vehiculos);
        request.getRequestDispatcher("/vehiculos.jsp").forward(request, response);
    }
    
    /**
     * Agrega un nuevo vehículo con los datos del formulario.
     */
    private void agregarVehiculo(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        // Obtener datos del formulario
        String placa = request.getParameter("placa");
        String marca = request.getParameter("marca");
        String modelo = request.getParameter("modelo");
        String color = request.getParameter("color");
        String propietario = request.getParameter("propietario");
        
        // Crear vehículo
        Vehiculo v = new Vehiculo();
        v.setPlaca(placa);
        v.setMarca(marca);
        v.setModelo(modelo);
        v.setColor(color);
        v.setPropietario(propietario);
        
        // Agregar vehículo (aquí se validan las reglas de negocio)
        facade.agregar(v);
        
        // Mensaje de éxito
        request.setAttribute("mensaje", "Vehículo agregado exitosamente");
        listarVehiculos(request, response);
    }
    
    /**
     * Elimina un vehículo por su ID.
     */
    private void eliminarVehiculo(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        int id = Integer.parseInt(request.getParameter("id"));
        facade.eliminar(id);
        
        request.setAttribute("mensaje", "Vehículo eliminado exitosamente");
        listarVehiculos(request, response);
    }
    
    /**
     * Muestra el formulario de edición con los datos del vehículo.
     */
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        int id = Integer.parseInt(request.getParameter("id"));
        Vehiculo v = facade.buscarPorId(id);
        
        request.setAttribute("vehiculo", v);
        request.getRequestDispatcher("/editar.jsp").forward(request, response);
    }
    
    /**
     * Actualiza un vehículo existente.
     */
    private void actualizarVehiculo(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        // Obtener datos del formulario
        int id = Integer.parseInt(request.getParameter("id"));
        String placa = request.getParameter("placa");
        String marca = request.getParameter("marca");
        String modelo = request.getParameter("modelo");
        String color = request.getParameter("color");
        String propietario = request.getParameter("propietario");
        
        // Crear vehículo con datos actualizados
        Vehiculo v = new Vehiculo();
        v.setId(id);
        v.setPlaca(placa);
        v.setMarca(marca);
        v.setModelo(modelo);
        v.setColor(color);
        v.setPropietario(propietario);
        
        // Actualizar
        facade.actualizar(v);
        
        request.setAttribute("mensaje", "Vehículo actualizado exitosamente");
        listarVehiculos(request, response);
    }
    
    /**
     * Maneja errores y muestra mensajes amigables (sin stack traces).
     */
    private void manejarError(HttpServletRequest request, HttpServletResponse response, 
            String mensajeError) throws ServletException, IOException {
        
        request.setAttribute("error", mensajeError);
        
        try {
            listarVehiculos(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error al cargar los vehículos");
            request.getRequestDispatcher("/vehiculos.jsp").forward(request, response);
        }
    }
}