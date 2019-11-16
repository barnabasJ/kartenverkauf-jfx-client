package at.fhv.teama.easyticket.client.jfx.views.login;

import at.fhv.teama.easyticket.client.jfx.views.main.MainView;
import at.fhv.teama.easyticket.rmi.EasyTicketService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
@RequiredArgsConstructor
@Slf4j
public class LoginController implements Initializable {
  private final EasyTicketService easyTicketService;

  @FXML
  private TextField tfUsername;

  @FXML
  private PasswordField pfPassword;

  @FXML
  private Label username;

  @FXML
  private Label password;

  @FXML
  private Button btLogin;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
  }
  @FXML
  private void changeScene(ActionEvent event){
    if (event.getSource() == btLogin){
      MainView view = new MainView();
      Scene scene = new Scene(view.getView());

      //stageinfo
      Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
      window.setScene(scene);
      window.show();
    }
  }

  @FXML
  private void onLoginClick(ActionEvent event){
    boolean success = login();
    if (success){
      changeScene(event);
    }else {

      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Achtung");
      alert.setHeaderText("Login fehlgeschlagen");
      alert.setContentText("Username und/oder Passwort ist falsch.");
      alert.showAndWait();
    }
  }

  private boolean login(){
    String uname = tfUsername.getText();
    String pword = pfPassword.getText();
    //return easyTicketService.login(uname, pword);
    return true;
  }
}
