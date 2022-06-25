package broadcast.client.controllers;

import java.net.*;
import java.util.ResourceBundle;

import broadcast.client.models.ModelMenu;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class MenuController implements Initializable {
    @FXML
    private Text error;
    @FXML
    private Button start;
    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private Button autoSelection;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        start.setOnAction(actionEvent -> ModelMenu.goChat(name,error,start,address));
        autoSelection.setOnAction(actionEvent -> ModelMenu.autoSelection(address, autoSelection));
    }

}
