package sv.edu.ufg.agenda.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sv.edu.ufg.agenda.AgendaApp;
import sv.edu.ufg.agenda.R;
import sv.edu.ufg.agenda.dao.DAOFactory;
import sv.edu.ufg.agenda.dao.MateriaDAO;
import sv.edu.ufg.agenda.dao.TareaDAO;
import sv.edu.ufg.agenda.modelo.Materia;


public class MateriasActivity extends AppCompatActivity implements MateriaAdapter.Acciones {

    private MateriaDAO materiaDAO;
    private TareaDAO tareaDAO;
    private MateriaAdapter adapter;
    private EditText etNombre;
    private EditText etDocente;
    private EditText etBuscar;
    private Button btnGuardar;
    private Materia enEdicion;   // null = se está agregando una materia nueva

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materias);

        DAOFactory daos = ((AgendaApp) getApplication()).daos();
        materiaDAO = daos.materias();
        tareaDAO = daos.tareas();

        etNombre = findViewById(R.id.etNombre);
        etDocente = findViewById(R.id.etDocente);
        etBuscar = findViewById(R.id.etBuscarMateria);
        btnGuardar = findViewById(R.id.btnGuardar);

        adapter = new MateriaAdapter(this);
        RecyclerView lista = findViewById(R.id.rvMaterias);
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(adapter);

        btnGuardar.setOnClickListener(v -> guardar());
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrar(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
        refrescar();
    }

    private void filtrar(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            mostrarConPendientes(materiaDAO.listar());
        } else {
            mostrarConPendientes(materiaDAO.buscarPorNombre(texto.trim()));
        }

    }
    private void guardar() {
        String nombre = etNombre.getText().toString().trim();
        String docente = etDocente.getText().toString().trim();
        if (nombre.isEmpty()) {
            etNombre.setError("El nombre es obligatorio");
            return;
        }
        if (enEdicion == null) {
            materiaDAO.insertar(new Materia(0, nombre, docente));
        } else {
            enEdicion.setNombre(nombre);
            enEdicion.setDocente(docente);
            materiaDAO.actualizar(enEdicion);
        }
        limpiarFormulario();
        refrescar();
    }

    private void limpiarFormulario() {
        enEdicion = null;
        etNombre.setText("");
        etDocente.setText("");
        btnGuardar.setText("Agregar materia");
    }

    private void refrescar() {
        mostrarConPendientes(materiaDAO.listar());
    }

    @Override
    public void editar(Materia materia) {
        enEdicion = materia;
        etNombre.setText(materia.getNombre());
        etDocente.setText(materia.getDocente());
        btnGuardar.setText("Actualizar materia");
    }

    @Override
    public void verTareas(Materia materia) {
        Intent intento = new Intent(this, TareasActivity.class);
        intento.putExtra("materiaId", materia.getId());
        startActivity(intento);
    }

    @Override
    public void eliminar(Materia materia) {
        if (!tareaDAO.listarPorMateria(materia.getId()).isEmpty()) {
            Toast.makeText(this, "No se puede eliminar: la materia tiene tareas",
                    Toast.LENGTH_LONG).show();
            return;
        }
        materiaDAO.eliminar(materia.getId());
        if (enEdicion != null && enEdicion.getId() == materia.getId()) {
            limpiarFormulario();
        }
        refrescar();
    }
    private void mostrarConPendientes(List<Materia> materias) {
        Map<Integer, Integer> pendientes = new HashMap<>();
        for (Materia m : materias) {
            pendientes.put(m.getId(), tareaDAO.contarPendientesPorMateria(m.getId()));
        }
        adapter.mostrar(materias, pendientes);

    }
}