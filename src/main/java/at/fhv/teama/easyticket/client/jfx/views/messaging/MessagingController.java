package at.fhv.teama.easyticket.client.jfx.views.messaging;

import at.fhv.teama.easyticket.client.jfx.views.AddMessage.AddMessageView;
import at.fhv.teama.easyticket.client.jfx.views.Model;
import at.fhv.teama.easyticket.client.jfx.views.Warenkorb.WarenkorbView;
import at.fhv.teama.easyticket.dto.MessageDto;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.sql.BatchUpdateException;
import java.text.NumberFormat;
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
    private TableView<MessageDto> Messages_Table;

    @FXML
    private TableColumn<MessageDto, String> Genre_Col;

    @FXML
    private TableColumn<MessageDto, String> Content_Col;

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

    private Model model = Model.getInstance();
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

        initMessageTable();
        fillMessageTable();
        Messages_Table.getSelectionModel().selectedItemProperty().addListener(this::onMessageChanged);
        New_Message_Button.setOnAction(onAddMessageButtonClicked);

        if(model.getCurrentUser().getRoles().contains("PUBLISHER_ROLE")){
            New_Message_Button.setVisible(true);
        } else {
            New_Message_Button.setVisible(false);
        }

    }

    public void fillMessageTable(){
        MessagesGesamt.clear();
        Messages_Table.getItems().clear();
        allMessages = easyTicketService.getAllUnreadMessages(model.getCurrentUser().getUsername());
        MessagesGesamt.addAll(allMessages);
        Messages_Table.setItems(MessagesGesamt);
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
    }

    private void onMessageChanged(ObservableValue<? extends MessageDto> obs, MessageDto oldSelection, MessageDto newSelection) {

        if(newSelection!=null){
            Sel_Message_Label.setText(newSelection.getTopic() +"\n"+newSelection.getContent());
            easyTicketService.acknowledgeMessage((String) newSelection.getId());
            Parent parent = model.getMainScene().getRoot();
            ObservableList<Node> nodes = ((VBox)parent).getChildren();
            Node pane = nodes.get(1);
            ObservableList<Node> children= ((AnchorPane)pane).getChildren();
            Tab tab = ((TabPane)children).getTabs().get(3);
            int old = Integer.parseInt(tab.getText().replaceAll("[\\D]", ""));
            int newv = old--;
            tab.setText("Nachrichten ("+newv+")");
        }
    }
}
