
package com.mycompany.guia_2;

import java.util.List;

public interface CursoDAO {

    void insertar(Curso c);

    Curso buscar(int id);

    List<Curso> listar();

    boolean actualizar(Curso c);

    boolean eliminar(int id);
}
