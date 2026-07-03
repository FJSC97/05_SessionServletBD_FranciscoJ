package dao;
import modelo.Usuario;
import conexion.ConexionMySQL;
import util.Seguridad;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class DAOUsuario
{
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    /**
     * Verifica si el correo ya existe registrado.
     */
    public boolean existeCorreo(String correo)
    {
        boolean existe = false;
        String sql = "SELECT idUsuario FROM usuarios WHERE correo = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            rs = ps.executeQuery();
            existe = rs.next();
            rs.close();
            ps.close();
            con.close();
        }
        catch (SQLException e) {}
        return existe;
    }

    public boolean registrar(Usuario usuario)
    {
        boolean ok = false;
        String sql = "INSERT INTO usuarios (nombreCompleto, correo, password, estatus, verificado, token) "
                   + "VALUES (?, ?, ?, 'Inactivo', 1, NULL)";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, usuario.getNombreCompleto());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, Seguridad.encriptar(usuario.getPassword()));
            ps.executeUpdate();
            ps.close();
            con.close();
            ok = true;
        }
        catch (SQLException e) {}
        return ok;
    }

    public Usuario validar(String correo, String password)
    {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND password = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            ps.setString(2, Seguridad.encriptar(password));
            rs = ps.executeQuery();
            if (rs.next())
            {
                usuario = mapearUsuario(rs);
            }
            rs.close();
            ps.close();
            con.close();
        }
        catch (SQLException e) {}
        return usuario;
    }

    public Usuario buscarPorCorreo(String correo)
    {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            rs = ps.executeQuery();
            if (rs.next())
            {
                usuario = mapearUsuario(rs);
            }
            rs.close();
            ps.close();
            con.close();
        }
        catch (SQLException e) {}
        return usuario;
    }

    public boolean verificarCuenta(int idUsuario)
    {
        boolean ok = false;
        String sql = "UPDATE usuarios SET verificado = 1, token = NULL WHERE idUsuario = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
            ps.close();
            con.close();
            ok = true;
        }
        catch (SQLException e) {}
        return ok;
    }

    public boolean actualizarToken(int idUsuario, String token)
    {
        boolean ok = false;
        String sql = "UPDATE usuarios SET token = ? WHERE idUsuario = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, token);
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
            ps.close();
            con.close();
            ok = true;
        }
        catch (SQLException e) {}
        return ok;
    }

    public boolean actualizarEstatus(int idUsuario, String estatus)
    {
        boolean ok = false;
        String sql = "UPDATE usuarios SET estatus = ? WHERE idUsuario = ?";
        try
        {
            con = ConexionMySQL.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, estatus);
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
            ps.close();
            con.close();
            ok = true;
        }
        catch (SQLException e) {}
        return ok;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException
    {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("idUsuario"));
        usuario.setNombreCompleto(rs.getString("nombreCompleto"));
        usuario.setCorreo(rs.getString("correo"));
        usuario.setPassword(rs.getString("password"));
        usuario.setEstatus(rs.getString("estatus"));
        usuario.setVerificado(rs.getBoolean("verificado"));
        usuario.setToken(rs.getString("token"));
        return usuario;
    }
}