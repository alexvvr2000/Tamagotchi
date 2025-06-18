package Tamagotchi;

import Tamagotchi.ObjetosRecogibles.Flor;
import Tamagotchi.ObjetosRecogibles.ObjetoRecogible;
import Tamagotchi.ObjetosRecogibles.Pelota;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Tamagotchi {
    private final List<ObjetoRecogible> objetosRecogidos = Collections.synchronizedList(new ArrayList<>());
    private final String nombre;

    private final AtomicInteger porcentajeFelicidad = new AtomicInteger(80);
    private final AtomicInteger porcentajeHigiene = new AtomicInteger(80);
    private final AtomicInteger porcentajeHambre = new AtomicInteger(10);
    public TamagotchiRunnable tamagotchiRunnable;
    public Thread hiloActualizacion;
    private volatile boolean estaHerido = false;

    public Tamagotchi(String nombre, long cicloEjecucionSegundos) throws Exception {
        tamagotchiRunnable = new TamagotchiRunnable(cicloEjecucionSegundos);
        hiloActualizacion = new Thread(tamagotchiRunnable);
        hiloActualizacion.start();
        this.nombre = nombre;
    }

    private boolean agregarPorcentajeFelicidad(int porcentajeAgregado) {
        if (porcentajeAgregado < -100 || porcentajeAgregado > 100) {
            return false;
        }
        while (true) {
            int numeroActual = getPorcentajeFelicidad();
            int nuevoPorcentaje = numeroActual + porcentajeAgregado;

            if (nuevoPorcentaje < 0) {
                nuevoPorcentaje = 0;
            }
            if (nuevoPorcentaje > 100) {
                return false;
            }

            if (porcentajeFelicidad.compareAndSet(numeroActual, nuevoPorcentaje)) {
                return true;
            }
        }
    }

    private boolean agregarPorcentajeHigiene(int porcentajeAgregado) {
        if (porcentajeAgregado < -100 || porcentajeAgregado > 100) {
            return false;
        }
        while (true) {
            int numeroActual = getPorcentajeHigiene();
            int nuevoPorcentaje = numeroActual + porcentajeAgregado;

            if (nuevoPorcentaje < 0) {
                nuevoPorcentaje = 0;
            }
            if (nuevoPorcentaje > 100) {
                return false;
            }

            if (porcentajeHigiene.compareAndSet(numeroActual, nuevoPorcentaje)) {
                return true;
            }
        }
    }

    private boolean agregarPorcentajeHambre(int porcentajeAgregado) {
        if (porcentajeAgregado < -100 || porcentajeAgregado > 100) {
            return false;
        }
        while (true) {
            int numeroActual = getPorcentajeHambre();
            int nuevoPorcentaje = numeroActual + porcentajeAgregado;

            if (nuevoPorcentaje < 0) {
                nuevoPorcentaje = 0;
            }
            if (nuevoPorcentaje > 100) {
                return false;
            }

            if (porcentajeHambre.compareAndSet(numeroActual, nuevoPorcentaje)) {
                return true;
            }
        }
    }

    private void setFelicidad(int nuevaFelicidad) {
        porcentajeFelicidad.set(nuevaFelicidad); // es atómico
    }

    private void setHigiene(int nuevaHigiene) {
        porcentajeHigiene.set(nuevaHigiene); // es atómico
    }

    private void setHambre(int nuevaHambre) {
        porcentajeHambre.set(nuevaHambre); // es atómico
    }

    public boolean isEstaHerido() {
        return estaHerido;
    }

    private void herirTamagotchi() {
        estaHerido = true;
    }

    public void curarTamagotchi() {
        if (!estaHerido) return;
        estaHerido = false;
    }

    public double getPorcentajeVida() {
        double porcentajeBase = (getPorcentajeFelicidad() + getPorcentajeHigiene() + (100 - getPorcentajeHambre())) / 3.0;

        if (estaHerido && porcentajeBase > 10) {
            return porcentajeBase - 10;
        }

        return porcentajeBase <= 10 && estaHerido ? 0 : porcentajeBase;
    }


    public int getPorcentajeHambre() {
        return porcentajeHambre.get();
    }

    public int getPorcentajeHigiene() {
        return porcentajeHigiene.get();
    }

    public int getPorcentajeFelicidad() {
        return porcentajeFelicidad.get();
    }

    public String getNombre() {
        return nombre;
    }

    public List<ObjetoRecogible> getObjetosRecogidos() {
        return objetosRecogidos;
    }

    public void recogerObjeto(ObjetoRecogible objeto) {
        objetosRecogidos.add(objeto);
    }

    public List<ObjetoRecogible> obtenerObjetosRecogidos() {
        synchronized (objetosRecogidos) {
            return new ArrayList<>(objetosRecogidos);
        }
    }

    public List<MensajeEstadosTamagotchi> obtenerEstadosActivos() {
        List<MensajeEstadosTamagotchi> estadosActivos = new ArrayList<>();
        if (isEstaHerido()) {
            estadosActivos.add(
                    new MensajeEstadosTamagotchi(EstadosTamagotchi.HERIDO, "Tu tamagotchi esta herido")
            );
        }

        if (getPorcentajeHambre() > 0) {
            estadosActivos.add(
                    new MensajeEstadosTamagotchi(EstadosTamagotchi.HAMBRIENTO, "Porcentaje de hambre: " + getPorcentajeHambre())
            );
        }

        if (getPorcentajeHigiene() < 100) {
            estadosActivos.add(
                    new MensajeEstadosTamagotchi(EstadosTamagotchi.SUCIO, "Porcentaje de higiene: " + getPorcentajeHigiene())
            );
        }

        if (getPorcentajeFelicidad() < 100) {
            estadosActivos.add(new MensajeEstadosTamagotchi(EstadosTamagotchi.ABURRIDO, "Porcentaje de felicidad: " + getPorcentajeFelicidad()));
        }
        return estadosActivos;
    }

    public boolean tamagotchiMuerto() {
        return getPorcentajeVida() == 0;
    }

    public int alimentarTamagotchi() throws Exception {
        if (getPorcentajeHambre() == 0) {
            throw new Exception("** Tu tamagotchi no tiene hambre **");
        }
        agregarPorcentajeHambre(4);
        return getPorcentajeHambre();
    }

    public boolean limpiarTamagotchi() {
        int faltanteLimpio = 100 - getPorcentajeHigiene();
        return agregarPorcentajeHigiene(faltanteLimpio);
    }

    public boolean dormirTamagotchi() {
        return agregarPorcentajeFelicidad(10);
    }

    public boolean jugarConTamagotchi() {
        return agregarPorcentajeFelicidad(10);
    }

    public enum EstadosTamagotchi {
        HAMBRIENTO,
        ABURRIDO,
        SUCIO,
        HERIDO
    }

    public record MensajeEstadosTamagotchi(EstadosTamagotchi estado, String Mensaje) {
    }

    public class TamagotchiRunnable implements Runnable {
        private final long cicloRepeticion;
        public static ObjetoRecogible[] objetos = new ObjetoRecogible[]{
                new Flor("fjdkaslñf", "fadklasjfklasd"),
                new Flor("dsala", "kdlñasjfklas"),
                new Pelota("djkflañd", "ñlñlk"),
                new Pelota("utiopewr", "vnmxcv")
        };

        public TamagotchiRunnable(long cicloRepeticion) {
            this.cicloRepeticion = cicloRepeticion;
        }

        public ObjetoRecogible obtenerObjetoAlAzar() {
            int indiceNuevoObjetoRecogido = ThreadLocalRandom.current().nextInt(0, objetos.length);
            return objetos[indiceNuevoObjetoRecogido];
        }

        private void verificarSiEstaMuertoYExplotar() {
            if (tamagotchiMuerto()) {
                throw new TamagotchiMuertoException("El Tamagotchi ha muerto y no puede seguir ejecutando acciones.");
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(this.cicloRepeticion * 1000);
                    boolean tamagotchiRecogeObjeto = ThreadLocalRandom.current().nextBoolean();
                    if (tamagotchiRecogeObjeto) {
                        ObjetoRecogible objetoRecogible = obtenerObjetoAlAzar();
                        recogerObjeto(objetoRecogible);
                    }
                    boolean tamagotchiSeHiere = ThreadLocalRandom.current().nextBoolean();
                    if (tamagotchiSeHiere && !isEstaHerido()) {
                        herirTamagotchi();
                    }
                    verificarSiEstaMuertoYExplotar();
                    agregarPorcentajeFelicidad(-10);
                    agregarPorcentajeHigiene(-20);
                    agregarPorcentajeHambre(+20);
                    verificarSiEstaMuertoYExplotar();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (TamagotchiMuertoException e) {
                    System.out.println("** Tu Tamagotchi ha muerto. **");
                    hiloActualizacion.interrupt();
                }
            }
        }
    }
}
