
package com.peea.mx.FF.utils;

import java.sql.*;

public class conexion 
{
    private Connection conect;
    private String user;
    private String password;
    private String path;
    private String bd;
    
    public conexion()
    {
        //"jdbc:sqlserver://192.168.0.4:1433;database=TESTE;user=sa;password=Peea2015";
        //this(null, "peea", "automat12", "jdbc:mysql://192.168.0.3:3312/", null);
        this(null, "sa", "Peea2015", "jdbc:sqlserver://192.168.0.12:1433;", null);
        this.bd="SAE60Empre01";
    }
        public conexion(String place)
    {
        this(null, "peea", "automat12", "jdbc:mysql://"+place+"/", null);
    }

    public conexion(String user, String password, String path) {
        
        this(null, user, password, "jdbc:mysql://"+path+"/", null);
    }
        

    public conexion(Connection conect, String user, String password, String path, String bd)
    {
        this.conect = conect;
        this.user = user;
        this.password = password;
        this.path = path;
        this.bd = bd;
    }

    public Connection getConect() 
    {
        return conect;
    }

    public void setConect(Connection conect) 
    {
        this.conect = conect;
    }

    public String getUser() 
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getPassword() 
    {
        return password;
    }

    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPath() 
    {
        return path;
    }

    public void setPath(String path) 
    {
        this.path = path;
    }

    public String getBd() 
    {
        return bd;
    }

    public void setBd(String bd) 
    {
        this.bd = bd;
    }
    
    private void cargarDriver() throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        Class.forName("org.gjt.mm.mysql.Driver").newInstance();
    }
    
    public void abrirConexion() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        //cargarDriver();
        
        String connectionUtl=path+";database="+bd +";user="+user +";password="+password;
        conect = DriverManager.getConnection(connectionUtl);
        System.out.println("\n Conexión exitosa a la base de datos: " + this.bd);
    }
    
    public void cerrarConexion() throws SQLException
    {
        conect.close();
        System.out.println("\n Conexion de la base de datos: " + this.bd + " cerrada ");
    }
    
    public void destruir()
    {
        this.conect = null;
        this.user = null;
        this.password = null;
        this.path = null;
        this.bd = null;
        System.gc();
    }
    
    @Override
    public String toString()
    {
        return "Conexión: " + conect + "\n"
                + "Usuario: " + user + "\n"
                + "Contraseña: " + password + "\n"
                + "Dirección URL: " + path + "\n"
                + "Base de datos: " + bd + "\n";
    }

}
