package com.mycompany.guia_2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAOJdbc implements EstudianteDAO{
    private final String url;
    private final String usuario;
    private final String password;
    
    public EstudianteDAOJdbc(String url,String usuario,String correo,String password){
        this.url = url;
        this.usuario = usuario;
        this.password = password;
    }
    
    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(url,usuario, password);       
    }
    
    // ---------- Operaciones del contrato ----------

    @Override
    public void insertar(Estudiante e) {
        String sql = "INSERT INTO estudiantes(nombre,correo,carrera) VALUES (?,?,?)";
        
        try (Connection con = conectar();
            PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getCorreo());
            ps.setString(3,e.getCarrera());
            
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al insertar el estudiante en la base", ex);
        }
    }

    @Override
    public Estudiante buscar(int id) {
        String sql = "SELECT id, nombre, correo, carrera FROM estudiantes WHERE id = ?";
        
        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Estudiante e = new Estudiante();
                    e.setId(rs.getInt("id"));
                    e.setNombre(rs.getString("nombre"));
                    e.setCorreo(rs.getString("correo"));
                    e.setCarrera(rs.getString("carrera"));                    
                    return e;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar el estudiante con ID: " + id, ex);
        }
        return null;
    }
    
    @Override
    public List<Estudiante> buscarPorCarrera(String carrera) {
        List<Estudiante> estudiantesFiltrados = new ArrayList<>();
        String sql = "SELECT id, nombre, correo, carrera FROM estudiantes WHERE LOWER(carrera) = LOWER(?)";
        
        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, carrera);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Estudiante e = new Estudiante();
                    e.setId(rs.getInt("id"));
                    e.setNombre(rs.getString("nombre"));
                    e.setCorreo(rs.getString("correo"));
                    e.setCarrera(rs.getString("carrera")); 
                    
                    estudiantesFiltrados.add(e);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar los estudiantes con carrera: " + carrera, ex);
        }
        return estudiantesFiltrados;
    }

    @Override
    public List<Estudiante> listar() {
        List<Estudiante> estudiantes = new ArrayList<>();
        String sql = "SELECT id,nombre,correo,carrera FROM estudiantes";
                
        try (Connection conn = conectar();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()){
                Estudiante e = new Estudiante();
                e.setId(rs.getInt("id"));
                e.setNombre(rs.getString("nombre"));
                e.setCorreo(rs.getString("correo"));
                e.setCarrera(rs.getString("carrera"));
                estudiantes.add(e);
            }
        } catch (SQLException ex) {
             throw new RuntimeException("No se pudo leer la base", ex);
        }
        return estudiantes;
    }

    @Override
    public boolean actualizar(Estudiante nuevo) {
        String sql = "UPDATE estudiantes SET nombre = ?, correo = ?, carrera = ? WHERE id = ?";
        
        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nuevo.getNombre());
            ps.setString(2, nuevo.getCorreo());
            ps.setString(3, nuevo.getCarrera());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Error al actualizar el estudiante", ex);
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM estudiantes WHERE id = ?";
        
        try (Connection conn = conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException ex) {
            throw new RuntimeException("Error al eliminar el estudiante", ex);
        }
    }
}
