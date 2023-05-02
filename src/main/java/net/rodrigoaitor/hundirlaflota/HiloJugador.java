package net.rodrigoaitor.hundirlaflota;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.concurrent.Task;

public class HiloJugador extends Thread{

    private Socket socket;
    private HashMap<int[], String> tablero;
    private ArrayList<int[]> barcos;

    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private String color;



    public HiloJugador() {
        tablero = new HashMap<>();
        barcos = new ArrayList<>();

    }

    public void run() {
        try {
            socket = new Socket("10.2.9.10", 5000);
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());
            color = entrada.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void disparar(int x, int y) {
        try {
            salida.writeUTF(x + ";" + y);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void waitTurn() {
        try {
            entrada.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void recibirDisparo(int x, int y) {
        int[] coordenadas = {x, y};
        if (tablero.containsKey(coordenadas)) {
            tablero.put(coordenadas, "tocado");
        } else {
            tablero.put(coordenadas, "agua");
        }
    }

    /**
     * Envía un mensaje al cliente
     * @param mensaje mensaje a enviar
     */
    public void enviarString(String mensaje) {
        try {
            salida.writeUTF(mensaje);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Envía un objeto al cliente
     * @param objeto
     */
    public void enviarObjeto(Object objeto) {
        try {
            salida.writeObject(objeto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Recibe un mensaje del cliente
     * @return mensaje recibido
     */
    public String recibirString() {
        try {
            return entrada.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Devuelve el color del jugador
     * @return color recibido
     */
    public String getColor() {
        return color;
    }

    /**
     * Devuelve la salida del jugador
     * @return salida
     */
    public ObjectOutputStream getSalida() {
        return salida;
    }

    /**
     * Devuelve la entrada del jugador
     * @return entrada
     */

    public ObjectInputStream getEntrada() {
        return entrada;
    }
}
