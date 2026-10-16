
package com.mycompany.guia_2;

import com.google.gson.reflect.TypeToken;
import java.util.List;

public class CursoDAOJson extends JsonDAOBase<Curso> implements CursoDAO {

    public CursoDAOJson(String rutaArchivo) {
        // Pasamos la ruta y el token de tipo List<Curso> superando las limitaciones de los genéricos de Java
        super(rutaArchivo, new TypeToken<List<Curso>>() {}.getType());
    }

    @Override
    public void insertar(Curso c) {
        List<Curso> lista = cargar();
        c.setId(siguienteId(lista));
        lista.add(c);
        guardar(lista);
    }

    @Override
    public Curso buscar(int id) {
        for (Curso c : cargar()) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    @Override
    public List<Curso> listar() {
        return cargar();
    }

    @Override
    public boolean actualizar(Curso nuevo) {
        List<Curso> lista = cargar();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == nuevo.getId()) {
                lista.set(i, nuevo);
                guardar(lista);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        List<Curso> lista = cargar();
        boolean quitado = lista.removeIf(c -> c.getId() == id);
        if (quitado) guardar(lista);
        return quitado;
    }

    private int siguienteId(List<Curso> lista) {
        int mayor = 0;
        for (Curso c : lista) {
            if (c != null) mayor = Math.max(mayor, c.getId());
        }
        return mayor + 1;
    }
}
