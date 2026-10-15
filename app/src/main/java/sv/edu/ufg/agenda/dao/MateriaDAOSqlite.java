package sv.edu.ufg.agenda.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

import sv.edu.ufg.agenda.modelo.Materia;

public class MateriaDAOSqlite implements MateriaDAO {
    private final SQLiteDatabase db;

    public MateriaDAOSqlite(SQLiteDatabase db) {
        this.db = db;
    }

    // --- Operaciones del contrato ---
    @Override
    public void insertar(Materia entidad){
        ContentValues valores = new ContentValues();
        valores.put("nombre",entidad.getNombre());

        long id = db.insert("materias", null, valores);

        if (id == -1) {
            throw new IllegalStateException("No se pudo insertar la materia");
        }

        entidad.setId((int) id);
    }

    @Override
    public Materia buscar(int id){
        try (Cursor cursor = db.query(
                "materias",
                new String[]{"id", "nombre"},
                "id = ?",
                new String[]{String.valueOf(id)},
                null, null, null)) {

            if (cursor.moveToFirst()) {
                Materia materia = new Materia();
                materia.setId(cursor.getInt(0));
                materia.setNombre(cursor.getString(1));
                return materia;
            }
        }
        return null;
    }

    @Override
    public List<Materia> listar(){
        List<Materia> lista = new ArrayList<>();

        try (Cursor cursor = db.query(
                "materias",
                new String[]{"id", "nombre"},
                null, null, null, null, "nombre ASC")) {

            while (cursor.moveToNext()) {
                Materia materia = new Materia();
                materia.setId(cursor.getInt(0));
                materia.setNombre(cursor.getString(1));
                lista.add(materia);
            }
        }
        return lista;
    }

    @Override
    public boolean actualizar(Materia entidad){
        ContentValues valores = new ContentValues();
        valores.put("nombre", entidad.getNombre());

        return db.update(
                "materias",
                valores,
                "id = ?",
                new String[]{String.valueOf(entidad.getId())}
        ) > 0;
    }

    @Override
    public boolean eliminar(int id){
        return db.delete(
                "materias",
                "id = ?",
                new String[]{String.valueOf(id)}
        ) > 0;
    }

    @Override
    public List<Materia> buscarPorNombre(String texto) {
        List<Materia> lista = new ArrayList<>();

        try (Cursor cursor = db.query(
                "materias",
                new String[]{"id", "nombre"},
                "nombre LIKE ?",
                new String[]{"%" + texto + "%"},
                null, null, "nombre ASC")) {

            while (cursor.moveToNext()) {
                Materia materia = new Materia();
                materia.setId(cursor.getInt(0));
                materia.setNombre(cursor.getString(1));
                lista.add(materia);
            }
        }
        return lista;
    }
}
