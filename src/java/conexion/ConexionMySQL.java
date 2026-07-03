package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL 
{
    private static String servidor;
    private static String nombreBD;
    private static String usuario;
    private static String password;
    private static String puerto;
    
    public static Connection getConnection() throws SQLException
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("No se cargo el drive");
        }
        
        servidor = "localhost";
        nombreBD = "registro_alumno";
        usuario = "root";
        password = "";
        puerto = "3306";
    
    String urlConexion = "jdbc:mysql://" + servidor + ":" + puerto + "/" + nombreBD;
        return DriverManager.getConnection(urlConexion, usuario, password);
    }
}