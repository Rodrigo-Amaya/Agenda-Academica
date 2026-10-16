package com.ufg.dao.dao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class JsonDAOBase<T> {
    protected final Path archivo;
    protected final Gson gson;
    private final Type tipoLista; // Permite a Gson saber el tipo exacto (List<T>)

    public JsonDAOBase(String rutaArchivo, Type tipoLista) {
        this.archivo = Path.of(rutaArchivo);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.tipoLista = tipoLista;
        inicializarArchivo();
    }

    private void inicializarArchivo() {
        if (!Files.exists(archivo)) {
            try {
                Files.createDirectories(archivo.getParent());
                Files.writeString(archivo, "[]"); 
            } catch (IOException e) {
                throw new RuntimeException("No se pudo inicializar el archivo: " + archivo, e);
            }
        }
    }

  // Metodos reutilizables
    protected List<T> cargar() {
        try {
            String json = Files.readString(archivo);
            List<T> lista = gson.fromJson(json, tipoLista);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar los datos desde " + archivo.getFileName(), e);
        }
    }

    protected void guardar(List<T> lista) {
        try {
            String json = gson.toJson(lista);
            Files.writeString(archivo, json);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar los datos en " + archivo.getFileName(), e);
        }
    }
}
