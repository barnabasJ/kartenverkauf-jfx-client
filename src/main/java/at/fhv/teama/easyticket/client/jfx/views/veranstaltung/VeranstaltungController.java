package at.fhv.teama.easyticket.client.jfx.views.veranstaltung;

import at.fhv.teama.easyticket.dto.ArtistDto;
import at.fhv.teama.easyticket.dto.VenueDto;
import at.fhv.teama.easyticket.rmi.EasyTicketService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;


@Component
@Scope("prototype")
@RequiredArgsConstructor
public class VeranstaltungController implements Initializable {

    private final EasyTicketService easyTicketService;


    //region FXML Declarations
    @FXML
    private AnchorPane Veranstaltungen_Table_Pane;

    @FXML
    private TableView<VenueDto> Veranstaltungen_Table;

    @FXML
    private TableColumn<VenueDto, String> Veranstaltungen_Datum_Col;

    @FXML
    private TableColumn<VenueDto, String> Veranstaltungen_Bezeichnung_Col;

    @FXML
    private TableColumn<VenueDto, String> Veranstaltungen_Kuenstler_Col;

    @FXML
    private Button Veranstaltung_Verkaufen_Button;

    @FXML
    private AnchorPane Veranstaltung_Details_Pane;

    @FXML
    private TextField Veranstaltungen_Kuenstler_Label;

    @FXML
    private TextField Veranstaltungen_Bezeichnung_Label;

    @FXML
    private TextField Veranstaltungen_Ort_Label;

    @FXML
    private TextField Veranstaltungen_Datum_Label;

    @FXML
    private TextField Veranstaltungen_Genre_Label;

    @FXML
    private TextField Veranstaltungen_Verfügbar_Label;

    @FXML
    private TextField Veranstaltungen_EMail_Label;

    @FXML
    private AnchorPane Veranstaltungen_Filter_Pane;

    @FXML
    private TextField Veranstaltungen_Bezeichnung_Searchbar;

    @FXML
    private ChoiceBox<String> Veranstaltungen_Genre_Searchbar;

    @FXML
    private DatePicker Veranstaltungen_Datum_Searchbar;

    @FXML
    private TextField Veranstaltungen_Kuenstler_Searchbar;

    @FXML
    private Button Veranstaltung_Erstellen_Button;
    //endregion

    private ObservableList<VenueDto> _veranstaltungenGesamt;

    private String _filterBezeichnung;
    private String _filterKuenstler;
    private Date _filterDatum;
    private String _filterGenre;
    private String _format = "dd/MM/yyyy HH:mm:ss";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setLabelsDisabled();

        initializeVeranstaltungTable();
        _veranstaltungenGesamt = FXCollections.observableArrayList();
       // _veranstaltungenGesamt.setAll(easyTicketService.getAllVenues());
        Veranstaltungen_Table.setItems(_veranstaltungenGesamt);
        Veranstaltungen_Table.getSelectionModel().selectedItemProperty().addListener(this::onVenueChanged);

    }

    private void onVenueChanged(ObservableValue<? extends VenueDto> obs, VenueDto oldSelection, VenueDto newSelection) {

        if (newSelection != null) {
            if (newSelection.getProgramDto().getDescription() != null){
                Veranstaltungen_Bezeichnung_Label.setText(newSelection.getProgramDto().getDescription());}
            else {Veranstaltungen_Bezeichnung_Label.setText("-");}

            if (String.format(_format, newSelection.getDate()) != null){
                Veranstaltungen_Datum_Label.setText(String.format(_format, newSelection.getDate()));}
            else {Veranstaltungen_Datum_Label.setText("-");}

            if (newSelection.getProgramDto().getGenre() != null){
                Veranstaltungen_Genre_Label.setText(newSelection.getProgramDto().getGenre());}
            else {Veranstaltungen_Genre_Label.setText("-");}

            if (newSelection.getProgramDto().getArtistDtos().iterator().next().getName() != null){
                Veranstaltungen_Kuenstler_Label.setText(newSelection.getProgramDto().getArtistDtos().iterator().next().getName());}
            else {Veranstaltungen_Kuenstler_Label.setText("-");}

            if (newSelection.getAddressDto().getLine1() != null && newSelection.getAddressDto().getLocality() != null && newSelection.getAddressDto().getRegion() != null){
                Veranstaltungen_Ort_Label.setText(newSelection.getAddressDto().getLocality()+", "+newSelection.getAddressDto().getLine1()+", "+newSelection.getAddressDto().getRegion());}
            else {Veranstaltungen_Ort_Label.setText("-");}

            if (newSelection.getTicketDtos().isEmpty() == false){
                Veranstaltungen_Verfügbar_Label.setText("Tickets verfügbar");}
            else {Veranstaltungen_Verfügbar_Label.setText("AUSVERKAUFT");}

            if (newSelection.getProgramDto().getOrganizer().getEmail() != null){
                Veranstaltungen_EMail_Label.setText(newSelection.getProgramDto().getOrganizer().getEmail());}
            else {Veranstaltungen_Kuenstler_Label.setText("-");}

        }

    }

    private void initializeVeranstaltungTable(){

        Veranstaltungen_Bezeichnung_Col.setCellValueFactory(p -> {
            if (p.getValue() != null && p.getValue().getProgramDto().getDescription() != null) {
                return new SimpleStringProperty(p.getValue().getProgramDto().getDescription());
            }
            return new SimpleStringProperty("-");
        });

        Veranstaltungen_Datum_Col.setCellValueFactory(p -> {
            if (p.getValue() != null && p.getValue().getDate() != null) {
                return new SimpleStringProperty(String.format(_format,p.getValue().getDate()));
            }
            return new SimpleStringProperty("-");
        });

        Veranstaltungen_Kuenstler_Col.setCellValueFactory(p -> {
            ArrayList<ArtistDto> artist = new ArrayList<>(p.getValue().getProgramDto().getArtistDtos());
            if (p.getValue() != null && artist.get(0) != null) {
                return new SimpleStringProperty(artist.get(0).getName());
            }
            return new SimpleStringProperty("-");
        });


    }

    private void setLabelsDisabled (){
        Veranstaltungen_Verfügbar_Label.setFocusTraversable(false);
        Veranstaltungen_Verfügbar_Label.setMouseTransparent(true);

        Veranstaltungen_EMail_Label.setFocusTraversable(false);
        Veranstaltungen_EMail_Label.setMouseTransparent(true);

        Veranstaltungen_Bezeichnung_Label.setFocusTraversable(false);
        Veranstaltungen_Bezeichnung_Label.setMouseTransparent(true);

        Veranstaltungen_Kuenstler_Label.setFocusTraversable(false);
        Veranstaltungen_Kuenstler_Label.setMouseTransparent(true);

        Veranstaltungen_Ort_Label.setFocusTraversable(false);
        Veranstaltungen_Ort_Label.setMouseTransparent(true);

        Veranstaltungen_Datum_Label.setFocusTraversable(false);
        Veranstaltungen_Datum_Label.setMouseTransparent(true);

        Veranstaltungen_Genre_Label.setFocusTraversable(false);
        Veranstaltungen_Genre_Label.setMouseTransparent(true);


    }






}
