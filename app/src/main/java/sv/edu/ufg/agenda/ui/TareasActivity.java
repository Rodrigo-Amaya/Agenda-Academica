package sv.edu.ufg.agenda.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import sv.edu.ufg.agenda.AgendaApp;
import sv.edu.ufg.agenda.R;
import sv.edu.ufg.agenda.dao.DAOFactory;
import sv.edu.ufg.agenda.dao.MateriaDAO;
import sv.edu.ufg.agenda.dao.TareaDAO;
import sv.edu.ufg.agenda.modelo.Materia;
import sv.edu.ufg.agenda.modelo.Tarea;

import android.widget.Spinner;
import android.widget.ArrayAdapter;


public class TareasActivity extends AppCompatActivity  {

    private MateriaDAO materiaDAO;
    private TareaDAO tareaDAO;

    private int materiaId;
    private TextView tvNombreMateria;
    private TextView tvResumenTareas;
    private EditText etTituloTarea;
    private EditText etFechaEntrega;
    private Button btnGuardarTarea;

    private TareaAdapter adapter;
    private Tarea enEdicion;

    private Spinner spPrioridadTarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas);

        DAOFactory daos =
                ((AgendaApp) getApplication()).daos();

        materiaDAO = daos.materias();
        tareaDAO = daos.tareas();

        materiaId = getIntent().getIntExtra("materiaId", -1);

        Materia materia = materiaDAO.buscar(materiaId);

        if (materia == null) {
            Toast.makeText(this, "La materia no existe",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        tvNombreMateria = findViewById(R.id.tvNombreMateria);
        tvResumenTareas = findViewById(R.id.tvResumenTareas);
        etTituloTarea = findViewById(R.id.etTituloTarea);
        etFechaEntrega = findViewById(R.id.etFechaEntrega);
        btnGuardarTarea = findViewById(R.id.btnGuardarTarea);
        //Selector de prioridad
        spPrioridadTarea = findViewById(R.id.spPrioridadTarea);

        String[] prioridades = {"Alta", "Media", "Baja"};

        ArrayAdapter<String> prioridadAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                prioridades
        );

        prioridadAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spPrioridadTarea.setAdapter(prioridadAdapter);

        tvNombreMateria.setText("Tareas de: " + materia.getNombre());

        // Conectar la lista con las acciones de esta pantalla.
        adapter = new TareaAdapter(new AccionesTarea() {
            @Override
            public void editar(Tarea tarea) {
                cargarEdicion(tarea);
            }

            @Override
            public void eliminar(Tarea tarea) {
                eliminarTarea(tarea);
            }

            @Override
            public void cambiarCompletada(
                    Tarea tarea, boolean completada) {
                tarea.setCompletada(completada);
                tareaDAO.actualizar(tarea);
                refrescar();
            }
        });

        RecyclerView lista = findViewById(R.id.rvTareas);
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(adapter);

        btnGuardarTarea.setOnClickListener(v -> guardar());

        refrescar();
    }

    // Agregar o actualizar una tarea.
    private void guardar() {
        String titulo = etTituloTarea.getText()
                .toString().trim();

        String fecha = etFechaEntrega.getText()
                .toString().trim();

        if (titulo.isEmpty()) {
            etTituloTarea.setError("El título es obligatorio");
            etTituloTarea.requestFocus();
            return;
        }

        if (!fechaValida(fecha)) {
            etFechaEntrega.setError(
                    "Introduce una fecha real: aaaa-mm-dd");
            etFechaEntrega.requestFocus();
            return;
        }

        String prioridad = spPrioridadTarea.getSelectedItem().toString();

        if (enEdicion == null) {
            tareaDAO.insertar(
                    new Tarea(0, materiaId, titulo, fecha, prioridad));
        } else {
            enEdicion.setTitulo(titulo);
            enEdicion.setFechaEntrega(fecha);
            enEdicion.setPrioridad(prioridad);

            tareaDAO.actualizar(enEdicion);
        }

        limpiarFormulario();
        refrescar();
    }

    // Cargar los datos de una tarea para editarla.
    private void cargarEdicion(Tarea tarea) {
        enEdicion = tarea;

        etTituloTarea.setText(tarea.getTitulo());
        etFechaEntrega.setText(tarea.getFechaEntrega());

        String prioridad = tarea.getPrioridad();
        if (prioridad == null) {
            prioridad = "Media";
        }

        int posicion = 1; // Media por defecto

        if (prioridad.equals("Alta")) {
            posicion = 0;
        } else if (prioridad.equals("Baja")) {
            posicion = 2;
        }

        spPrioridadTarea.setSelection(posicion);

        btnGuardarTarea.setText("Actualizar tarea");
    }

    private void limpiarFormulario() {
        enEdicion = null;

        etTituloTarea.setText("");
        etFechaEntrega.setText("");

        etTituloTarea.setError(null);
        etFechaEntrega.setError(null);

        spPrioridadTarea.setSelection(1);

        btnGuardarTarea.setText("Agregar tarea");
    }

    // Eliminar una tarea.
    private void eliminarTarea(Tarea tarea) {
        tareaDAO.eliminar(tarea.getId());

        if (enEdicion != null
                && enEdicion.getId() == tarea.getId()) {
            limpiarFormulario();
        }

        refrescar();

        Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
    }

    // Actualizar lista, ordenar por fecha y calcular el resumen.
    private void refrescar() {
        List<Tarea> tareas = tareaDAO.listarPorMateria(materiaId);

        Collections.sort(tareas, (a, b) -> a.getFechaEntrega().compareTo(b.getFechaEntrega()));

        adapter.mostrar(tareas);

        int completadas = 0;

        for (Tarea tarea : tareas) {
            if (tarea.isCompletada()) {
                completadas++;
            }
        }

        tvResumenTareas.setText(completadas + " de " + tareas.size() + " completadas");
    }

    // Comprobar formato y existencia real de la fecha.
    private boolean fechaValida(String texto) {
        if (!texto.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return false;
        }

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        formato.setLenient(false);

        ParsePosition posicion = new ParsePosition(0);
        java.util.Date fecha = formato.parse(texto, posicion);

        return fecha != null
                && posicion.getIndex() == texto.length();
    }

    // Contrato de acciones entre la lista y la pantalla.
    private interface AccionesTarea {
        void editar(Tarea tarea);
        void eliminar(Tarea tarea);
        void cambiarCompletada(Tarea tarea, boolean completada);
    }

    // Adaptador del RecyclerView.
    private static class TareaAdapter
            extends RecyclerView.Adapter<TareaAdapter.Fila> {

        private final List<Tarea> datos = new ArrayList<>();
        private final AccionesTarea acciones;

        TareaAdapter(AccionesTarea acciones) {
            this.acciones = acciones;
        }

        void mostrar(List<Tarea> nuevas) {
            datos.clear();
            datos.addAll(nuevas);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public Fila onCreateViewHolder(@NonNull ViewGroup padre, int tipo) {

            View vista = LayoutInflater.from(padre.getContext()).inflate(R.layout.item_tarea, padre, false);

            return new Fila(vista);
        }

        @Override
        public void onBindViewHolder(
                @NonNull Fila fila, int posicion) {

            Tarea tarea = datos.get(posicion);

            fila.tvTitulo.setText(tarea.getTitulo());
            fila.tvFecha.setText("Entrega: " + tarea.getFechaEntrega());

            String prioridad = tarea.getPrioridad();

            if (prioridad == null || prioridad.isEmpty()) {
                prioridad = "Media";
            }

            fila.tvPrioridad.setText("Prioridad: " + prioridad);

            // Evitar que una fila reciclada active el listener anterior.
            fila.cbCompletada.setOnCheckedChangeListener(null);
            fila.cbCompletada.setChecked(tarea.isCompletada());

            fila.cbCompletada.setOnCheckedChangeListener(
                    (boton, marcada) -> acciones.cambiarCompletada(tarea, marcada));

            fila.btnEditar.setOnClickListener(v -> acciones.editar(tarea));

            fila.btnEliminar.setOnClickListener(v -> acciones.eliminar(tarea));
        }

        @Override
        public int getItemCount() {
            return datos.size();
        }

        static class Fila extends RecyclerView.ViewHolder {

            final TextView tvTitulo;
            final TextView tvFecha;
            final TextView tvPrioridad;
            final CheckBox cbCompletada;
            final Button btnEditar;
            final Button btnEliminar;

            Fila(@NonNull View vista) {
                super(vista);

                tvTitulo = vista.findViewById(R.id.tvTituloTarea);
                tvFecha = vista.findViewById(R.id.tvFechaTarea);
                tvPrioridad = vista.findViewById(R.id.tvPrioridadTarea);
                cbCompletada = vista.findViewById(R.id.cbCompletada);
                btnEditar = vista.findViewById(R.id.btnEditarTarea);
                btnEliminar = vista.findViewById(R.id.btnEliminarTarea);
            }
        }
    }
}