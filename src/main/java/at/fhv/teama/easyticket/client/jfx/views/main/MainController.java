package at.fhv.teama.easyticket.client.jfx.views.main;

import at.fhv.teama.easyticket.client.jfx.views.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;


@Component
@Scope("prototype")
@RequiredArgsConstructor
public class MainController implements Initializable {


    @FXML
    private TabPane tabPane;

    @FXML
    private Tab Uebersicht_Tab;

    @FXML
    private AnchorPane uebersicht_pane;

    @FXML
    private Tab Kunden_Tab;

    @FXML
    private AnchorPane kunden_pane;

    @FXML
    private Tab Veranstaltung_Tab;

    @FXML
    private AnchorPane veranstaltung_pane;

   @FXML
   private Tab Messaging_Tab;

    private final Model model;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        model.setMainController(this);

    }


    public void updateMessagingTabDescription(int num){
        try {
             Thread.sleep(10);
            Messaging_Tab.setText("Nachrichten ("+num+")");
        } catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    public Tab getMessaging_Tab(){
        return Messaging_Tab;
    }
}


