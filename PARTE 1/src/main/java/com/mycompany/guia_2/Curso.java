
package com.mycompany.guia_2;

public class Curso {

    private int id;
    private String nombre;
    private String docente;
    private int creditos;

    public Curso() {
    }

    public Curso(
            int id,
            String nombre,
            String docente,
            int creditos) {

        this.id = id;
        this.nombre = nombre;
        this.docente = docente;
        this.creditos = creditos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocente() {
        return docente;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    @Override
    public String toString() {

        return id + " | " + nombre + " | " + docente + " | " + creditos + " créditos";
    }
}
