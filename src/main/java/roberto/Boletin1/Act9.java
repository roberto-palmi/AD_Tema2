package roberto.Boletin1;

import roberto.DataSource;
import roberto.lib.Tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Act9 {
    private Connection conn;
    public Act9(){
        try {
            conn = new DataSource(DataSource.Driver.POSTGRESQL, "localhost", 5432,
                    "postgres", "root", "test").getConnection();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String clasificacionPorEquipos(){
        Tables t = new Tables("Posición", "Nombre del equipo", "Tiempo total");
        String consulta = """
                WITH tiempos AS (
                  SELECT
                    r.id_etapa,
                    c.id_equipo,
                    e.nombre AS equipo,
                    r.id_ciclista,
                    r.tiempo,
                    ROW_NUMBER() OVER (
                      PARTITION BY r.id_etapa, c.id_equipo
                      ORDER BY r.tiempo ASC
                    ) AS rn
                  FROM resultados_etapa r
                  JOIN ciclistas c ON c.id_ciclista = r.id_ciclista
                  JOIN equipos   e ON e.id_equipo   = c.id_equipo
                  WHERE r.estado = 'FINALIZADO'
                    AND r.tiempo IS NOT NULL
                ),
                top3 AS (
                  SELECT id_etapa, id_equipo, equipo, tiempo
                  FROM tiempos
                  WHERE rn <= 3
                )
                SELECT
                  DENSE_RANK() OVER (ORDER BY SUM(tiempo)) AS posicion,
                  equipo,
                  SUM(tiempo)                              AS tiempo_total
                FROM top3
                GROUP BY id_equipo, equipo
                ORDER BY tiempo_total ASC, equipo ASC;
                """;
        try(PreparedStatement stmt = conn.prepareStatement(consulta)){
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                t.addRow(
                        String.valueOf(rs.getInt("posicion")),
                        rs.getString("equipo"),
                        rs.getString("tiempo_total")
                );
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return t.toString();
    }
}
