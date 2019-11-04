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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;

@Component
@Scope("prototype")
@RequiredArgsConstructor
@Slf4j
public class TicketverkaufController implements Initializable {


    //region FXML

    @FXML
    private ChoiceBox<PersonDto> kunden_ChoiceBar;

    @FXML
    private ToggleButton Kat1_Button;

    @FXML
    private ToggleButton Kat2_Button;

    @FXML
    private ToggleButton Kat3_Button;

    @FXML
    private Text Kategorie1_Label;

    @FXML
    private Text Kategorie2_Label;

    @FXML
    private Text Kategorie3_Label;

    @FXML
    private AnchorPane SitzplatzPane;

    @FXML
    private ListView<TicketDto> TicketList;

    @FXML
    private Button Verkaufen_Button;

    @FXML
    private Button Reservieren_Button;

    @FXML
    private Label venue_label;

    @FXML
    private Label sumField;

    //endregion

    private ObservableList<PersonDto> PersonGesamt;
    private Set<PersonDto> allPersons;
    private PersonDto selPerson;

    private ObservableList<TicketDto> TicketsGewaehltGesamt;
    private ArrayList<TicketDto> allTickets;




    private final EasyTicketService easyTicketService;
    private Model model= Model.getInstance();
    private VenueDto venue ;


    private final EventHandler<ActionEvent> onToggleButtonClicked = new EventHandler<ActionEvent>(){

        @Override
        public void handle(final ActionEvent event) {
            ToggleButton t= (ToggleButton)event.getSource();
            int x = Integer.parseInt(t.getId().split("/")[0]);
            int y = Integer.parseInt(t.getId().split("/")[1]);
            TicketDto ticket = getTicketFromCoordinates(x, y);
            if (t.isSelected()){
                allTickets.add(ticket);
                t.setDisable(true);
            } else if(allTickets.contains(ticket)){
                allTickets.remove(ticket);
                t.setDisable(false);
            }
            populateTicketList();
            getSumAndUpdateField();
        }
    };

    private final EventHandler<ActionEvent> onBuyTicketClicked = new EventHandler<ActionEvent>(){

        @Override
        public void handle(final ActionEvent event) {

            Set<TicketDto> retTickets = easyTicketService.buyTickets(allTickets);
            if (retTickets.size()== 0 ){

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Glückwunsch");
                alert.setHeaderText("Erfolgreicher Kauf");
                alert.setContentText("Glückwunsch, ihre "+allTickets.size()+" Tickets wurden erfolgreich gebucht.");
                alert.showAndWait();

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
            }


        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        venue = model.getSelectedVenue();
        initVenueLabel();

        PersonGesamt = FXCollections.observableArrayList();
        allPersons = easyTicketService.getAllCustomer();
        PersonGesamt.setAll(allPersons);
        kunden_ChoiceBar.setItems(PersonGesamt);

        TicketsGewaehltGesamt = FXCollections.observableArrayList();
        TicketList.setSelectionModel(null);

        generateSeatButtons();
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
        Kat2_Button.setMouseTransparent(true);
        Kat3_Button.setMouseTransparent(true);
    }

    public void setUpCategoryLegend(){

    }

    public void generateSeatButtons(){

        ArrayList<ToggleButton> buttons = new ArrayList<>();
        for (TicketDto t:venue.getTickets()) {
            ToggleButton b = new ToggleButton();
            b.setOnAction(onToggleButtonClicked);
            b.setId(t.getX()+"/"+t.getY());
            b.setText(t.getX()+"/"+t.getY());
            if (!t.getState().equals(TicketState.FREE)){
                b.setDisable(true);
            }
            buttons.add(b);
        }

        SitzplatzPane.getChildren().addAll(buttons);
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
        TicketsGewaehltGesamt.addAll(allTickets);
        TicketList.setItems(TicketsGewaehltGesamt);

    }

    public void getSumAndUpdateField(){
        float sum = 0;
        for(TicketDto t : allTickets){
            sum = sum+t.getCategory().getPrice();
        }
        sumField.setText(String.valueOf(sum)+" €");
    }
}
