package modelo;
public class Usuario
{
    private int idUsuario;
    private String nombreCompleto;
    private String correo;
    private String password;
    private String estatus;
    private boolean verificado;
    private String token;

    public Usuario()
    {
    }
    public Usuario(int idUsuario, String nombreCompleto, String correo, String password, String estatus)
    {
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.password = password;
        this.estatus = estatus;
    }
    public int getIdUsuario()
    {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario)
    {
        this.idUsuario = idUsuario;
    }
    public String getNombreCompleto()
    {
        return nombreCompleto;
    }
    public void setNombreCompleto(String nombreCompleto)
    {
        this.nombreCompleto = nombreCompleto;
    }
    public String getCorreo()
    {
        return correo;
    }
    public void setCorreo(String correo)
    {
        this.correo = correo;
    }
    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    public String getEstatus()
    {
        return estatus;
    }
    public void setEstatus(String estatus)
    {
        this.estatus = estatus;
    }
    public boolean isVerificado()
    {
        return verificado;
    }
    public void setVerificado(boolean verificado)
    {
        this.verificado = verificado;
    }
    public String getToken()
    {
        return token;
    }
    public void setToken(String token)
    {
        this.token = token;
    }
}