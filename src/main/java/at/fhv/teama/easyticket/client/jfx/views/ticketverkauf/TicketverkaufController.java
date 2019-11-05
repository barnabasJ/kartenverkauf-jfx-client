package at.fhv.teama.easyticket.client.jfx.views.ticketverkauf;


import at.fhv.teama.easyticket.client.jfx.views.Model;
import at.fhv.teama.easyticket.dto.PersonDto;
import at.fhv.teama.easyticket.dto.TicketDto;
import at.fhv.teama.easyticket.dto.TicketState;
import at.fhv.teama.easyticket.dto.VenueDto;
import at.fhv.teama.easyticket.rmi.EasyTicketService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Component
@Scope("prototype")
@RequiredArgsConstructor
@Slf4j
public class TicketverkaufController implements Initializable {


    //region FXML

    @FXML
    private Button DeletePersonButton;

    @FXML
    private ChoiceBox<PersonDto> kunden_ChoiceBar;

    @FXML
    private ToggleButton Kat1_Button;

    @FXML
    private ToggleButton Kat2_Button;

    @FXML
    private ToggleButton Kat3_Button;

    @FXML
    private ToggleButton Kat4_Button;

    @FXML
    private ToggleButton Kat5_Button;

    @FXML
    private Text Kategorie1_Label;

    @FXML
    private Text Kategorie2_Label;

    @FXML
    private Text Kategorie3_Label;

    @FXML
    private GridPane SitzplatzPane;

    @FXML
    private ListView<TicketDto> TicketList;

    @FXML
    private Button Verkaufen_Button;

    @FXML
    private Button Reservieren_Button;

    @FXML
    private Label venue_label;

    @FXML
    private Label sum_field;

    //endregion

    private ObservableList<PersonDto> PersonGesamt = FXCollections.observableArrayList();
    private Set<PersonDto> allPersons = new HashSet<>();


    private ObservableList<TicketDto> TicketsGewaehltGesamt = FXCollections.observableArrayList();
    private ArrayList<TicketDto> allTickets = new ArrayList<>();


    ArrayList<ToggleButton> buttons = new ArrayList<>();

    private final EasyTicketService easyTicketService;
    private Model model= Model.getInstance();
    private VenueDto venue ;




    //region Handlers
    private final EventHandler<ActionEvent> onToggleButtonClicked = new EventHandler<ActionEvent>(){

        @Override
        public void handle(final ActionEvent event) {
            ToggleButton t= (ToggleButton)event.getSource();
            int x = Integer.parseInt(t.getId().split("/")[0]);
            int y = Integer.parseInt(t.getId().split("/")[1]);
            TicketDto ticket = getTicketFromCoordinates(x, y);
            if (t.isSelected()){
                allTickets.add(ticket);
                //t.setDisable(true);
            } else if(allTickets.contains(ticket)){
                allTickets.remove(ticket);
                //t.setDisable(false);
            }
            populateTicketList();
            getSumAndUpdateField();
        }
    };

    private final EventHandler<ActionEvent> onBuyTicketClicked = new EventHandler<ActionEvent>(){

        @Override
        public void handle(final ActionEvent event) {

            if (model.getSelectedPerson()!=null){
                for (TicketDto t: allTickets){
                    t.setPerson(model.getSelectedPerson());
                }
            }
            Set<TicketDto> retTickets = easyTicketService.buyTickets(allTickets);
            if (retTickets.size()== 0 ){

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Glückwunsch");
                alert.setHeaderText("Erfolgreicher Kauf");
                alert.setContentText("Glückwunsch, ihre "+allTickets.size()+" Tickets wurden erfolgreich gebucht.");
                alert.showAndWait();
                model.setSelectedPerson(null);
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Achtung");
                alert.setHeaderText(retTickets.size()+" Ticket/s konnten nicht gebucht werden.");
                String content = new String();
                for (TicketDto t : retTickets){
                    content = content+" Ticket: "+t.getX()+"/"+t.getY()+"\n";
                }
                alert.setContentText(retTickets.size()+" ihrer gewünschten Ticket/s konnten nicht gebucht werden.\n"+content);

                alert.showAndWait();
                model.setSelectedPerson(null);
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }


        }
    };

    private final EventHandler<ActionEvent> onCustomerChanged = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            model.setSelectedPerson(kunden_ChoiceBar.getSelectionModel().getSelectedItem());
        }
    };

    private final EventHandler<ActionEvent> onCustomerDeleteChanged = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            kunden_ChoiceBar.getSelectionModel().clearSelection();
            model.setSelectedPerson(null);
        }
    };

    //endregion

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearEverything();

        disableCategoryButtons();
        venue = model.getSelectedVenue();
        initVenueLabel();

        initPersons();
        kunden_ChoiceBar.setOnAction(onCustomerChanged);

        TicketsGewaehltGesamt = FXCollections.observableArrayList();
        TicketList.setSelectionModel(null);

        generateSeatButtons();

        Verkaufen_Button.setOnAction(onBuyTicketClicked);
        DeletePersonButton.setOnAction(onCustomerDeleteChanged);

    }

    public void initVenueLabel(){
        if (venue != null){
            venue_label.setText(venue.getProgram().getDescription());
        } else {
            venue_label.setText("FEHLER");
        }
    }

    public void disableCategoryButtons (){
        Kat1_Button.setMouseTransparent(true);
        Kat1_Button.setStyle("-fx-text-fill: #026a00; ");
        Kat2_Button.setMouseTransparent(true);
        Kat2_Button.setStyle("-fx-text-fill: #007295;");
        Kat3_Button.setMouseTransparent(true);
        Kat3_Button.setStyle("-fx-text-fill: #340060; ");
        Kat4_Button.setMouseTransparent(true);
        Kat4_Button.setStyle("-fx-text-fill: #870087; ");
        Kat5_Button.setMouseTransparent(true);
        Kat5_Button.setStyle("-fx-text-fill: #8a0002; ");
    }

    public void generateSeatButtons(){
        ArrayList<TicketDto> tickets = new ArrayList<>();
        tickets.addAll(venue.getTickets());
        Collections.sort(tickets, TicketComp);

        for (TicketDto t:tickets) {
            ToggleButton b = new ToggleButton();
            b.setOnAction(onToggleButtonClicked);
            b.setId(t.getX()+"/"+t.getY());
            b.setText(t.getX()+"/"+t.getY());
            switch (t.getCategory().getId().intValue()) {
                case 1:
                    b.setStyle("-fx-text-fill: #026a00; ");
                    break;
                case 2:
                    b.setStyle("-fx-text-fill: #007295; ");
                    break;
                case 3:b.setStyle("-fx-text-fill: #340060; ");
                    break;
                case 4:
                    b.setStyle("-fx-text-fill: #870087; ");
                    break;
                case 5:
                    b.setStyle("-fx-text-fill: #8a0002; ");
                    break;

            }
            if (!t.getState().equals(TicketState.FREE)){
                b.setDisable(true);
            }
            buttons.add(b);
        }


        int rowindex = -1;
        for(int i=0; i< buttons.size(); i++){
            if (i%6==0){
                rowindex++;
                RowConstraints rowConstraints = new RowConstraints(50);
                SitzplatzPane.getRowConstraints().add(rowConstraints);
            }
            SitzplatzPane.add(buttons.get(i), i%6, rowindex);
        }
    }

    public TicketDto getTicketFromCoordinates (int X, int y){
        TicketDto result = null;

        for (TicketDto t : venue.getTickets()){
            if (t.getY().equals(y)&&t.getX().equals(X)){
                result = t;
            }
        }

        return result;
    }

    public void populateTicketList(){
        TicketsGewaehltGesamt.clear();
        TicketList.getItems().clear();
        TicketsGewaehltGesamt.addAll(allTickets);
        TicketList.setItems(TicketsGewaehltGesamt);

    }

    public void initPersons(){
        PersonGesamt = FXCollections.observableArrayList();
        allPersons = easyTicketService.getAllCustomer();
        PersonGesamt.setAll(allPersons);
        kunden_ChoiceBar.setItems(PersonGesamt);
    }

    public void getSumAndUpdateField(){
        int sum = 0;
        for(TicketDto t : allTickets){
            sum = sum+t.getCategory().getPrice();
        }
        sum_field.setText(sum/100+" €");
    }


    public static Comparator<TicketDto> TicketComp = (t1, t2) -> {

        int tcor1 = t1.getX()*100+t1.getY();
        int tcor2 = t2.getX()*100+t2.getY();

        return tcor1-tcor2;
    };

    public void clearEverything(){
        allPersons.clear();
        PersonGesamt.clear();
        buttons.clear();
        allTickets.clear();
        TicketsGewaehltGesamt.clear();
        TicketList.getItems().clear();
    }


}
