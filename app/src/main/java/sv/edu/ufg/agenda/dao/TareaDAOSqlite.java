package sv.edu.ufg.agenda.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sv.edu.ufg.agenda.modelo.Tarea;

public class TareaDAOSqlite implements TareaDAO{
    private final SQLiteDatabase db;

    public TareaDAOSqlite(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public void insertar(Tarea entidad) {
        ContentValues valores = new ContentValues();
        valores.put("titulo", entidad.getTitulo());
        valores.put("prioridad", entidad.getPrioridad());

        long id = db.insert("tareas", null, valores);

        if (id != -1) {
            entidad.setId((int) id);
        }
    }

    @Override
    public Tarea buscar(int id) {
        try (Cursor cursor = db.query(
                "tareas",
                new String[]{"id", "titulo", "prioridad"},
                "id = ?",
                new String[]{String.valueOf(id)},
                null, null, null)) {

            if (cursor.moveToFirst()) {
                Tarea tarea = new Tarea();
                tarea.setId(cursor.getInt(0));
                tarea.setTitulo(cursor.getString(1));
                tarea.setPrioridad(cursor.getString(2));
                return tarea;
            }
        }

        return null;
    }

    @Override
    public List<Tarea> listar() {
        List<Tarea> lista = new ArrayList<>();

        try (Cursor cursor = db.query(
                "tareas",
                new String[]{"id", "titulo", "prioridad"},
                null, null, null, null, "id DESC")) {

            while (cursor.moveToNext()) {
                Tarea tarea = new Tarea();
                tarea.setId(cursor.getInt(0));
                tarea.setTitulo(cursor.getString(1));
                tarea.setPrioridad(cursor.getString(2));
                lista.add(tarea);
            }
        }

        return lista;
    }

    @Override
    public boolean actualizar(Tarea entidad) {
        ContentValues valores = new ContentValues();
        valores.put("titulo", entidad.getTitulo());
        valores.put("prioridad", entidad.getPrioridad());

        return db.update(
                "tareas",
                valores,
                "id = ?",
                new String[]{String.valueOf(entidad.getId())}
        ) > 0;
    }

    @Override
    public boolean eliminar(int id) {
        return db.delete(
                "tareas",
                "id = ?",
                new String[]{String.valueOf(id)}
        ) > 0;
    }

    @Override
    public List<Tarea> listarPorMateria(int materiaId) {
        List<Tarea> resultado = new ArrayList<>();

        try (Cursor cursor = db.query(
                "tareas",
                new String[]{
                        "id",
                        "materia_id",
                        "titulo",
                        "fecha_entrega",
                        "completada",
                        "prioridad"
                },
                "materia_id = ?",
                new String[]{String.valueOf(materiaId)},
                null,
                null,
                "fecha_entrega ASC")) {

            while (cursor.moveToNext()) {
                Tarea tarea = new Tarea();

                tarea.setId(cursor.getInt(0));
                tarea.setMateriaId(cursor.getInt(1));
                tarea.setTitulo(cursor.getString(2));
                tarea.setFechaEntrega(cursor.getString(3));
                tarea.setCompletada(cursor.getInt(4) == 1);
                tarea.setPrioridad(cursor.getString(5));

                resultado.add(tarea);
            }
        }
        return resultado;
    }

    @Override
    public List<Tarea> buscarPorTitulo(String texto) {
        List<Tarea> lista = new ArrayList<>();

        try (Cursor cursor = db.query(
                "tareas",
                new String[]{"id", "titulo", "prioridad"},
                "titulo LIKE ?",
                new String[]{"%" + texto + "%"},
                null, null, "titulo ASC")) {

            while (cursor.moveToNext()) {
                Tarea tarea = new Tarea();
                tarea.setId(cursor.getInt(0));
                tarea.setTitulo(cursor.getString(1));
                tarea.setPrioridad(cursor.getString(2));
                lista.add(tarea);
            }
        }

        return lista;
    }

    @Override
    public int contarPendientesPorMateria(int materiaId) {
        int contador = 0;

        try (Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM tareas " +
                        "WHERE materia_id = ? AND completada = 0",
                new String[]{String.valueOf(materiaId)})) {

            if (cursor.moveToFirst()) {
                contador = cursor.getInt(0);
            }
        }
        return contador;
    }
}
