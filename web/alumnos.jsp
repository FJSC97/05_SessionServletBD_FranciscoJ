<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Alumno"%>
<%@page import="modelo.Usuario"%>
<%@page import="dao.DAOAlumno"%>


<%!
    DAOAlumno lista = new DAOAlumno();
    Alumno edit = null ;
%>


<%
   edit = null;
   if(request.getAttribute("edit")!= null)
   {
   edit = (Alumno) request.getAttribute("edit");
   }

   Usuario usuarioSesion = (session != null) ? (Usuario) session.getAttribute("usuario") : null;
   if (usuarioSesion == null)
   {
       response.sendRedirect("login.jsp");
       return;
   }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registro de Alumnos</title>
        <link rel="stylesheet" href="style.css">   
    </head>
    <body>    
    <div class="container">
        <div class="userbar">
            <span>Hola, <strong><%= usuarioSesion.getNombreCompleto() %></strong></span>
            <span class="estado estado-<%= usuarioSesion.getEstatus().toLowerCase() %>"><%= usuarioSesion.getEstatus() %></span>
            <form method="post" action="Servlet_Usuario">
                <input type="hidden" name="accion" value="Logout"/>
                <input type="submit" class="btn-logout" value="Cerrar sesión"/>
            </form>
        </div>
        <div class="card">
            <div>
                <h2 class ="mt-4"><%= (edit != null) ? "Modificar Calificaciones" : "Registro de calificaciones"%></h2>
                <form  method="post"                                    id="form_registro" action="Servlet_Alumno" class="form-grid">
                    <input      type="hidden" name="accion"             id="accion"             value="<%= (edit != null) ? "Modificar" : "Agregar"%>" />
                    <input      type="hidden" name="tfNLOld"            id="tfMatriculaOld"     value="<%= (edit != null) ? edit.getNL() : ""%>"/>
                    <div><input type="text"   name="tfNL"               id="tfMatricula"        value="<%= (edit != null) ? edit.getNL() : ""%>"             placeholder="NL"              required/></div>
                    <div><input type="text"   name="tfNombre"           id="tfNombre"           value="<%= (edit != null) ? edit.getNombre() : ""%>"         placeholder="Nombre"           required/></div>
                    <div><input type="text"   name="tfPaterno"          id="tfPaterno"          value="<%= (edit != null) ? edit.getPaterno() : ""%>"        placeholder="Apellido Paterno" required/></div>
                    <div><input type="text"   name="tfMaterno"          id="tfMaterno"          value="<%= (edit != null) ? edit.getMaterno() : ""%>"        placeholder="Apellido Materno" required/></div>
                    <input      type="submit" name="btnAccion"          id="btnAccion"          value="<%= (edit != null) ? "Modificar" : "Agregar"%>">
                </form>
            </div>
        </div>
        
        <div class="card">
            <h2> Lista de alumnos registrados </h2>
            <%= lista.mostrar() %>
        </div>
    </div>
    </body>
</html>