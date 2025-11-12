package roberto.Boletin1;

import roberto.DataSource;
import roberto.lib.Tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Act2 {
    private Connection conn;
    public Act2() {
        try {
            conn = new DataSource(DataSource.Driver.POSTGRESQL, "localhost", 5432,
                    "postgres", "root", "test").getConnection();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String ciclistasPorEquipo(int id_equipo) {
        String consulta;
        Tables t;

        if (id_equipo == 0) {
            consulta = """
                SELECT e.nombre AS equipo, c.id_ciclista, c.nombre, c.pais, c.fecha_nac
                FROM ciclistas c
                JOIN equipos e ON c.id_equipo = e.id_equipo
                ORDER BY e.nombre, c.nombre
            """;
            t = new Tables("Equipo", "ID", "Nombre", "Pais", "Fecha Nacimiento");
            try (PreparedStatement stmt = conn.prepareStatement(consulta)) {
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    t.addRow(
                            rs.getString("equipo"),
                            rs.getString("id_ciclista"),
                            rs.getString("nombre"),
                            rs.getString("pais"),
                            rs.getDate("fecha_nac").toString()
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Si se indica un ID específico, mostramos solo los ciclistas de ese equipo
            consulta = "SELECT id_ciclista, nombre, pais, fecha_nac FROM ciclistas WHERE id_equipo = ? ORDER BY nombre";
            t = new Tables("ID", "Nombre", "Pais", "Fecha Nacimiento");
            try (PreparedStatement stmt = conn.prepareStatement(consulta)) {
                stmt.setInt(1, id_equipo);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    t.addRow(
                            rs.getString("id_ciclista"),
                            rs.getString("nombre"),
                            rs.getString("pais"),
                            rs.getDate("fecha_nac").toString()
                    );
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return t.toString();
    }
}
