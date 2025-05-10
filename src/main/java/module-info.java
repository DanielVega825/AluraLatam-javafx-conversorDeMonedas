module org.example.apiconveror {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.net.http;
    requires com.google.gson;

    opens org.example.apiconveror to javafx.fxml;
    exports org.example.apiconveror.view;
    opens org.example.apiconveror.view to javafx.fxml;

    exports org.example.apiconveror.model; // Exporta el paquete para que otras clases puedan acceder
    opens org.example.apiconveror.model to javafx.base; // Abre el paquete a javafx.base para la reflexión
}