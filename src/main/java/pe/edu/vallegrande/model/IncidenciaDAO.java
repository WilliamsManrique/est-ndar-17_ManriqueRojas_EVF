package pe.edu.vallegrande.model;

import pe.edu.vallegrande.util.ConexionDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IncidenciaDAO {

    public boolean agregar(Incidencia inc) {
        String sql = "INSERT INTO incidencia (tipo, aula, fecha, estado, descripcion) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, inc.getTipo());
            ps.setString(2, inc.getAula());
            ps.setDate(3, Date.valueOf(inc.getFecha()));
            ps.setString(4, inc.getEstado());
            ps.setString(5, inc.getDescripcion());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean actualizar(Incidencia inc) {
        String sql = "UPDATE incidencia SET tipo=?, aula=?, fecha=?, estado=?, descripcion=? WHERE id=?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, inc.getTipo());
            ps.setString(2, inc.getAula());
            ps.setDate(3, Date.valueOf(inc.getFecha()));
            ps.setString(4, inc.getEstado());
            ps.setString(5, inc.getDescripcion());
            ps.setInt(6, inc.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM incidencia WHERE id=?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<Incidencia> listarTodos() {
        List<Incidencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM incidencia ORDER BY fecha DESC";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public List<Incidencia> buscar(String tipo, String aula, String estado) {
        List<Incidencia> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM incidencia WHERE 1=1 ");
        if (tipo != null && !tipo.isBlank()) sql.append("AND tipo LIKE ? ");
        if (aula != null && !aula.isBlank()) sql.append("AND aula LIKE ? ");
        if (estado != null && !estado.isBlank()) sql.append("AND estado LIKE ? ");
        sql.append("ORDER BY fecha DESC");
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (tipo != null && !tipo.isBlank()) ps.setString(idx++, "%" + tipo + "%");
            if (aula != null && !aula.isBlank()) ps.setString(idx++, "%" + aula + "%");
            if (estado != null && !estado.isBlank()) ps.setString(idx++, "%" + estado + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    private Incidencia mapear(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String tipo = rs.getString("tipo");
        String aula = rs.getString("aula");
        LocalDate fecha = rs.getDate("fecha").toLocalDate();
        String estado = rs.getString("estado");
        String descripcion = rs.getString("descripcion");
        return new Incidencia(id, tipo, aula, fecha, estado, descripcion);
    }

}
