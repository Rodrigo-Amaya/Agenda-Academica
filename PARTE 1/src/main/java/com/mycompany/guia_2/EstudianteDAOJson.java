
package com.mycompany.guia_2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAOJson implements EstudianteDAO {

    private final Path archivo;

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private final Type tipoLista =
            new TypeToken<List<Estudiante>>() {}.getType();

    public EstudianteDAOJson(String rutaArchivo) {
        this.archivo = Path.of(rutaArchivo);
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
    public List<Estudiante> listar() {
        return cargar();
    }

    @Override
    public List<Estudiante> buscarPorCarrera(String carrera) {

        List<Estudiante> resultado = new ArrayList<>();

        for (Estudiante e : cargar()) {

            if (e.getCarrera().equalsIgnoreCase(carrera)) {
                resultado.add(e);
            }
        }

        return resultado;
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

        boolean quitado = lista.removeIf(
                e -> e.getId() == id
        );

        if (quitado) {
            guardar(lista);
        }

        return quitado;
    }

    // ---------- Métodos privados ----------

    private List<Estudiante> cargar() {

        try {

            if (Files.notExists(archivo)) {
                return new ArrayList<>();
            }

            String json = Files.readString(
                    archivo,
                    StandardCharsets.UTF_8
            );

            List<Estudiante> lista =
                    gson.fromJson(json, tipoLista);

            return lista != null
                    ? lista
                    : new ArrayList<>();

        } catch (IOException ex) {

            throw new RuntimeException(
                    "No se pudo leer " + archivo,
                    ex
            );
        }
    }

    private void guardar(List<Estudiante> lista) {

        try {

            Files.writeString(
                    archivo,
                    gson.toJson(lista, tipoLista),
                    StandardCharsets.UTF_8
            );

            System.out.println(
                    "JSON guardado en: "
                    + archivo.toAbsolutePath()
            );

        } catch (IOException ex) {

            throw new RuntimeException(
                    "No se pudo escribir " + archivo,
                    ex
            );
        }
    }

    private int siguienteId(List<Estudiante> lista) {

        int mayor = 0;

        for (Estudiante e : lista) {
            mayor = Math.max(mayor, e.getId());
        }

        return mayor + 1;
    }
}