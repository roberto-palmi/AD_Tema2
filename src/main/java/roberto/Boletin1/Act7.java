package roberto.Boletin1;

import roberto.DataSource;
import roberto.lib.Tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Act7 {
    private Connection conn;
    public Act7(){
        try {
            conn = new DataSource(DataSource.Driver.POSTGRESQL, "localhost", 5432,
                    "postgres", "root", "test").getConnection();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String clasificacionRegularidad(){
        Tables t = new Tables("Posicion","Ciclista","Equipo", "Puntos totales");
        String consulta =""" 
        WITH puntos AS (
        SELECT id_ciclista, puntos FROM puntos_meta
        UNION ALL
        SELECT id_ciclista, puntos FROM resultados_sprint
        )
        SELECT
          DENSE_RANK() OVER (ORDER BY SUM(p.puntos) DESC) AS posicion,
          c.nombre  AS ciclista,
          e.nombre  AS equipo,
          SUM(p.puntos) AS puntos_totales
        FROM puntos p
        JOIN ciclistas c ON c.id_ciclista = p.id_ciclista
        JOIN equipos   e ON e.id_equipo   = c.id_equipo
        GROUP BY c.id_ciclista, c.nombre, e.nombre
        ORDER BY puntos_totales DESC, ciclista ASC;
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
