<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    String error = (String) request.getAttribute("error");
    String correoPrevio = (String) request.getAttribute("tfCorreo");
    boolean registrado = "1".equals(request.getParameter("registrado"));
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Iniciar sesión</title>
        <link rel="stylesheet" href="style.css">
    </head>
    <body>
        <div class="container auth-container">
            <div class="card auth-card">
                <h2>Iniciar sesión</h2>

                <% if (registrado) { %>
                    <p class="msg-ok">Tu cuenta se registró correctamente. Te enviamos un código por correo; revísalo (y la carpeta de spam) para confirmarla.</p>
                <% } %>

                <% if (error != null) { %>
                    <p class="msg-error"><%= error %></p>
                <% } %>

                <form method="post" action="Servlet_Usuario" class="form-auth">
                    <input type="hidden" name="accion" value="Login"/>
                    <div>
                        <label for="tfCorreo">Usuario (correo)</label>
                        <input type="email" name="tfCorreo" id="tfCorreo" value="<%= (correoPrevio != null) ? correoPrevio : "" %>" placeholder="correo@ejemplo.com" required/>
                    </div>
                    <div>
                        <label for="tfPassword">Contraseña</label>
                        <input type="password" name="tfPassword" id="tfPassword" placeholder="Contraseña" required/>
                    </div>
                    <input type="submit" value="Iniciar sesión"/>
                </form>

                <p class="auth-switch">¿No tienes cuenta? <a href="registro.jsp">Regístrate aquí</a></p>
            </div>
        </div>
    </body>
</html>
