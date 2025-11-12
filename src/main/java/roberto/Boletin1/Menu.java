package roberto.Boletin1;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Scanner;

public class Menu {

    public void mostrarMenu() {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("========= MENU PRINCIPAL =========");
            System.out.println("1. Listar equipos");
            System.out.println("2. Listar ciclistas por equipo");
            System.out.println("3. Listar etapas");
            System.out.println("4. Velocidad media ciclista");
            System.out.println("5. Clasificación de etapa");
            System.out.println("6. Clasificación de la montaña");
            System.out.println("7. Clasificación de la regularidad");
            System.out.println("8. Clasificación general");
            System.out.println("9. Clasificación por equipos");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    System.out.println("Lista de todos los equipos: ");
                    System.out.println(new Act1().listarEquipos());
                    break;
                case 2:
                    int id2 = 0;
                    System.out.println(new Act1().listarEquipos());
                    System.out.println("Elige un ID para ver todos los ciclistas: ");
                    id2 = sc.nextInt();
                    sc.nextLine();
                    System.out.println(new Act2().ciclistasPorEquipo(id2));
                    break;
                case 3:
                    System.out.println(new Act3().listarEtapas());
                    System.out.println("Resumen por etapas: ");
                    System.out.println(new Act3().resumenPorEtapas());
                    break;
                case 4:
                    int id4 = 0;
                    System.out.println(new Act1().listarEquipos());
                    System.out.println("Elige el ID del equipo al que pertenece el ciclista: ");
                    id4 = sc.nextInt();
                    sc.nextLine();
                    System.out.println(new Act2().ciclistasPorEquipo(id4));
                    System.out.println("Elige el ID del ciclista el cual le quieres ver la velocidad media: ");
                    id4 = sc.nextInt();
                    sc.nextLine();
                    System.out.println(new Act4().velocidadMediaCiclista(id4));
                    break;
                case 5:
                    int id5;
                    System.out.println(new Act3().listarEtapas());
                    System.out.println("Elige el ID de la etapa que quieras ver la clasificación: ");
                    id5 = sc.nextInt();
                    sc.nextLine();
                    System.out.println(new Act5().mostrarClasificacionEtapa(id5));
                    break;
                case 6:
                    System.out.println(new Act6().clasificacionMontania());
                    break;
                case 7:
                    System.out.println(new Act7().clasificacionRegularidad());
                    break;
                case 8:
                    System.out.println(new Act8().clasificacionGeneral());
                    break;
                case 9:
                    System.out.println(new Act9().clasificacionPorEquipos());
                case 0:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }
}
