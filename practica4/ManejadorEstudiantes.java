/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica4;

import freemarker.template.Configuration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.ModelAndView;
import static spark.Spark.get;
import static spark.Spark.staticFileLocation;
import spark.template.freemarker.FreeMarkerEngine;

/**
 *
 * @author ltejada
 */
public class ManejadorEstudiantes {

    private List<Estudiante> misEstudiantes = new ArrayList<>();

    public ManejadorEstudiantes() {
        staticFileLocation("/resources");
       /* Estudiante e = new Estudiante(20062123, "Liz", "Tejada","8295683522");
        Estudiante e2 = new Estudiante(20062124, "Lourdez Nairobi", "Fernandez Mateo","8298100719");
        misEstudiantes.add(e);
        misEstudiantes.add(e2);*/
       misEstudiantes=EstudiantesServices.listaEstudiantes();

               Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setClassForTemplateLoading(ManejadorEstudiantes.class, "/resources/");
        FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine(configuration);

        get("/", (req, res) -> {
            res.redirect("/verEstudiantes");
            return "";
        });

        get("/verEstudiantes", (req, res) -> {

            Map<String, Object> attributes = new HashMap<>();
            Collections.sort(misEstudiantes);
            attributes.put("estudiantes", misEstudiantes);

            //enviando los parametros a la vista.
            return new ModelAndView(attributes, "VerEstudiantes.ftl");

        }, freeMarkerEngine);

        get("/verEstudiante", (req, res) -> {

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("estudiante", misEstudiantes.get(0));

            //enviando los parametros a la vista.
            return new ModelAndView(attributes, "VerEstudiante.ftl");

        }, freeMarkerEngine);

        get("/EditarEstudiante", (req, res) -> {

            Map<String, Object> attributes = new HashMap<>();

            Estudiante temp = null;
            String nuevo="false";
            String ver=req.queryParams("ver")==null?"false":req.queryParams("ver");
            try{
                nuevo=req.queryParams("nuevo");
            if (!Boolean.valueOf(nuevo)) {
                for (Estudiante e1 : misEstudiantes) {
                    if (e1.getMyId() == Integer.parseInt(req.queryParams("idEstudiante"))) {
                        temp = e1;
                        break;
                    }
                }

            }
            }catch(Exception e1){
                nuevo="false";
            }
            attributes.put("estudiante", temp);
            attributes.put("nuevo", nuevo);
            attributes.put("ver", ver);
            if (temp == null&&!Boolean.valueOf(req.queryParams("nuevo"))) {
                attributes.put("mensaje", "EL id " + req.queryParams("idEstudiante") + " no es valido");
                // return "EL id "+req.queryParams("idEstudiante")+" no es valido";
                return new ModelAndView(attributes, "error.ftl");
            }
            //enviando los parametros a la vista.
            return new ModelAndView(attributes, "ActualizarEstudiante.ftl");

        }, freeMarkerEngine);

        get("/GuardarCambiosEstudiante", (req, res) -> {

            Map<String, Object> attributes = new HashMap<>();

            Estudiante temp = null;
            for (Estudiante e1 : misEstudiantes) {
                if (e1.getMyId() == Integer.parseInt(req.queryParams("idEstudiante"))) {
                    
                    
                    temp = new Estudiante(e1.getMatricula(), e1.getNombre(), e1.getApellido(),e1.getTelefono());
                    temp.setId(e1.getMyId());
                    temp.setApellido(req.queryParams("apellido"));
                    temp.setTelefono(req.queryParams("telefono"));
                    temp.setNombre(req.queryParams("nombre"));
                    temp.setMatricula(Integer.parseInt(req.queryParams("matricula")));
                    
                    if(EstudiantesServices.actualizarEstudiante(temp)){
                       
                       misEstudiantes.remove(e1);
                       e1=temp;
                       misEstudiantes.add(e1);
                       int r=1;
                    }else{
                        temp=null;
                    }
                    
                    
                    break;
                }
            }
           
            if (temp == null) {
                attributes.put("mensaje", "EL id " + req.queryParams("idEstudiante") + " no es valido");
                // return "EL id "+req.queryParams("idEstudiante")+" no es valido";
                return new ModelAndView(attributes, "error.ftl");
            }
 attributes.put("estudiante", temp);
            

            attributes.put("estudiantes", misEstudiantes);
            res.redirect("/verEstudiantes");
            //enviando los parametros a la vista.
            return new ModelAndView(attributes, "VerEstudiantes.ftl");

        }, freeMarkerEngine);

        get("/BorrarEstudiantes", (req, res) -> {

            Map<String, Object> attributes = new HashMap<>();

            Estudiante temp = null;
            boolean borrado = false;
            for (Estudiante e1 : misEstudiantes) {
                if (e1.getMyId() == Integer.parseInt(req.queryParams("idEstudiante"))) {
                    temp = e1;
                    attributes.put("nombreEstudiante", temp.getNombre() + " " + temp.getApellido());

                    try {
                            temp = new Estudiante(e1.getMatricula(), e1.getNombre(), e1.getApellido(),e1.getTelefono());
                        temp.setId(e1.getMyId());
                        

                        if(EstudiantesServices.borrarEstudiante(temp)){

                           misEstudiantes.remove(e1);
                            borrado=true;
                        }else{
                            temp=null;
                        }
                        
                    } catch (Exception e22) {
                        borrado = false;
                    }
                    break;
                }
            }

            if (temp == null) {
                attributes.put("mensaje", "EL id " + req.queryParams("idEstudiante") + " no es valido");
                // return "EL id "+req.queryParams("idEstudiante")+" no es valido";
                return new ModelAndView(attributes, "error.ftl");
            }
            attributes.put("borrado", borrado);
            //enviando los parametros a la vista.
            return new ModelAndView(attributes, "BorrarEstudiante.ftl");

        }, freeMarkerEngine);

        get("/agregarEstudiante", (req, res) -> {

            Map<String, Object> attributes = new HashMap<>();

            boolean borrado = false;
            for (Estudiante e1 : misEstudiantes) {
                if (e1.getMatricula() == Integer.parseInt(req.queryParams("matricula"))) {

                    attributes.put("mensaje", "La matricula " + req.queryParams("matricula") + " Ya está siendo utilizada por el  estudiante: " + e1.getNombre() + " " + e1.getApellido());
                    // return "EL id "+req.queryParams("idEstudiante")+" no es valido";
                    return new ModelAndView(attributes, "error.ftl");

                }
            }

            try {
                Estudiante nuevo = new Estudiante(Integer.parseInt(req.queryParams("matricula")), req.queryParams("nombre"), req.queryParams("apellido"), req.queryParams("telefono"));
                if(EstudiantesServices.insertarEstudiante(nuevo)){
                    misEstudiantes.add(nuevo);
                    attributes.put("agregado", true);
                    attributes.put("nombreEstudiante", nuevo.getNombre() + " " + nuevo.getApellido());
                    return new ModelAndView(attributes, "EstudianteAgregado.ftl");
                }else{
                    attributes.put("agregado", false);
                     attributes.put("mensaje", "El estudiante no pudo ser agregado");
                    return new ModelAndView(attributes, "error.ftl");
                }
                
                
            } catch (Exception exc) {
                attributes.put("agregado", false);
                return new ModelAndView(attributes, "EstudianteAgregado.ftl");
            }

        }, freeMarkerEngine);
    }

    public static void main(String[] args) {
        new ManejadorEstudiantes();
    }
}
