<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    String error = (String) request.getAttribute("error");
    String nombrePrevio = (String) request.getAttribute("tfNombreCompleto");
    String correoPrevio = (String) request.getAttribute("tfCorreo");
    boolean expirado = "1".equals(request.getParameter("expirado"));
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Crear cuenta</title>
        <link rel="stylesheet" href="style.css">
    </head>
    <body>
        <div class="container auth-container">
            <div class="card auth-card">
                <h2>Crear cuenta</h2>

                <% if (expirado) { %>
                    <p class="msg-error">Tu tiempo para confirmar el código expiró (o nunca llegaste a ponerlo). No te preocupes: no quedó nada guardado. Regístrate de nuevo.</p>
                <% } %>

                <% if (error != null) { %>
                    <p class="msg-error"><%= error %></p>
                <% } %>

                <form method="post" action="Servlet_Usuario" class="form-auth">
                    <input type="hidden" name="accion" value="Registrar"/>
                    <div>
                        <label for="tfNombreCompleto">Nombre completo</label>
                        <input type="text" name="tfNombreCompleto" id="tfNombreCompleto" value="<%= (nombrePrevio != null) ? nombrePrevio : "" %>" placeholder="Nombre completo" required/>
                    </div>
                    <div>
                        <label for="tfCorreo">Usuario (correo)</label>
                        <input type="email" name="tfCorreo" id="tfCorreo" value="<%= (correoPrevio != null) ? correoPrevio : "" %>" placeholder="correo@ejemplo.com" required/>
                    </div>
                    <div>
                        <label for="tfPassword">Contraseña</label>
                        <input type="password" name="tfPassword" id="tfPassword" placeholder="Contraseña" required/>
                    </div>
                    <div>
                        <label for="tfPassword2">Verificar contraseña</label>
                        <input type="password" name="tfPassword2" id="tfPassword2" placeholder="Verificar contraseña" required/>
                    </div>
                    <input type="submit" value="Registrarme"/>
                </form>

                <p class="auth-switch">¿Ya tienes cuenta? <a href="login.jsp">Inicia sesión aquí</a></p>
            </div>
        </div>
    </body>
</html>
