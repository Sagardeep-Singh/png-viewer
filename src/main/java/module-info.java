module com.cmpt.pngviewer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.cmpt.pngviewer to javafx.fxml;
    exports com.cmpt.pngviewer;
}