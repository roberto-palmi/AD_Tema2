package roberto.Boletin1;

import roberto.DataSource;
import roberto.lib.Tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Act5 {
    private Connection conn;
    public Act5(){
        try {
            conn = new DataSource(DataSource.Driver.POSTGRESQL, "localhost", 5432,
                    "postgres", "root", "test").getConnection();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String mostrarClasificacionEtapa(int idEtapa){
        Tables t = new Tables("Ciclista","Equipo", "Tiempo");
        String consulta ="""
                SELECT ROW_NUMBER() OVER (ORDER BY r.tiempo ASC) AS posicion,
                c.nombre AS ciclista, e.nombre AS equipo, r.tiempo
                FROM resultados_etapa r 
                JOIN ciclistas c ON c.id_ciclista = r.id_ciclista
                JOIN equipos   e ON e.id_equipo   = c.id_equipo
                WHERE r.id_etapa = ?
                AND r.estado = 'FINALIZADO' 
                ORDER BY r.tiempo ASC;
                """;;
        try(PreparedStatement stmt = conn.prepareStatement(consulta)){
            stmt.setInt(1, idEtapa);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                t.addRow(
                        rs.getString("ciclista"),
                        rs.getString("equipo"),
                        rs.getString("tiempo")
                );
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return t.toString();
    }
}
