package sv.edu.ufg.agenda;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.List;

import sv.edu.ufg.agenda.dao.TareaDAOJson;
import sv.edu.ufg.agenda.modelo.Tarea;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TareaDAOJsonTest {
    @Rule
    public TemporaryFolder carpetaTemporal = new TemporaryFolder();

    @Test
    public void insertarListarPorMateriaYEliminar() throws Exception {
        // Crear un archivo JSON temporal para la prueba
        File archivo = carpetaTemporal.newFile("tareas.json");

        TareaDAOJson dao = new TareaDAOJson(archivo);

        // Crear e insertar tareas de materias diferentes
        Tarea tarea1 = new Tarea(
                1, 10, "Estudiar matematicas", "2026-11-10", "Alta"
        );

        Tarea tarea2 = new Tarea(
                2, 20, "Leer historia", "2026-11-12", "Media"
        );

        dao.insertar(tarea1);
        dao.insertar(tarea2);

        // Verificar que listarPorMateria devuelve solo la tarea correcta
        List<Tarea> resultado = dao.listarPorMateria(10);

        assertEquals(1, resultado.size());
        assertEquals("Estudiar matematicas", resultado.get(0).getTitulo());
        assertEquals(10, resultado.get(0).getMateriaId());

        // Eliminar la tarea insertada
        boolean eliminada = dao.eliminar(tarea1.getId());

        assertTrue(eliminada);

        // Verificar que ya no aparece en la materia 10
        List<Tarea> despuesDeEliminar = dao.listarPorMateria(10);

        assertTrue(despuesDeEliminar.isEmpty());

        // La tarea de la materia 20 debe seguir existiendo
        List<Tarea> otraMateria = dao.listarPorMateria(20);

        assertEquals(1, otraMateria.size());
        assertEquals("Leer historia", otraMateria.get(0).getTitulo());
    }
}
