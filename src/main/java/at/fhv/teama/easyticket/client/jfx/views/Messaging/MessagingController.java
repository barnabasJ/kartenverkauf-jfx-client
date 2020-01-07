package at.fhv.teama.easyticket.client.jfx.views.Messaging;

import at.fhv.teama.easyticket.client.jfx.views.Messaging.AddMessage.AddMessageView;
import at.fhv.teama.easyticket.client.jfx.views.Model;
import at.fhv.teama.easyticket.dto.MessageDto;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Scope("prototype")
@RequiredArgsConstructor
@Slf4j

public class MessagingController implements Initializable {

    //region FXML

    @FXML
    private Button New_Message_Button;

    @FXML
    private TableView<MessageDto> Messages_Table;

    @FXML
    private TableColumn<MessageDto, String> Genre_Col;

    @FXML
    private TableColumn<MessageDto, String> Content_Col;

    @FXML
    private Label Sel_Message_Label;

    @FXML
    private TableColumn<MessageDto, String> timestamp_col;

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

    private final Model model;
    private final EasyTicketService easyTicketService;
    private ObservableList<MessageDto> MessagesGesamt = FXCollections.observableArrayList();
    private Set<MessageDto> allMessages = new HashSet<>();



    private final EventHandler<ActionEvent> onAddMessageButtonClicked = new EventHandler<ActionEvent>(){

        @Override
        public void handle(final ActionEvent event) {


            Stage newWindow = new Stage();
            newWindow.initModality(Modality.APPLICATION_MODAL);
            Scene addMessageScene = new Scene(new AddMessageView().getView());
            newWindow.setScene(addMessageScene);
            newWindow.showAndWait();
            fillMessageTable();
        }};

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        model.setMessageDtoTableView(Messages_Table);
        initMessageTable();
        fillMessageTable();
        Messages_Table.getSelectionModel().selectedItemProperty().addListener(this::onMessageChanged);
        New_Message_Button.setOnAction(onAddMessageButtonClicked);

        if(model.getCurrentUser().getRoles().contains("ROLE_PUBLISHER")){
            New_Message_Button.setVisible(true);
        } else {
            New_Message_Button.setVisible(false);
        }

    }

    public void fillMessageTable(){
        MessagesGesamt.clear();
        Messages_Table.getItems().clear();
        allMessages = easyTicketService.getAllUnreadMessages(model.getCurrentUser().getUsername());

        if (allMessages != null) {
          MessagesGesamt.addAll(allMessages);
          MessagesGesamt.sort(Comparator.comparing(MessageDto::getTimestamp));
          Messages_Table.setItems(MessagesGesamt);
        }

    }

    public void initMessageTable(){
        Genre_Col.setCellValueFactory(p -> {
            if (p.getValue() != null && p.getValue().getTopic() != null) {
                return new SimpleStringProperty(p.getValue().getTopic() );
            }
            return new SimpleStringProperty("-");
        });

        Content_Col.setCellValueFactory(p -> {
            if (p.getValue() != null && p.getValue().getContent() != null) {
                return new SimpleStringProperty(p.getValue().getContent() );
            }
            return new SimpleStringProperty("-");
        });
        timestamp_col.setCellValueFactory(p -> {
            if (p.getValue() != null ) {
                Date date = new Date(p.getValue().getTimestamp());
                Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
                return new SimpleStringProperty(format.format(date));
            }
            return new SimpleStringProperty("-");
        });
    }

    private void onMessageChanged(ObservableValue<? extends MessageDto> obs, MessageDto oldSelection, MessageDto newSelection) {

        if(newSelection!=null){
            Sel_Message_Label.setText(newSelection.getTopic() +"\n"+newSelection.getContent());
             easyTicketService.acknowledgeMessage(newSelection, model.getCurrentUser().getUsername());

        }
    }
}
