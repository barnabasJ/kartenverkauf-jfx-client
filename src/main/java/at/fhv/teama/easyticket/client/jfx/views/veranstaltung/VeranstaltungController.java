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
import javafx.scene.layout.AnchorPane;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;


@Component
@Scope("prototype")
@RequiredArgsConstructor
@Slf4j
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
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(_format);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setLabelsDisabled();

        initializeVeranstaltungTable();
        _veranstaltungenGesamt = FXCollections.observableArrayList();
        Set<VenueDto> allVenues = easyTicketService.getAllVenues();
        log.info("Venues ================");
        System.out.println(allVenues);
        _veranstaltungenGesamt.setAll(allVenues);
        Veranstaltungen_Table.setItems(_veranstaltungenGesamt);
        Veranstaltungen_Table.getSelectionModel().selectedItemProperty().addListener(this::onVenueChanged);

    }

    private void onVenueChanged(ObservableValue<? extends VenueDto> obs, VenueDto oldSelection, VenueDto newSelection) {

        if (newSelection != null) {
            if (newSelection.getProgram().getDescription() != null) {
                Veranstaltungen_Bezeichnung_Label.setText(newSelection.getProgram().getDescription());
            } else {
                Veranstaltungen_Bezeichnung_Label.setText("-");
            }

            if (String.format(_format, newSelection.getDate()) != null) {
                Veranstaltungen_Datum_Label.setText(String.format(_format, newSelection.getDate()));
            } else {
                Veranstaltungen_Datum_Label.setText("-");
            }

            if (newSelection.getProgram().getGenre() != null) {
                Veranstaltungen_Genre_Label.setText(newSelection.getProgram().getGenre());
            } else {
                Veranstaltungen_Genre_Label.setText("-");
            }

            if (newSelection.getProgram().getArtists().iterator().next().getName() != null) {
                Veranstaltungen_Kuenstler_Label.setText(newSelection.getProgram().getArtists().iterator().next().getName());
            } else {
                Veranstaltungen_Kuenstler_Label.setText("-");
            }

            if (newSelection.getAddress().getLine1() != null && newSelection.getAddress().getLocality() != null && newSelection.getAddress().getRegion() != null) {
                Veranstaltungen_Ort_Label.setText(newSelection.getAddress().getLocality() + ", " + newSelection.getAddress().getLine1() + ", " + newSelection.getAddress().getRegion());
            } else {
                Veranstaltungen_Ort_Label.setText("-");
            }

            if (newSelection.getTickets().isEmpty() == false) {
                Veranstaltungen_Verfügbar_Label.setText("Tickets verfügbar");
            } else {
                Veranstaltungen_Verfügbar_Label.setText("AUSVERKAUFT");
            }

            if (newSelection.getProgram().getOrganizer().getEmail() != null) {
                Veranstaltungen_EMail_Label.setText(newSelection.getProgram().getOrganizer().getEmail());
            } else {
                Veranstaltungen_Kuenstler_Label.setText("-");
            }

        }

    }

    private void initializeVeranstaltungTable() {

        Veranstaltungen_Bezeichnung_Col.setCellValueFactory(p -> {
            if (p.getValue() != null && p.getValue().getProgram().getDescription() != null) {
                return new SimpleStringProperty(p.getValue().getProgram().getDescription());
            }
            return new SimpleStringProperty("-");
        });

        Veranstaltungen_Datum_Col.setCellValueFactory(p -> {
            if (p.getValue() != null && p.getValue().getDate() != null) {
                return new SimpleStringProperty(p.getValue().getDate().format(formatter));
            }
            return new SimpleStringProperty("-");
        });

        Veranstaltungen_Kuenstler_Col.setCellValueFactory(p -> {
            ArrayList<ArtistDto> artist = new ArrayList<>(p.getValue().getProgram().getArtists());
            if (p.getValue() != null && artist != null && artist.get(0) != null) {
                return new SimpleStringProperty(artist.get(0).getName());
            }
            return new SimpleStringProperty("-");
        });


    }

    private void setLabelsDisabled() {
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
