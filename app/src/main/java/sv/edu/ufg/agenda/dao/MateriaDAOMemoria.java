package sv.edu.ufg.agenda.dao;

import java.util.ArrayList;
import java.util.List;
import sv.edu.ufg.agenda.modelo.Materia;
public class MateriaDAOMemoria implements MateriaDAO {
    private final List<Materia> datos = new ArrayList<>();
    private int siguienteId = 1;

    @Override
    public void insertar(Materia entidad) {
        entidad.setId(siguienteId++);
        datos.add(entidad);
    }

    @Override
    public Materia buscar(int id) {
        for (Materia m : datos) {
            if (m.getId() == id) return m;
        }
        return null;
    }

    @Override
    public List<Materia> listar() {
        return new ArrayList<>(datos);
    }

    @Override
    public boolean actualizar(Materia entidad) {
        for (int i = 0; i < datos.size(); i++) {
            if (datos.get(i).getId() == entidad.getId()) {
                datos.set(i, entidad);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        return datos.removeIf(m -> m.getId() == id);
    }

    @Override
    public List<Materia> buscarPorNombre(String texto) {
        List<Materia> resultado = new ArrayList<>();
        if (texto == null) return resultado;

        String filtro = texto.trim().toLowerCase();
        for (Materia m : datos) {
            if (m.getNombre() != null
                    && m.getNombre().toLowerCase().contains(filtro)) {
                resultado.add(m);
            }
        }
        return resultado;
    }
}
