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
 reglas.
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
 ejemplo, no agregar si la placa ya existe, si propietario está vacío,
 etc.
     * @param v
     * @throws java.sql.SQLException
     */
    public void agregar(Vehiculo v) throws SQLException, Exception {
        
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);
            
            if(dao.existePlaca(v.getPlaca())){
                throw new Exception("no puede haber placa duplicada");
            }
            if(v.getPropietario() == null || v.getPropietario().length() < 5){
                throw new Exception("la casilla de propetario debe tener mas de 5 caracteres"
                + " o no pude estar basia"); 
            }
            if(v.getMarca().length()<3 && v.getModelo().length()<3 && v.getPlaca().length()<3){
                throw new Exception("las casillas color, modelo y placa deben tner minimo 3 caracteres ");
            }
           
            
            dao.agregar(v);
        }
    }
//ddadda
    /**
     * Actualiza vehículo; incluir reglas de negocio.
     * @param v
     * @throws java.sql.SQLException
     */
    public void actualizar(Vehiculo v) throws SQLException {
        
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);
            dao.actualizar(v);
        }
    }

    /**
     * Elimina vehículo por id.
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

}

