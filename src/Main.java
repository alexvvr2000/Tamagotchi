import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int SEGUNDOS_ACTUALIZACION = 2;
        Scanner scanner = new Scanner(System.in);
        try {
            JuegoTamagotchi juego = new JuegoTamagotchi(SEGUNDOS_ACTUALIZACION, scanner);
            juego.IniciarlizarTamagotchi();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        scanner.close();
    }
}