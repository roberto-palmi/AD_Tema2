package roberto.Boletin1;

import roberto.DataSource;
import roberto.lib.Tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Act3 {
    private Connection conn;
    public Act3(){
        try {
            conn = new DataSource(DataSource.Driver.POSTGRESQL, "localhost", 5432,
                    "postgres", "root", "test").getConnection();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String listarEtapas(){
        Tables t;
        String consulta = "SELECT id_etapa, tipo, fecha, salida, llegada, distancia_km FROM etapas";
        t = new Tables("ID Etapa", "Tipo", "Fecha", "Salida", "Llegada", "KM");
        try (PreparedStatement stmt = conn.prepareStatement(consulta)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                t.addRow(
                        rs.getString("id_etapa"),
                        rs.getString("tipo"),
                        rs.getDate("fecha").toString(),
                        rs.getString("salida"),
                        rs.getString("llegada"),
                        rs.getString("distancia_km")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return t.toString();
    }

    public String resumenPorEtapas(){
        String consulta = "SELECT tipo, COUNT(*) AS cantidad, SUM(distancia_km) AS total_km FROM etapas GROUP BY tipo";
        Tables t = new Tables("Tipo", "Cantidad", "Total KM");

        try (PreparedStatement stmt = conn.prepareStatement(consulta)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                t.addRow(
                        rs.getString("tipo"),
                        rs.getString("cantidad"),
                        rs.getString("total_km")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return t.toString();
    }
}
