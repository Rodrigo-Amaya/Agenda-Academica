
package com.mycompany.guia_2;

import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAOJson extends JsonDAOBase<Estudiante> implements EstudianteDAO{

    public EstudianteDAOJson(String rutaArchivo) {
        super(rutaArchivo, new TypeToken<List<Estudiante>>() {}.getType());
    }
    
    // ---------- Operaciones del contrato ----------

    @Override
    public void insertar(Estudiante e) {
        List<Estudiante> lista = cargar();
        e.setId(siguienteId(lista));
        lista.add(e);
        guardar(lista);
    }

    @Override
    public Estudiante buscar(int id) {
        for (Estudiante e : cargar()) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }
    
    @Override
    public List<Estudiante> buscarPorCarrera(String carrera) {
        List<Estudiante> estudiantesFiltrados = new ArrayList<>();
    
        for (Estudiante e : cargar()) {
            if (e.getCarrera().equalsIgnoreCase(carrera)) {
                estudiantesFiltrados.add(e);
            }
        }

        return estudiantesFiltrados;
    }

    @Override
    public List<Estudiante> listar() {
        return cargar();
    }

    @Override
    public boolean actualizar(Estudiante nuevo) {
        List<Estudiante> lista = cargar();
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
        List<Estudiante> lista = cargar();
        boolean quitado = lista.removeIf(e -> e.getId() == id);
        if (quitado) {
            guardar(lista);
        }
        return quitado;
    }


    private int siguienteId(List<Estudiante> lista) {
        int mayor = 0;
        for (Estudiante e : lista) {
            mayor = Math.max(mayor, e.getId());
        }
        return mayor + 1;
    }
}
