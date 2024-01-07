module chazz.c482project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens controllers to javafx.fxml;
    opens chazz.c482project to javafx.fxml;
    opens model to javafx.base;
    exports chazz.c482project;
    exports controllers to javafx.fxml;

}