package modelo;

public class Alumno
{
    private int NL;
    private String Nombre;
    private String Paterno;
    private String Materno;

    public Alumno()
    {
    }

    public Alumno(int NL, String Nombre, String Paterno, String Materno)
    {
        this.NL = NL;
        this.Nombre = Nombre;
        this.Paterno = Paterno;
        this.Materno = Materno;
    }

    public int getNL()
    {
        return NL;
    }

    public void setNL(int NL)
    {
        this.NL = NL;
    }

    public String getNombre()
    {
        return Nombre;
    }

    public void setNombre(String Nombre)
    {
        this.Nombre = Nombre;
    }

    public String getPaterno()
    {
        return Paterno;
    }

    public void setPaterno(String Paterno)
    {
        this.Paterno = Paterno;
    }

    public String getMaterno()
    {
        return Materno;
    }

    public void setMaterno(String Materno)
    {
        this.Materno = Materno;
    }
}