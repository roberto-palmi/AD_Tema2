package roberto.Boletin1;
import roberto.DataSource;
import roberto.lib.Tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Act1 {
    private Connection conn;
    public Act1() {
        try {
            conn = new DataSource(DataSource.Driver.POSTGRESQL, "localhost", 5432,
                    "postgres", "root", "test").getConnection();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String listarEquipos(){
        Tables t = new Tables("ID","Nombre","Pais");
        String consulta = "SELECT * FROM equipos";
        try(PreparedStatement stmt = conn.prepareStatement(consulta)){
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    t.addRow(
                            rs.getString("id_equipo"),
                            rs.getString("nombre"),
                            rs.getString("pais")
                    );
                }
            }
         catch (SQLException e) {
            e.printStackTrace();
        }
        return t.toString();
    }

}
