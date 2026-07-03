package control;

import modelo.Usuario;
import modelo.RegistroPendiente;
import dao.DAOUsuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EnviadorCorreo;

public class Servlet_Usuario extends HttpServlet {

    private DAOUsuario dao;

    private static final long DURACION_CODIGO_MS = 50_000;

    private String generarCodigo()
    {
        int numero = (int) (Math.random() * 900000) + 100000; 
        return String.valueOf(numero);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    {
        response.setContentType("text/html;charset=UTF-8");

        try
        {
            String accion = request.getParameter("accion");

            if ("Registrar".equals(accion))
            {
                String nombreCompleto = request.getParameter("tfNombreCompleto");
                String correo = request.getParameter("tfCorreo");
                String password = request.getParameter("tfPassword");
                String password2 = request.getParameter("tfPassword2");

                dao = new DAOUsuario();

                if (nombreCompleto == null || nombreCompleto.isBlank()
                        || correo == null || correo.isBlank()
                        || password == null || password.isBlank())
                {
                    request.setAttribute("error", "Todos los campos son obligatorios.");
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/registro.jsp");
                    rd.forward(request, response);
                    return;
                }

                if (!password.equals(password2))
                {
                    request.setAttribute("error", "Las contraseñas no coinciden.");
                    request.setAttribute("tfNombreCompleto", nombreCompleto);
                    request.setAttribute("tfCorreo", correo);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/registro.jsp");
                    rd.forward(request, response);
                    return;
                }

                if (dao.existeCorreo(correo))
                {
                    request.setAttribute("error", "Ese correo ya está registrado.");
                    request.setAttribute("tfNombreCompleto", nombreCompleto);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/registro.jsp");
                    rd.forward(request, response);
                    return;
                }

                Usuario usuario = new Usuario();
                usuario.setNombreCompleto(nombreCompleto);
                usuario.setCorreo(correo);
                usuario.setPassword(password);

                String codigo = generarCodigo();

                RegistroPendiente pendiente = new RegistroPendiente(nombreCompleto, correo, password, codigo);
                HttpSession session = request.getSession(true);
                session.setAttribute("registroPendiente", pendiente);
                session.setMaxInactiveInterval(600);

                boolean correoEnviado = EnviadorCorreo.enviarCodigoVerificacion(correo, nombreCompleto, codigo);

                String correoCodificado = URLEncoder.encode(correo, StandardCharsets.UTF_8);

                if (correoEnviado)
                {
                    response.sendRedirect("verificar.jsp?correo=" + correoCodificado);
                }
                else
                {
                    session.removeAttribute("registroPendiente");
                    request.setAttribute("error", "No se pudo enviar el código de verificación. Intenta registrarte de nuevo.");
                    request.setAttribute("tfNombreCompleto", nombreCompleto);
                    request.setAttribute("tfCorreo", correo);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/registro.jsp");
                    rd.forward(request, response);
                }
                return;
            }
            else if ("VerificarCodigo".equals(accion))
            {
                String correo = request.getParameter("tfCorreo");
                String codigo = request.getParameter("tfCodigo");

                HttpSession session = request.getSession(false);
                RegistroPendiente pendiente = (session != null) ? (RegistroPendiente) session.getAttribute("registroPendiente") : null;

                if (pendiente == null)
                {
                    response.sendRedirect("registro.jsp?expirado=1");
                    return;
                }

                long transcurrido = System.currentTimeMillis() - pendiente.getGeneradoEn();
                if (transcurrido > DURACION_CODIGO_MS)
                {
                    request.setAttribute("tfCorreo", correo);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/verificar.jsp");
                    rd.forward(request, response);
                    return;
                }

                if (correo != null && correo.equalsIgnoreCase(pendiente.getCorreo())
                        && codigo != null && codigo.equals(pendiente.getCodigo()))
                {
                    dao = new DAOUsuario();

                    if (dao.existeCorreo(pendiente.getCorreo()))
                    {
                        session.removeAttribute("registroPendiente");
                        request.setAttribute("error", "Ese correo ya fue registrado. Inicia sesión o usa otro correo.");
                        RequestDispatcher rd = getServletContext().getRequestDispatcher("/registro.jsp");
                        rd.forward(request, response);
                        return;
                    }

                    Usuario usuario = new Usuario();
                    usuario.setNombreCompleto(pendiente.getNombreCompleto());
                    usuario.setCorreo(pendiente.getCorreo());
                    usuario.setPassword(pendiente.getPassword());
                    dao.registrar(usuario);

                    session.removeAttribute("registroPendiente");
                    response.sendRedirect("login.jsp?verificado=1");
                }
                else
                {
                    request.setAttribute("error", "El código no es válido. Revísalo o pide uno nuevo.");
                    request.setAttribute("tfCorreo", correo);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/verificar.jsp");
                    rd.forward(request, response);
                }
                return;
            }
            else if ("Reenviar".equals(accion))
            {
                String correo = request.getParameter("tfCorreo");

                HttpSession session = request.getSession(false);
                RegistroPendiente pendiente = (session != null) ? (RegistroPendiente) session.getAttribute("registroPendiente") : null;

                if (pendiente != null && pendiente.getCorreo().equalsIgnoreCase(correo))
                {
                    String codigo = generarCodigo();
                    pendiente.setCodigo(codigo);
                    pendiente.setGeneradoEn(System.currentTimeMillis());
                    session.setAttribute("registroPendiente", pendiente);
                    session.setMaxInactiveInterval(600);

                    EnviadorCorreo.enviarCodigoVerificacion(pendiente.getCorreo(), pendiente.getNombreCompleto(), codigo);

                    String correoCodificado = URLEncoder.encode(correo, StandardCharsets.UTF_8);
                    response.sendRedirect("verificar.jsp?correo=" + correoCodificado + "&reenviado=1");
                }
                else
                {
                    response.sendRedirect("registro.jsp?expirado=1");
                }
                return;
            }
            else if ("Login".equals(accion))
            {
                String correo = request.getParameter("tfCorreo");
                String password = request.getParameter("tfPassword");

                dao = new DAOUsuario();
                Usuario usuario = dao.validar(correo, password);

                if (usuario != null && usuario.isVerificado())
                {
                    dao.actualizarEstatus(usuario.getIdUsuario(), "Activo");
                    usuario.setEstatus("Activo");

                    HttpSession session = request.getSession(true);
                    session.setAttribute("usuario", usuario);

                    response.sendRedirect("Servlet_Alumno");
                    return;
                }
                else if (usuario != null && !usuario.isVerificado())
                {
                    request.setAttribute("error", "Tu cuenta aún no ha sido confirmada. Revisa el código que te enviamos por correo.");
                    request.setAttribute("tfCorreo", correo);
                    request.setAttribute("noVerificado", Boolean.TRUE);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
                    rd.forward(request, response);
                    return;
                }
                else
                {
                    request.setAttribute("error", "Correo o contraseña incorrectos.");
                    request.setAttribute("tfCorreo", correo);
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
                    rd.forward(request, response);
                    return;
                }
            }
            else if ("Logout".equals(accion))
            {
                HttpSession session = request.getSession(false);
                if (session != null)
                {
                    Usuario usuario = (Usuario) session.getAttribute("usuario");
                    if (usuario != null)
                    {
                        dao = new DAOUsuario();
                        dao.actualizarEstatus(usuario.getIdUsuario(), "Inactivo");
                    }
                    session.invalidate();
                }
                response.sendRedirect("login.jsp");
                return;
            }
            else
            {
                response.sendRedirect("login.jsp");
            }
        }
        catch (IOException | ServletException ex)
        {
            Logger.getLogger(Servlet_Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet de autenticación: registro, login y logout";
    }
}