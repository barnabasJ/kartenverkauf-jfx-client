package at.fhv.teama.easyticket.client.jfx.views.messaging;

import at.fhv.teama.easyticket.client.jfx.views.AddMessage.AddMessageView;
import at.fhv.teama.easyticket.client.jfx.views.Warenkorb.WarenkorbView;
import at.fhv.teama.easyticket.dto.PersonDto;
import at.fhv.teama.easyticket.dto.VenueDto;
import at.fhv.teama.easyticket.rmi.EasyTicketService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.sql.BatchUpdateException;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

@Component
@Scope("prototype")
@RequiredArgsConstructor
@Slf4j

public class MessagingController implements Initializable {

    //region FXML

    @FXML
    private Button New_Message_Button;

    @FXML
    private TableView<PersonDto> Messages_Table;

    @FXML
    private TableColumn<PersonDto, String> Genre_Col;

    @FXML
    private TableColumn<PersonDto, String> Content_Col;

    @FXML
    private Label Sel_Message_Label;

    @FXML
    private CheckBox System_CB;

    @FXML
    private CheckBox Konzerte_CB;

    @FXML
    private CheckBox Kabarett_CB;

    @FXML
    private CheckBox Chor_CB;

    @FXML
    private CheckBox Theater_CB;

    //endregion

    private final EasyTicketService easyTicketService;
    private ObservableList<PersonDto> MessagesGesamt = FXCollections.observableArrayList();
    private Set<PersonDto> allMessages = new HashSet<>();



    private final EventHandler<ActionEvent> onAddMessageButtonClicked = new EventHandler<ActionEvent>(){

        @Override
        public void handle(final ActionEvent event) {


            Stage newWindow = new Stage();
            newWindow.initModality(Modality.APPLICATION_MODAL);
            Scene WarenkorbScene = new Scene(new AddMessageView().getView());
            newWindow.setScene(WarenkorbScene);
            newWindow.showAndWait();
            //updateTable
        }};

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initMessageTable();
        fillMessageTable();
        Messages_Table.getSelectionModel().selectedItemProperty().addListener(this::onMessageChanged);
        New_Message_Button.setOnAction(onAddMessageButtonClicked);

    }

    public void fillMessageTable(){
       // allMessages = easyTicketService.getAllCustomer();
        MessagesGesamt.addAll(allMessages);
        Messages_Table.setItems(MessagesGesamt);
    }

    public void initMessageTable(){
        Genre_Col.setCellValueFactory(p -> {
            if (p.getValue() != null && p.getValue().getLastname() != null) {
                return new SimpleStringProperty(p.getValue().getLastname() );
            }
            return new SimpleStringProperty("-");
        });

        Content_Col.setCellValueFactory(p -> {
            if (p.getValue() != null && p.getValue().getFirstname() != null) {
                return new SimpleStringProperty(p.getValue().getFirstname() );
            }
            return new SimpleStringProperty("-");
        });
    }

    private void onMessageChanged(ObservableValue<? extends PersonDto> obs, PersonDto oldSelection, PersonDto newSelection) {

        if(newSelection!=null){
            Sel_Message_Label.setText(newSelection.getLastname()+"\n"+newSelection.getLastname());
        }
    }
}
