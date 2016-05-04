/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ltejada
 */
public class Estudiante implements Comparable<Estudiante>{
    
    
    private int matricula;
    private String nombre;

    private  int ID=1;
    private String apellido;
    private String telefono;

    public int getMyId() {
        return myId;
    }
    private  int myId=ID;
    
    public Estudiante(int matricula, String nombre, String apellido, String telefono) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    //    ID++;
    }
    
    public Estudiante(int matricula, String nombre, String apellido) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.apellido = apellido;
      //  ID++;
    }
    
    public Estudiante(){
        
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono==null?"":telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
      public String getMatriculaString()
      {
          return (matricula+"").replace(",","");
      }
   
      
      public void setId(int id){
          this.myId=id;
      }
      
      public PreparedStatement generarInsertSQL(PreparedStatement prepareStatement,Connection conexion){
          String SQL="INSERT INTO ESTUDIANTES ( ID , MATRICULA , NOMBRE , APELLIDO , TELEFONO ) VALUES ( ifnull((select max(id) from estudiantes),0)+1,?,?,?,?);";
        try {
            prepareStatement = conexion.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            //prepareStatement.setInt(1, ID);
            prepareStatement.setInt(1, matricula);
            prepareStatement.setString(2, nombre);
            prepareStatement.setString(3, apellido);
            prepareStatement.setString(4, telefono);
        } catch (SQLException ex) {
            
            Logger.getLogger(Estudiante.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
          
          return prepareStatement;
      }

      public PreparedStatement generarDeleteSQL(PreparedStatement prepareStatement,Connection conexion){
          String SQL="delete from ESTUDIANTES where id=?;";
        try {
            prepareStatement = conexion.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            //prepareStatement.setInt(1, ID);
            prepareStatement.setInt(1, myId);
            
        } catch (SQLException ex) {
            
            Logger.getLogger(Estudiante.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
          
          return prepareStatement;
      }

      
      
    @Override
    public int compareTo(Estudiante o) {
       if(myId>o.getMyId()) return 1;
       if(myId<o.getMyId()) return -1;
       return 0;
    }
 
      
      
      
}
