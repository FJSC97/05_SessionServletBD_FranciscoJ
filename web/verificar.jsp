<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.RegistroPendiente"%>
<%
    final long DURACION_CODIGO_MS = 50_000;

    RegistroPendiente pendienteCheck = (session != null) ? (RegistroPendiente) session.getAttribute("registroPendiente") : null;
    if (pendienteCheck == null)
    {
        response.sendRedirect("registro.jsp?expirado=1");
        return;
    }

    long transcurrido = System.currentTimeMillis() - pendienteCheck.getGeneradoEn();
    int restanteSeg = (int) Math.max(0, Math.min(50, (DURACION_CODIGO_MS - transcurrido) / 1000));
    boolean codigoExpirado = (transcurrido > DURACION_CODIGO_MS);

    String correo = request.getParameter("correo");
    String reenviado = request.getParameter("reenviado");
    String error = (String) request.getAttribute("error");
    String correoForward = (String) request.getAttribute("tfCorreo");
    if (correoForward != null) correo = correoForward;
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Confirma tu cuenta</title>
        <link rel="stylesheet" href="style.css">
    </head>
    <body>
        <div class="container auth-container">
            <div class="card auth-card">
                <h2>Confirma tu cuenta</h2>
                <p>Te enviamos un código de 6 dígitos a tu correo. Escríbelo aquí para activar tu cuenta.</p>

                <% if ("1".equals(reenviado)) { %>
                    <p class="msg-ok">Te reenviamos un nuevo código a tu correo.</p>
                <% } %>

                <% if (codigoExpirado) { %>
                    <p class="msg-error">Se acabó el tiempo para ese código. Da clic en "Reenviar código" para recibir uno nuevo.</p>
                <% } %>

                <% if (error != null) { %>
                    <p class="msg-error"><%= error %></p>
                <% } %>

                <p id="temporizador" class="temporizador<%= codigoExpirado ? " temporizador-agotado" : "" %>">
                    <% if (codigoExpirado) { %>
                        Tiempo agotado
                    <% } else { %>
                        Tiempo restante: <span id="segundos"><%= restanteSeg %></span> segundos
                    <% } %>
                </p>

                <form method="post" action="Servlet_Usuario" class="form-auth" id="formCodigo">
                    <input type="hidden" name="accion" value="VerificarCodigo"/>
                    <div>
                        <label for="tfCorreo">Correo</label>
                        <input type="email" name="tfCorreo" id="tfCorreo" value="<%= (correo != null) ? correo : "" %>" required/>
                    </div>
                    <div>
                        <label for="tfCodigo">Código de verificación</label>
                        <input type="text" name="tfCodigo" id="tfCodigo" maxlength="6" placeholder="123456" <%= codigoExpirado ? "disabled" : "" %> required/>
                    </div>
                    <input type="submit" id="btnConfirmar" value="Confirmar cuenta" <%= codigoExpirado ? "disabled" : "" %>/>
                </form>

                <form method="post" action="Servlet_Usuario" class="form-resend">
                    <input type="hidden" name="accion" value="Reenviar"/>
                    <input type="hidden" name="tfCorreo" value="<%= (correo != null) ? correo : "" %>"/>
                    <button type="submit" class="link-button">Reenviar código</button>
                </form>
            </div>
        </div>

        <script>
            (function () {
                var segundosRestantes = <%= restanteSeg %>;
                var yaExpirado = <%= codigoExpirado %>;

                if (yaExpirado || segundosRestantes <= 0) {
                    return;
                }

                var spanSegundos = document.getElementById("segundos");
                var temporizador = document.getElementById("temporizador");
                var btnConfirmar = document.getElementById("btnConfirmar");
                var campoCodigo = document.getElementById("tfCodigo");

                var intervalo = setInterval(function () {
                    segundosRestantes--;

                    if (segundosRestantes <= 0) {
                        clearInterval(intervalo);
                        temporizador.textContent = "Tiempo agotado";
                        temporizador.classList.add("temporizador-agotado");
                        btnConfirmar.disabled = true;
                        campoCodigo.disabled = true;

                        var aviso = document.createElement("p");
                        aviso.className = "msg-error";
                        aviso.textContent = "Se acabó el tiempo para ese código. Da clic en \"Reenviar código\" para recibir uno nuevo.";
                        temporizador.insertAdjacentElement("afterend", aviso);
                        return;
                    }

                    spanSegundos.textContent = segundosRestantes;
                    if (segundosRestantes <= 10) {
                        temporizador.classList.add("temporizador-critico");
                    }
                }, 1000);
            })();
        </script>
    </body>
</html>
