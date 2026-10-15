package com.mycompany.guia_2;

public class Estudiante {
    private int id;
    private String nombre;
    private String carrera;
    //Se agrega correo
    private String correo;

    public Estudiante(int id, String nombre, String carrera, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.carrera = carrera;
        this.correo = correo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }
    //Metodos del correo
    public String getCorreo(){return correo;}
    public void setCorreo(String correo){this.correo = correo;}
    

    @Override
    public String toString() {
        return id + " | " + nombre + " | " + carrera + "|" + correo;
    }
}
