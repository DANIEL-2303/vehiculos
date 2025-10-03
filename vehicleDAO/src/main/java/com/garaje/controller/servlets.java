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

@WebServlet("/vehiculos")
public class servlets extends HttpServlet {

    @EJB
    private VehiculoFacade facade;

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

    private void listarVehiculos(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        List<Vehiculo> vehiculos = facade.listar();  // USA listar() no findAll()
        request.setAttribute("vehiculos", vehiculos);
        request.getRequestDispatcher("vehiculos.jsp").forward(request, response);
    }

    private void agregarVehiculo(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String placa = request.getParameter("placa");
        String marca = request.getParameter("marca");
        String modelo = request.getParameter("modelo");
        String color = request.getParameter("color");
        String propietario = request.getParameter("propietario");

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(placa);
        vehiculo.setMarca(marca);
        vehiculo.setModelo(modelo);
        vehiculo.setColor(color);
        vehiculo.setPropietario(propietario);

        try {
            facade.agregar(vehiculo);
            response.sendRedirect("vehiculos");  // Redirige después de agregar
        } catch (Exception e) {
            // Si hay error, vuelve a mostrar el formulario con el mensaje
            request.setAttribute("error", e.getMessage());
            listarVehiculos(request, response);
        }
    }

    private void actualizarVehiculo(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        int id = Integer.parseInt(request.getParameter("id"));
        String placa = request.getParameter("placa");
        String marca = request.getParameter("marca");
        String modelo = request.getParameter("modelo");
        String color = request.getParameter("color");
        String propietario = request.getParameter("propietario");

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(id);
        vehiculo.setPlaca(placa);
        vehiculo.setMarca(marca);
        vehiculo.setModelo(modelo);
        vehiculo.setColor(color);
        vehiculo.setPropietario(propietario);

        facade.actualizar(vehiculo);
        response.sendRedirect("vehiculos");
    }

    private void eliminarVehiculo(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        int id = Integer.parseInt(request.getParameter("id"));

        try {
            facade.eliminar(id);
            response.sendRedirect("vehiculos");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            listarVehiculos(request, response);
        }
    }

    private void manejarError(HttpServletRequest request, HttpServletResponse response,
            String mensajeError, Exception e)
            throws ServletException, IOException {
        request.setAttribute("error", mensajeError + ": " + e.getMessage());
        request.getRequestDispatcher("vehiculos.jsp").forward(request, response);
    }
}
