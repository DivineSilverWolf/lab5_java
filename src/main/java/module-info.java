module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires com.google.gson;

    opens broadcast.client to javafx.fxml;
    exports broadcast.client;
    exports broadcast.client.controllers;
    opens broadcast.client.controllers to javafx.fxml;

    opens broadcast.client.models to com.google.gson;
    opens broadcast.server to com.google.gson;
    opens broadcast.general to com.google.gson;
    opens broadcast.general.message.type to com.google.gson;
}
