package Tamagotchi;

public class TamagotchiMuertoException extends RuntimeException {
    public TamagotchiMuertoException(String mensaje) {
        super(mensaje);
    }
}