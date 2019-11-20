package at.fhv.teama.easyticket.client.jfx.views.AddMessage;

import at.fhv.teama.easyticket.client.jfx.views.messaging.MessagingController;
import at.fhv.teama.easyticket.dto.PersonDto;
import at.fhv.teama.easyticket.rmi.EasyTicketService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;


@Component
@Scope("prototype")
@RequiredArgsConstructor
@Slf4j

public class AddMessageController implements Initializable {

    @FXML
    private TextArea Message_Area;

    @FXML
    private Button Publish_Mess_Button;

    @FXML
    private ChoiceBox<String> Genre_ChoiceBox;


    private final EasyTicketService easyTicketService;
    private List<String> allGenres = new ArrayList<>();
    private ObservableList<String> MessagesGesamt = FXCollections.observableArrayList();

    private final EventHandler<ActionEvent> onAddMessageButtonClicked = new EventHandler<ActionEvent>(){

        @Override
        public void handle(final ActionEvent event) {
            if((Genre_ChoiceBox.getSelectionModel().getSelectedItem()!= null )&&(Message_Area.getText()!=null)||(Message_Area.getText()!="")){
                //puplish new message
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }

        }};

    private final EventHandler<ActionEvent> onGenreChanged = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {

            if (Genre_ChoiceBox.getSelectionModel().getSelectedItem()!= null){
                Publish_Mess_Button.setDisable(false);
            }
        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Publish_Mess_Button.setDisable(true);
        Genre_ChoiceBox.setOnAction(onGenreChanged);
        initChoiceBox();

    }


    public void initChoiceBox(){
        allGenres.add("System");
        allGenres.add("Konzerte");
        allGenres.add("Chor");
        allGenres.add("Kabarett");
        allGenres.add("Theater");
        MessagesGesamt.addAll(allGenres);
        Genre_ChoiceBox.setItems(MessagesGesamt);
    }
}