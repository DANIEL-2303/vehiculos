<%-- 
    Document   : editar
    Created on : 7/10/2025, 10:10:14 p. m.
    Author     : danie
--%>

<%@page import="com.garaje.model.Vehiculo"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        
        <% Vehiculo vehiculo = (Vehiculo) request.getAttribute("vehiculo"); %>
        
        <form action="vehiculos" method="post">
            <input type="hidden" name="accion" value="actualizar" />
            <input type="hidden" name="id" value="<%= vehiculo.getId() %>" />
            
            <label>ID:</label>
            <input type="text" value="<%= vehiculo.getId() %>" disabled /><br/>
            
            <label>Placa:</label>
            <input type="text" name="placa" value="<%= vehiculo.getPlaca() %>" required /><br/>
            
            <label>Marca:</label>
            <input type="text" name="marca" value="<%= vehiculo.getMarca() %>" required /><br/>
            
            <label>Modelo/Año:</label>
            <input type="text" name="modelo" value="<%= vehiculo.getModelo() %>" required /><br/>
            
            <label>Color:</label>
            <select name="color" required>
                <option value="Rojo" <%= vehiculo.getColor().equals("Rojo") ? "selected" : "" %>>Rojo</option>
                <option value="Blanco" <%= vehiculo.getColor().equals("Blanco") ? "selected" : "" %>>Blanco</option>
                <option value="Negro" <%= vehiculo.getColor().equals("Negro") ? "selected" : "" %>>Negro</option>
                <option value="Azul" <%= vehiculo.getColor().equals("Azul") ? "selected" : "" %>>Azul</option>
                <option value="Gris" <%= vehiculo.getColor().equals("Gris") ? "selected" : "" %>>Gris</option>
            </select><br/>
            
            <label>Propietario:</label>
            <input type="text" name="propietario" value="<%= vehiculo.getPropietario() %>" required /><br/>
            
            <br/>
            <input type="submit" value="Guardar Cambios" />
            <a href="vehiculos" class="btn btn-secondary">Cancelar</a>
        </form>
    </body>    
</html>
