module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;

    opens broadcast.client to javafx.fxml;
    exports broadcast.client;
    exports broadcast.client.controllers;
    opens broadcast.client.controllers to javafx.fxml;
}
