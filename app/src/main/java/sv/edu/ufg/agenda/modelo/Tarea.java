package sv.edu.ufg.agenda.modelo;

public class Tarea implements Entidad{
    private int id;
    private int materiaId;
    private String titulo;
    private String fechaEntrega;   // texto con formato aaaa-mm-dd
    private boolean completada;
    private String prioridad;

    public Tarea() { }

    public Tarea(int id, int materiaId, String titulo, String fechaEntrega, String prioridad) {
        this.id = id;
        this.materiaId = materiaId;
        this.titulo = titulo;
        this.fechaEntrega = fechaEntrega;
        this.completada = false;
        this.prioridad = prioridad;
    }

    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }
    public int getMateriaId() { return materiaId; }
    public void setMateriaId(int materiaId) { this.materiaId = materiaId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }
    public boolean isCompletada() { return completada; }
    public void setCompletada(boolean completada) { this.completada = completada; }

    //Se agrega prioridad
    public String getPrioridad(){return prioridad;}
    public void setPrioridad(String prioridad){this.prioridad = prioridad;}
}
