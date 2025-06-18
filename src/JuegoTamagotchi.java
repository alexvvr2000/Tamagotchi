import Tamagotchi.ObjetosRecogibles.ObjetoRecogible;
import Tamagotchi.Tamagotchi;

import java.util.List;
import java.util.Scanner;

public class JuegoTamagotchi {
    private final long PERIODO_ESTADOS_TAMAGOCHI;
    private final Scanner scannerConsola;
    private Tamagotchi tamagotchi;

    public JuegoTamagotchi(long periodoEstadosTamagochi, Scanner scannerConsola) throws Exception {
        if (periodoEstadosTamagochi <= 1) {
            throw new Exception("Tiene que ser 2 segundos de actualización o más para empezar a jugar");
        }
        this.PERIODO_ESTADOS_TAMAGOCHI = periodoEstadosTamagochi;
        this.scannerConsola = scannerConsola;
    }

    public void IniciarlizarTamagotchi() throws Exception {
        System.out.print("Introduzca el nombre de tu tamagotchi: ");
        String nombreTamagotchi = scannerConsola.nextLine();
        this.tamagotchi = new Tamagotchi(nombreTamagotchi, this.PERIODO_ESTADOS_TAMAGOCHI);
        System.out.print("Presione enter para continuar...");
        scannerConsola.nextLine();
        iniciarCicloJuegoPrincipal();
    }

    public void mostrarEstadosActivos() {
        List<Tamagotchi.MensajeEstadosTamagotchi> estatusActivos = tamagotchi.obtenerEstadosActivos();
        if (estatusActivos.isEmpty()) {
            System.out.println("** Tu tamagochi esta en perfectas condiciones **");
        }
        for (Tamagotchi.MensajeEstadosTamagotchi mensaje : estatusActivos) {
            System.out.println("Estado: " + mensaje.estado() + ", Mensaje: " + mensaje.Mensaje());
        }
    }

    private void iniciarCicloJuegoPrincipal() {
        while (true) {
            System.out.println("** Bienvenido, ¿Que quiere hacer con su tamagotchi?: **");
            System.out.println("1. Llevarlo a dormir");
            System.out.println("2. Jugar con el");
            System.out.println("3. Curar sus heridas");
            System.out.println("4. Alimentarlo");
            System.out.println("5. Bañarlo");
            System.out.println("6. Ver que ha recogido");
            System.out.println("7. Mostrar su estatus");
            System.out.println("8. Salir");
            System.out.print("Introduzca la opcion a escoger: ");
            int opcionEscogida = scannerConsola.nextInt();
            scannerConsola.nextLine();
            if (opcionEscogida == 8) {
                break;
            }
            switch (opcionEscogida) {
                case 1:
                    try {
                        tamagotchi.dormirTamagotchi();
                        System.out.println("** Tu tamagotchi descanso muy bien **");
                        System.out.println("Nivel de felicidad actual: " + tamagotchi.getPorcentajeFelicidad());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        tamagotchi.jugarConTamagotchi();
                        System.out.println("** Tu tamagotchi se divirtio bastante **");
                        System.out.println("Nivel de felicidad actual: " + tamagotchi.getPorcentajeFelicidad());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        tamagotchi.curarTamagotchi();
                        System.out.println("** Tu tamagotchi esta muy contento ahora **");
                        System.out.println("Nivel de felicidad actual: " + tamagotchi.getPorcentajeFelicidad());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    try {
                        int porcentajeHambreActual = tamagotchi.alimentarTamagotchi();
                        System.out.println("** Tu tamagotchi ahora esta satisfecho **");
                        System.out.println("** Porcentaje actual de hambre " + porcentajeHambreActual + " **");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    try {
                        tamagotchi.limpiarTamagotchi();
                        System.out.println("** A tu tamagotchi le gusto el baño **");
                        System.out.println("Porcentaje actual de higiene: " + this.tamagotchi.getPorcentajeHigiene());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 6:
                    System.out.println("Estos objetos estan en la bolsa del tamagotchi: ");
                    List<ObjetoRecogible> objetosRecogidos = tamagotchi.obtenerObjetosRecogidos();
                    for (ObjetoRecogible objeto : objetosRecogidos) {
                        System.out.println("Nombre: " + objeto.obtenerNombre() + ", Descripcion: " + objeto.obtenerDescripcion());
                    }
                    break;
                case 7:
                    System.out.println("Porcentaje de vida actual: " + tamagotchi.getPorcentajeVida());
                    System.out.println("Estados activos de tu tamagotchi: ");
                    mostrarEstadosActivos();
                    break;
            }
            System.out.print("Presione enter para continuar...");
            scannerConsola.nextLine();
        }
        System.exit(0);
    }
}
