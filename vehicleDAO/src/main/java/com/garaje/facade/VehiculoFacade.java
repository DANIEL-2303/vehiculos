/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.garaje.facade;

/**
 *
 * @author danie
 */
import com.garaje.model.Vehiculo;
import com.garaje.persistence.VehiculoDAO;
import jakarta.ejb.Stateless;
import jakarta.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Fachada para operaciones sobre vehículos. Deben agregarse reglas de negocio
 * antes de llamar al DAO.
 */
@Stateless
public class VehiculoFacade {

    @Resource(lookup = "jdbc/garageDB")
    private DataSource ds;

    /**
     * Lista todos los vehículos.Debe documentar excepciones si se agregan
     * reglas.
     *
     * @return
     * @throws java.sql.SQLException
     */
    public List<Vehiculo> listar() throws SQLException {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);
            return dao.listar();
        }
    }

    /**
     * Busca vehículo por id.Manejar errores en llamada.
     *
     * @param id
     * @return
     * @throws java.sql.SQLException
     */
    public Vehiculo buscarPorId(int id) throws SQLException {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);
            return dao.buscarPorId(id);
        }
    }

    /**
     * Agrega vehículo.Debe validar con reglas de negocio antes de agregar.Por
     * ejemplo, no agregar si la placa ya existe, si propietario está vacío,
     * etc.
     *
     * @param v
     * @throws java.sql.SQLException
     */
    public void agregar(Vehiculo v) throws SQLException, Exception {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);

            // 1. Placa duplicada
            if (dao.existePlaca(v.getPlaca())) {
                throw new Exception("No puede haber placas duplicadas.");
            }

            // 2. Propietario
            if (v.getPropietario() == null || v.getPropietario().trim().length() < 5) {
                throw new Exception("El propietario debe tener al menos 5 caracteres y no puede estar vacío.");
            }

            // 3. Marca, modelo, placa (mínimo 3 caracteres)
            if (v.getMarca().length() < 3 || v.getModelo().length() < 3 || v.getPlaca().length() < 3) {
                throw new Exception("Marca, modelo y placa deben tener al menos 3 caracteres.");
            }

            // 4. Color permitido
            List<String> coloresPermitidos = Arrays.asList("Rojo", "Blanco", "Negro", "Azul", "Gris");
            if (!coloresPermitidos.contains(v.getColor())) {
                throw new Exception("Color inválido. Debe ser uno de: " + coloresPermitidos);
            }

            // 5. Año de modelo no mayor a 20 años (asumiendo campo modelo = año)
            try {
                int anio = Integer.parseInt(v.getModelo());
                int anioActual = java.time.Year.now().getValue();
                if (anioActual - anio > 20) {
                    throw new Exception("No se aceptan vehículos con más de 20 años de antigüedad.");
                }
            } catch (NumberFormatException e) {
                throw new Exception("El modelo debe ser un año numérico válido.");
            }

            // 6. Validación básica contra SQL injection (usando método auxiliar)
            if (contieneSQLInjection(v)) {
                throw new Exception("Datos inválidos: posible intento de inyección SQL.");
            }

            // 7. Notificación si marca es Ferrari
            if (v.getMarca().equalsIgnoreCase("Ferrari")) {
                System.out.println("⚠️ Notificación: Se ha agregado un Ferrari al garaje (simulado).");
            }
            // Si pasa todas las validaciones, agregar
            dao.agregar(v);
        }
    }

//ddadda
    /**
     * Actualiza vehículo; incluir reglas de negocio.
     *
     * @param v
     * @throws java.sql.SQLException
     */
    public void actualizar(Vehiculo v) throws SQLException, Exception {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);

            Vehiculo existente = dao.buscarPorId(v.getId());
            if (existente == null) {
                throw new Exception("No se puede actualizar: el vehículo no existe.");
            }

            dao.actualizar(v);
        }
    }

    /**
     * Elimina vehículo por id.
     *
     * @param id
     * @throws java.sql.SQLException
     */
    public void eliminar(int id) throws SQLException, Exception {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);

            Vehiculo v = dao.buscarPorId(id);
            if (v == null) {
                throw new Exception("El vehículo no existe");
            }

            if (v.getPropietario().equalsIgnoreCase("Administrador")) {
                throw new Exception("No se puede eliminar un vehículo del Administrador");
            }

            dao.eliminar(id);
        }
    }

    /**
     * Valida si algún campo del vehículo contiene patrones sospechosos de SQL
     * Injection.
     *
     * @param v objeto Vehiculo a validar.
     * @return true si se detecta contenido peligroso.
     */
    private boolean contieneSQLInjection(Vehiculo v) {
        // Se escapa el guion para evitar error de expresión mal formada
        String regex = ".*([';]|--|(?i)DROP|(?i)DELETE|(?i)INSERT|(?i)UPDATE).*";

        // Validar cada campo, asegurando que no sea nulo antes de aplicar .matches()
        return (v.getPlaca() != null && v.getPlaca().matches(regex))
                || (v.getMarca() != null && v.getMarca().matches(regex))
                || (v.getModelo() != null && v.getModelo().matches(regex))
                || (v.getColor() != null && v.getColor().matches(regex))
                || (v.getPropietario() != null && v.getPropietario().matches(regex));
    }
}