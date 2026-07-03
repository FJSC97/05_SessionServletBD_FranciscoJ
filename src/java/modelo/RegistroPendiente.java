package modelo;

import java.io.Serializable;

public class RegistroPendiente implements Serializable
{
    private String nombreCompleto;
    private String correo;
    private String password;   
    private String codigo;
    private long generadoEn;   

    public RegistroPendiente()
    {
    }

    public RegistroPendiente(String nombreCompleto, String correo, String password, String codigo)
    {
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.password = password;
        this.codigo = codigo;
        this.generadoEn = System.currentTimeMillis();
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

    public String getCodigo()
    {
        return codigo;
    }

    public void setCodigo(String codigo)
    {
        this.codigo = codigo;
    }

    public long getGeneradoEn()
    {
        return generadoEn;
    }

    public void setGeneradoEn(long generadoEn)
    {
        this.generadoEn = generadoEn;
    }
}