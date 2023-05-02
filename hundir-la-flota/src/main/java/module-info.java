module net.rodrigoaitor.hundirlaflota {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    opens net.rodrigoaitor.hundirlaflota to javafx.fxml;
    exports net.rodrigoaitor.hundirlaflota;
}