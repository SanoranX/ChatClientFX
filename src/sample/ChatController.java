package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController {

    private static String text;
    private static boolean firstTime = true;
    @FXML
    public TextField chatText;
    String tempString;
    String previousChatBox; //Сохраняет данные, что были в чат боксе до этого.
    ClientSomething clientSomething;
    @FXML
    private TextArea chatBox;
    @FXML
    private Button sendButton;

    public ChatController chatController = this;

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        clientSomething = new ClientSomething(chatController, Controller.nickname);
    }

    public void sendText(javafx.event.ActionEvent event) {
        clientSomething.sendText();
    }

    @FXML
    /*public void addText(ActionEvent event){


    }*/

    public void removeTest(){
        chatText.setText("");
    }


    public void addText(javafx.event.ActionEvent event) {
        addText("bb");
    }

    public void addText(String bb) {
        previousChatBox = chatBox.getText();
        chatBox.setText(previousChatBox + bb + "\n");
    }

}
