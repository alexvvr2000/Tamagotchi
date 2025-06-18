package Tamagotchi.ObjetosRecogibles;

import java.io.Serializable;

public class Flor implements ObjetoRecogible, Serializable {
    private final String nombre;
    private final String descripcion;

    public Flor(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    @Override
    public String obtenerNombre() {
        return this.nombre;
    }

    @Override
    public String obtenerDescripcion() {
        return this.descripcion;
    }
}
