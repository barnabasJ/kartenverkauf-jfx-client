package at.fhv.teama.easyticket.client.jfx.views.Main;

import at.fhv.teama.easyticket.client.jfx.views.Model;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


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


        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        ses.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {

                Platform.runLater(() -> updateMessagingTabDescription(model.getNumberMessages()));
            }
        }, 0, 500, TimeUnit.MILLISECONDS);

    }


    public void updateMessagingTabDescription(int num){


        if(num>0) {
            Messaging_Tab.setStyle("-fx-background-color: lightblue");
            Messaging_Tab.setText("Nachrichten ("+num+")");
        } else {
            Messaging_Tab.setStyle("--fx-skin: \"com.sun.javafx.scene.control.skin.LabelSkin\"" +
                    " --fx-background-color: transparent" +
                    " --fx-alignment: CENTER" +
                    " --fx-text-fill: -fx-text-base-color");
            Messaging_Tab.setText("Nachrichten");
        }
    }
}


