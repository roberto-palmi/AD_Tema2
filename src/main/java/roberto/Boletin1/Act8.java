package roberto.Boletin1;

import roberto.DataSource;
import roberto.lib.Tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Act8 {
    private Connection conn;
    public Act8(){
        try {
            conn = new DataSource(DataSource.Driver.POSTGRESQL, "localhost", 5432,
                    "postgres", "root", "test").getConnection();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String clasificacionGeneral(){
        Tables t = new Tables("Ciclista","Equipo", "Tiempo total");
        String consulta ="""
            SELECT DENSE_RANK() OVER (ORDER BY SUM(r.tiempo)) AS posicion,
            c.nombre AS ciclista, e.nombre AS equipo, SUM(r.tiempo) AS tiempo_total
            FROM resultados_etapa r
            JOIN ciclistas c ON c.id_ciclista = r.id_ciclista
            JOIN equipos   e ON e.id_equipo   = c.id_equipo
            WHERE r.estado = 'FINALIZADO'
                AND r.tiempo IS NOT NULL
            GROUP BY c.id_ciclista, c.nombre, e.nombre
            ORDER BY tiempo_total ASC, ciclista ASC;
            """;
        try(PreparedStatement stmt = conn.prepareStatement(consulta)){
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                t.addRow(
                        rs.getString("ciclista"),
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
