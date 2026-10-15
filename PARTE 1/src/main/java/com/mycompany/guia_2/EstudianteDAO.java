package com.mycompany.guia_2;

import java.util.List;

public interface EstudianteDAO {

    void insertar(Estudiante e);
    Estudiante buscar(int id);
    List<Estudiante> listar();
    List<Estudiante> buscarPorCarrera(String carrera);
    boolean actualizar(Estudiante e);
    boolean eliminar(int id);
}

