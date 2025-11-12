package roberto.Boletin1;

import roberto.DataSource;
import roberto.lib.Tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Act6 {
    private Connection conn;
    public Act6(){
        try {
            conn = new DataSource(DataSource.Driver.POSTGRESQL, "localhost", 5432,
                    "postgres", "root", "test").getConnection();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String clasificacionMontania(){
        Tables t = new Tables("Posicion","Ciclista","Equipo", "Puntos totales");
        String consulta ="""
            SELECT DENSE_RANK() OVER (ORDER BY SUM(rp.puntos) DESC) AS posicion,
                   c.nombre  AS ciclista,
                   e.nombre  AS equipo,
                   SUM(rp.puntos) AS puntos_totales
            FROM resultados_puerto rp
            JOIN ciclistas c ON c.id_ciclista = rp.id_ciclista
            JOIN equipos   e ON e.id_equipo   = c.id_equipo
            GROUP BY c.id_ciclista, c.nombre, e.nombre
            ORDER BY puntos_totales DESC
""";
        try(PreparedStatement stmt = conn.prepareStatement(consulta)){
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                t.addRow(
                        rs.getString("posicion"),
                        rs.getString("ciclista"),
                        rs.getString("equipo"),
                        rs.getString("puntos_totales")
                );
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return t.toString();
    }
}
