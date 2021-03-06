package com.example.tpo_chatappclient;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    @FXML
    public Button button_send;
    @FXML
    public TextField tf_message;
    @FXML
    public ScrollPane sp_main;
    @FXML
    public VBox vbox_messages;
    private Client client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            client = new Client(new Socket("localhost",4200));
            System.out.println("Connected to server");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error creating server");
        }

        vbox_messages.heightProperty().addListener((observableValue, number, t1) -> sp_main.setVvalue((Double) t1));

        client.receiveMessageFromServer(vbox_messages);

        button_send.setOnAction(actionEvent -> {
            String messageToSend = tf_message.getText();
            if(!messageToSend.isEmpty()) {
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new Insets(5));
                Text text = new Text(messageToSend);
                TextFlow textFlow = new TextFlow(text);

                textFlow.setStyle("-fx-color: rgb(239,242,255); " +
                        "-fx-background-color: rgb(253,160,25);" +
                        "-fx-background-radius: 20px");
                textFlow.setPadding(new Insets(5,5,5,10));
                text.setFill(Color.color(0.934,0.945,0.966));

                hBox.getChildren().add(textFlow);
                vbox_messages.getChildren().add(hBox);

                client.sendMessageToServer(messageToSend);
                tf_message.clear();
            }
        });
    }

    public static void addLabel(String messageFromServer, VBox vbox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5));

        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle("-fx-background-color: rgb(223,223,241);" +
                "-fx-background-radius: 20px");
        textFlow.setPadding(new Insets(5,5,5,10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(() -> vbox.getChildren().add(hBox));

    }
}