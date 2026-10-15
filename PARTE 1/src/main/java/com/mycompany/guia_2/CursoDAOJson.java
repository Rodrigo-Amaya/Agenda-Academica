
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

public class CursoDAOJson implements CursoDAO {

    private final Path archivo;

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private final Type tipoLista =
            new TypeToken<List<Curso>>() {}.getType();

    public CursoDAOJson(String rutaArchivo) {
        this.archivo = Path.of(rutaArchivo);
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

            if (c.getId() == id) {
                return c;
            }
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

        boolean quitado =
                lista.removeIf(
                        c -> c.getId() == id
                );

        if (quitado) {
            guardar(lista);
        }

        return quitado;
    }

    // ---------- cargar ----------

    private List<Curso> cargar() {

        try {

            if (Files.notExists(archivo)) {
                return new ArrayList<>();
            }

            String json =
                    Files.readString(
                            archivo,
                            StandardCharsets.UTF_8
                    );

            List<Curso> lista =
                    gson.fromJson(
                            json,
                            tipoLista
                    );

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

    // ---------- guardar ----------

    private void guardar(List<Curso> lista) {

        try {

            Files.writeString(
                    archivo,
                    gson.toJson(
                            lista,
                            tipoLista
                    ),
                    StandardCharsets.UTF_8
            );

            System.out.println(
                    "JSON guardado en: "
                            + archivo.toAbsolutePath()
            );

        } catch (IOException ex) {

            throw new RuntimeException(
                    "No se pudo escribir "
                            + archivo,
                    ex
            );
        }
    }

    // ---------- siguiente ID ----------

    private int siguienteId(List<Curso> lista) {

        int mayor = 0;

        for (Curso c : lista) {

            mayor = Math.max(
                    mayor,
                    c.getId()
            );
        }

        return mayor + 1;
    }
}
