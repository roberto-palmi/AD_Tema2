package roberto.Boletin1;

import roberto.DataSource;
import roberto.lib.Tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Act4 {
    private Connection conn;
    public Act4(){
        try {
            conn = new DataSource(DataSource.Driver.POSTGRESQL, "localhost", 5432,
                    "postgres", "root", "test").getConnection();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String velocidadMediaCiclista(int idCiclista){
        Tables t = new Tables("ID Ciclista","Nombre Ciclista","Velocidad media");
        String consulta ="""
                       SELECT c.id_ciclista,
                       c.nombre,
                       ROUND(SUM(e.distancia_km) / NULLIF(SUM(EXTRACT(EPOCH FROM r.tiempo)) / 3600.0, 0), 2) AS velocidad_media
                FROM resultados_etapa r
                JOIN etapas e    ON e.id_etapa = r.id_etapa
                JOIN ciclistas c ON c.id_ciclista = r.id_ciclista
                WHERE r.id_ciclista = ?
                  AND r.estado = 'FINALIZADO'
                  AND r.tiempo IS NOT NULL
                GROUP BY c.id_ciclista, c.nombre
                """;;
        try(PreparedStatement stmt = conn.prepareStatement(consulta)){
            stmt.setInt(1, idCiclista);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                t.addRow(
                        rs.getString("id_ciclista"),
                        rs.getString("nombre"),
                        rs.getString("velocidad_media")
                );
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return t.toString();
    }
}
