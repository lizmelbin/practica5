/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ltejada
 */
public class EstudiantesServices {
    
    
            public     static String DRIVER = "org.gjt.mm.mysql.Driver";

    //public static String Connection="jdbc:mysql://192.168.1.2/cencarci1";  
    //public static String Connection="jdbc:mysql://10.0.0.131/cencarci1";
      public static String Connection="jdbc:h2:tcp://localhost/~/test";
      public static String user="sa";
      public static String password="";
      
      


    public static  boolean insertarEstudiante(Estudiante es) {
        boolean respuesta = false;
        Connection conexion=null;
           PreparedStatement update=null;
        try {
            Class.forName("org.h2.Driver");
                conexion =
                        DriverManager.getConnection
                                        (Connection,user,password);
               
                
                update=es.generarInsertSQL(update, conexion);
                
                if(update==null) return false;
                update.executeUpdate();
                 
                 
                es.setId(getEstudiante(es.getMatricula()).getMyId());
                conexion.close();
                //l.Guardar_logs_en_la_BD(Sentencia,FuncionesGenerales.Fecha()+" "+FuncionesGenerales.Hora()                        , ""+mysesion, "INSERT");
                return true;
            
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            
            try {
                conexion.close();
            } catch (SQLException ex) {
                Logger.getLogger(EstudiantesServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return respuesta;
               
    }

    public static Estudiante getEstudiante(int matricula) {
        Estudiante est = null;
        Connection con = null;        
        try {
            
            String query = "select * from estudiantes where matricula = ?";
            con = getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setInt(1, matricula);
            //Ejecuto...
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                est = new Estudiante();
                est.setMatricula(rs.getInt("matricula"));
                est.setNombre(rs.getString("nombre"));
                est.setApellido(rs.getString("apellido"));
                    est.setId(rs.getInt("id"));
                est.setTelefono(rs.getString("telefono"));               
                
            }           
            
        } catch (SQLException ex) {
            Logger.getLogger(EstudiantesServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(EstudiantesServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return est;
    }
    
    
    
    public static List<Estudiante> listaEstudiantes() {
        List<Estudiante> lista = new ArrayList<>();
        Connection con = null;        
        try {
            
            String query = "select * from estudiantes";
            con = getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            ResultSet rs = prepareStatement.executeQuery();
            while(rs.next()){
                Estudiante est = new Estudiante();
                est.setMatricula(rs.getInt("matricula"));
                est.setNombre(rs.getString("nombre"));
                est.setApellido(rs.getString("apellido"));
                est.setId(rs.getInt("id"));
                est.setTelefono(rs.getString("telefono"));
                
                lista.add(est);
            }           
            
        } catch (SQLException ex) {
            Logger.getLogger(EstudiantesServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(EstudiantesServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return lista;
    }

    private static Connection getConexion() {
                try {
                    return DriverManager.getConnection(Connection,user,password);
                } catch (SQLException ex) {
                    return  null;
                }
    }
    
    
    
    public static boolean actualizarEstudiante(Estudiante est){
        boolean ok =false;
        
         Connection con = null;        
        try {
            
            String query = "update estudiantes set nombre=?, apellido=?,  telefono=? where id = ?";
            con = getConexion();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.            
            prepareStatement.setString(1, est.getNombre());
            prepareStatement.setString(2, est.getApellido());
            prepareStatement.setString(3, est.getTelefono());
           
            //Indica el where...
            prepareStatement.setInt(4, est.getMyId());
            //
            int fila = prepareStatement.executeUpdate();
            fila= prepareStatement.getUpdateCount();
            ok = fila > 0 ;            
            
        } catch (SQLException ex) {
            Logger.getLogger(EstudiantesServices.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(EstudiantesServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return ok;
    }
    
    
    
     public static  boolean borrarEstudiante(Estudiante es) {
        boolean respuesta = false;
        Connection conexion=null;
           PreparedStatement update=null;
        try {
            Class.forName("org.h2.Driver");
                conexion =
                        DriverManager.getConnection
                                        (Connection,user,password);
               
                
                update=es.generarDeleteSQL(update, conexion);
                
                if(update==null) return false;
                update.executeUpdate();
                 
                 
        
                conexion.close();
                //l.Guardar_logs_en_la_BD(Sentencia,FuncionesGenerales.Fecha()+" "+FuncionesGenerales.Hora()                        , ""+mysesion, "INSERT");
                return true;
            
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            
            try {
                conexion.close();
            } catch (SQLException ex) {
                Logger.getLogger(EstudiantesServices.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return respuesta;
               
    }
}
