package net.rodrigoaitor.hundirlaflota;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Jugador extends Application implements Runnable{
    private HiloJugador hiloJugador = new HiloJugador();

    @FXML
    private Rectangle shJugador;

    public Jugador() throws IOException {
    }

    @Override
    public void run() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Jugador.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Hundir la flota");
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.setMaximized(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    private GridPane gridPane;
    @FXML
    private Button btnIniciar, btnSalir, btnRotar, btnEmpezar;
    @FXML
    private RadioButton rbPortaaviones, rbAcorazado, rbDestructor, rbSubmarino, rbPatrullero;
    @FXML
    private Label lblPortaaviones, lblAcorazado, lblDestructor, lblSubmarino, lblPatrullero;

    private Button[][] tablero;
    private boolean rotar;
    private HashMap<int[], String> barcos = new HashMap<>();
    private ArrayList<int[]> posBarcos = new ArrayList<>();


    //El método initialize deshabilita los botones que se usan para colocar los barcos  y establece el color del jugador en la interfaz.
    @FXML
    public void initialize() throws IOException, InterruptedException {
        rotar = false;
        ToggleGroup group = new ToggleGroup();
        rbPortaaviones.setToggleGroup(group);
        rbAcorazado.setToggleGroup(group);
        rbDestructor.setToggleGroup(group);
        rbSubmarino.setToggleGroup(group);
        rbPatrullero.setToggleGroup(group);
        rbPortaaviones.setDisable(true);
        rbAcorazado.setDisable(true);
        rbDestructor.setDisable(true);
        rbSubmarino.setDisable(true);
        rbPatrullero.setDisable(true);
        btnRotar.setDisable(true);
        btnEmpezar.setDisable(true);

        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                Button btn = new Button();
                btn.setPrefSize(50, 50);
                btn.setOnMouseClicked(e -> {
                    //                          v Hace referencia a la columna                  v Hace referencia a la fila
                    System.out.println("[" + (GridPane.getColumnIndex(btn)) + ", " + GridPane.getRowIndex(btn) + "]");
                });
                gridPane.add(btn, i, j);
            }
        }

        hiloJugador.start();

        Thread.sleep(1000);

        /*String color = hiloJugador.getColor();
        if(color.equals("rojo")) {
            shJugador.setFill(new Color(1,0,0,1));
            shJugador.setStroke(new Color(1,0,0,1));
        } else {
            shJugador.setFill(new Color(0,0,1,1));
            shJugador.setStroke(new Color(0,0,1,1));
        }*/
    }

    @FXML
    public void onSalir() {
        System.exit(0);
    }

    @FXML
    public void onIniciar() {
        btnIniciar.setDisable(true);
        btnRotar.setDisable(false);
        rbPortaaviones.setDisable(false);
        rbAcorazado.setDisable(false);
        rbDestructor.setDisable(false);
        rbSubmarino.setDisable(false);
        rbPatrullero.setDisable(false);
    }

    private Button[][] cargarTablero(){
        Button[][] tablero = new Button[10][10];
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                tablero[i][j] = (Button) gridPane.getChildren().get(i * 10 + j);
            }
        }
        return tablero;
    }

    @FXML
    public void onBarco() {
        tablero = cargarTablero();
        select();
    }

    @FXML
    public void onRotar() {
        rotar = !rotar;
        select();
    }

    @FXML
    public void onEmpezar(){
        hiloJugador.enviarString("Listo");
        gridPane.getChildren().forEach(e -> e.setDisable(true));
        hiloJugador.enviarObjeto(barcos);
    }

    private void select(){
        if(rbPortaaviones.isSelected() && Integer.parseInt(lblPortaaviones.getText()) > 0) {
            selector(5, rotar);
        }
        if(rbAcorazado.isSelected() && Integer.parseInt(lblAcorazado.getText()) > 0) {
            selector(4, rotar);
        }
        if(rbDestructor.isSelected() && Integer.parseInt(lblDestructor.getText()) > 0) {
            selector(3, rotar);
        }
        if(rbSubmarino.isSelected() && Integer.parseInt(lblSubmarino.getText()) > 0) {
            selector(3, rotar);
        }
        if(rbPatrullero.isSelected() && Integer.parseInt(lblPatrullero.getText()) > 0) {
            selector(2, rotar);
        }
    }
    private void selector(int p, boolean rotacion){
        String style = tablero[0][0].getStyle();

        if(rotacion) {
            // Horizontal
            gridPane.getChildren().forEach(btn -> btn.setOnMouseEntered(e -> {
                for (int i = 0; i < p; i++) {
                    int[] pos;
                    if (GridPane.getColumnIndex(btn) - i < 0 ||
                            tablero[GridPane.getColumnIndex(btn) - i][GridPane.getRowIndex(btn)].isDisable()) {
                        pos = new int[]{GridPane.getColumnIndex(btn) + (p - i), GridPane.getRowIndex(btn)};
                        tablero[GridPane.getColumnIndex(btn) + (p - i)][GridPane.getRowIndex(btn)].setStyle("-fx-background-color: #5080ff");
                    }else {
                        pos = new int[]{GridPane.getColumnIndex(btn) - i, GridPane.getRowIndex(btn)};
                        tablero[GridPane.getColumnIndex(btn) - i][GridPane.getRowIndex(btn)].setStyle("-fx-background-color: #5080ff");
                    }
                }
            }));
            gridPane.getChildren().forEach(btn -> btn.setOnMouseExited(ev -> {
                for (int i = 0; i < p; i++) {
                    int[] pos;
                    if (GridPane.getColumnIndex(btn) - i < 0 ||
                            tablero[GridPane.getColumnIndex(btn) - i][GridPane.getRowIndex(btn)].isDisable()) {
                        pos = new int[]{GridPane.getColumnIndex(btn) + (p - i), GridPane.getRowIndex(btn)};
                        tablero[GridPane.getColumnIndex(btn) + (p - i)][GridPane.getRowIndex(btn)].setStyle(style);
                    }else {
                        pos = new int[]{GridPane.getColumnIndex(btn) - i, GridPane.getRowIndex(btn)};
                        tablero[GridPane.getColumnIndex(btn) - i][GridPane.getRowIndex(btn)].setStyle(style);
                    }
                }
            }));

            gridPane.getChildren().forEach(btn -> btn.setOnMouseClicked(e -> {
                for (int i = 0; i < p; i++) {
                    int[] pos;
                    if (GridPane.getColumnIndex(btn) - i < 0 ||
                            tablero[GridPane.getColumnIndex(btn) - i][GridPane.getRowIndex(btn)].isDisable()) {
                        pos = new int[]{GridPane.getColumnIndex(btn) + (p - i), GridPane.getRowIndex(btn)};
                        tablero[GridPane.getColumnIndex(btn) + (p - i)][GridPane.getRowIndex(btn)].setDisable(true);
                        tablero[GridPane.getColumnIndex(btn) + (p - i)][GridPane.getRowIndex(btn)].setStyle("" +
                                "-fx-border-color: #000000; " +
                                "-fx-border-width: 2px;" +
                                "-fx-border-radius: 4px;" +
                                "");

                        none(p, pos);
                    }else {
                        pos = new int[]{GridPane.getColumnIndex(btn) - i, GridPane.getRowIndex(btn)};
                        tablero[GridPane.getColumnIndex(btn) - i][GridPane.getRowIndex(btn)].setDisable(true);
                        tablero[GridPane.getColumnIndex(btn) - i][GridPane.getRowIndex(btn)].setStyle("" +
                                "-fx-border-color: #000000; " +
                                "-fx-border-width: 2px;" +
                                "-fx-border-radius: 4px;"
                                + "");

                        none(p, pos);
                    }
                    guardarBarco(p, pos);
                    btn.setOnMouseExited(ev -> {});
                }
                for (int[] ints : posBarcos) {
                    System.out.println(Arrays.toString(ints));
                }
                barcoPuesto(p);
            }));

        }else {
            // Vertical
            gridPane.getChildren().forEach(btn -> btn.setOnMouseEntered(e -> {
                for (int i = 0; i < p; i++) {
                    int[] pos;
                    if (GridPane.getRowIndex(btn) - i < 0 ||
                            tablero[GridPane.getColumnIndex(btn)][GridPane.getRowIndex(btn) - i].isDisable()){
                        pos= new int[]{GridPane.getColumnIndex(btn), GridPane.getRowIndex(btn) + (p - i)};
                        tablero[GridPane.getColumnIndex(btn)][GridPane.getRowIndex(btn) + (p - i)].setStyle("-fx-background-color: #5080ff");
                    }else {
                        pos= new int[]{GridPane.getColumnIndex(btn), GridPane.getRowIndex(btn) - i};
                        tablero[GridPane.getColumnIndex(btn)][GridPane.getRowIndex(btn) - i].setStyle("-fx-background-color: #5080ff");
                    }
                }
            }));
            gridPane.getChildren().forEach(btn -> btn.setOnMouseExited(ev -> {
                for (int i = 0; i < p; i++) {
                    int[] pos = new int[]{GridPane.getColumnIndex(btn), GridPane.getRowIndex(btn) - i };
                    if (GridPane.getRowIndex(btn) - i < 0 ||
                            tablero[GridPane.getColumnIndex(btn)][GridPane.getRowIndex(btn) - i].isDisable())
                        tablero[GridPane.getColumnIndex(btn)][GridPane.getRowIndex(btn) + (p - i)].setStyle(style);
                    else
                        tablero[GridPane.getColumnIndex(btn)][GridPane.getRowIndex(btn) - i].setStyle(style);
                }
            }));

            gridPane.getChildren().forEach(btn -> btn.setOnMouseClicked(e -> {
                for (int i = 0; i < p; i++) {
                    int[] pos;

                    if (GridPane.getRowIndex(btn) - i < 0 ||
                            tablero[GridPane.getColumnIndex(btn)][GridPane.getRowIndex(btn) - i].isDisable()){
                        pos= new int[]{GridPane.getColumnIndex(btn), GridPane.getRowIndex(btn) + (p - i)};
                        tablero[GridPane.getColumnIndex(btn)][GridPane.getRowIndex(btn) + (p - i)].setDisable(true);
                        tablero[GridPane.getColumnIndex(btn)][GridPane.getRowIndex(btn) + (p - i)].setStyle("" +
                                "-fx-border-color: #000000; " +
                                "-fx-border-width: 2px; " +
                                "-fx-border-radius: 4px;" +
                                "");
                        btn.setDisable(true);
                    } else {
                        pos= new int[]{GridPane.getColumnIndex(btn), GridPane.getRowIndex(btn) - i};
                        tablero[GridPane.getColumnIndex(btn)][GridPane.getRowIndex(btn) - i].setDisable(true);
                        tablero[GridPane.getColumnIndex(btn)][GridPane.getRowIndex(btn) - i].setStyle("" +
                                "-fx-border-color: #000000; " +
                                "-fx-border-width: 2px; " +
                                "-fx-border-radius: 4px;" +
                                "");
                    }

                    guardarBarco(p, pos);
                    btn.setOnMouseExited(ev -> {});
                }

                for (int[] ints : posBarcos) {
                    System.out.println(Arrays.toString(ints));
                }
                barcoPuesto(p);
            }));
        }
    }

    private void guardarBarco(int p, int[] pos) {

        switch (p) {
            case 5 -> {
                barcos.put(pos, "Portaaviones");
                posBarcos.add(pos);
            }
            case 4 -> {
                barcos.put(pos, "Acorazado");
                posBarcos.add(pos);
            }
            case 3 -> {
                if(rbAcorazado.isSelected()) {
                    barcos.put(pos, "Destructor");
                    posBarcos.add(pos);
                }else if(rbSubmarino.isSelected()){
                    barcos.put(pos, "Submarino");
                    posBarcos.add(pos);
                }
            }
            case 2 -> {
                barcos.put(pos, "Patrullero");
                posBarcos.add(pos);
            }
        }
    }

    private void none(int p, int[] pos) {
        switch (p) {
            case 5 -> barcos.put(pos, "Portaaviones");
            case 4 -> barcos.put(pos, "Acorazado");
            case 3 -> barcos.put(pos, "Destructor");
            case 2 -> barcos.put(pos, "Patrullero");
        }
    }

    private void barcoPuesto(int p) {
        switch (p) {
            case 5 -> deshabilitar(lblPortaaviones, rbPortaaviones);
            case 4 -> deshabilitar(lblAcorazado, rbAcorazado);
            case 3 -> {
                if (rbDestructor.isSelected()) {
                    deshabilitar(lblDestructor, rbDestructor);
                }else if (rbSubmarino.isSelected()){
                    deshabilitar(lblSubmarino, rbSubmarino);
                }
            }
            case 2 -> deshabilitar(lblPatrullero, rbPatrullero);
        }
    }

    private void deshabilitar(Label label, RadioButton radioButton) {
        label.setText(String.valueOf(Integer.parseInt(label.getText()) - 1));
        radioButton.setDisable(true);
        radioButton.setSelected(false);
        gridPane.getChildren().forEach(btn -> btn.setOnMouseClicked(e -> {}));
        gridPane.getChildren().forEach(btn -> btn.setOnMouseEntered(e -> {}));
        gridPane.getChildren().forEach(btn -> btn.setOnMouseExited(e -> {}));
        if(rbPortaaviones.isDisable() && rbAcorazado.isDisable() && rbDestructor.isDisable() && rbSubmarino.isDisable() && rbPatrullero.isDisable()){
            btnEmpezar.setDisable(false);
        }
    }

    public HashMap<int[], String> getBarcos() {
        return barcos;
    }
}

