<%-- 
    Document   : vehiculos
    Created on : 1/10/2025, 10:29:10 p. m.
    Author     : danie
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="com.garaje.model.Vehiculo"%>
<%@page import="java.util.List"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html;charset=UTF-8" language="java"%>
<html>
    <head>
        <title>Sistema de Garaje</title>
    </head>
    <body>
        <h1>Sistema de Garaje</h1>
        
        <!-- Mensajes -->
        <c:if test="${not empty mensaje}">
            <p style="color: green;"><strong>${mensaje}</strong></p>
        </c:if>
        
        <c:if test="${not empty error}">
            <p style="color: red;"><strong>Error: ${error}</strong></p>
        </c:if>
        
        <!-- Formulario Agregar -->
        <h2>Agregar Vehículo</h2>
        <form action="vehiculos" method="post">
            Placa: <input type="text" name="placa" /><br/>
            Marca: <input type="text" name="marca" /><br/>
            Modelo: <input type="text" name="modelo" /><br/>
            Color: <select name="color">
                <option value="Rojo">Rojo</option>
                <option value="Blanco">Blanco</option>
                <option value="Negro">Negro</option>
                <option value="Azul">Azul</option>
                <option value="Gris">Gris</option>
            </select><br/>
            Propietario: <input type="text" name="propietario" /><br/>
            <input type="submit" value="Agregar" />
        </form>
        
        <!-- Lista de Vehículos -->
        <h2>Lista de Vehículos</h2>
        <table border="1">
            <tr>
                <th>ID</th>
                <th>Placa</th>
                <th>Marca</th>
                <th>Modelo</th>
                <th>Color</th>
                <th>Propietario</th>
                <th>Acciones</th>
            </tr>
            
            <c:forEach var="vehiculo" items="${vehiculos}">
                <tr>
                    <td>${vehiculo.id}</td>
                    <td>${vehiculo.placa}</td>
                    <td>${vehiculo.marca}</td>
                    <td>${vehiculo.modelo}</td>
                    <td>${vehiculo.color}</td>
                    <td>${vehiculo.propietario}</td>
                    <td>
                        <a href="vehiculos?accion=editar&id=${vehiculo.id}">Editar</a> |
                        <a href="vehiculos?accion=eliminar&id=${vehiculo.id}" 
                           onclick="return confirm('¿Eliminar este vehículo?')">Eliminar</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>