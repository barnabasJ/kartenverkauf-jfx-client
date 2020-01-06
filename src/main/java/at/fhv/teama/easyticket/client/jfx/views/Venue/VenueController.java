package at.fhv.teama.easyticket.client.jfx.views.Venue;

import at.fhv.teama.easyticket.client.jfx.views.Model;
import at.fhv.teama.easyticket.client.jfx.views.ShoppingCart.CartView;
import at.fhv.teama.easyticket.client.jfx.views.TicketSale.SaleView;
import at.fhv.teama.easyticket.dto.ArtistDto;
import at.fhv.teama.easyticket.dto.TicketDto;
import at.fhv.teama.easyticket.dto.TicketState;
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
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


@Component
@Scope("prototype")
@RequiredArgsConstructor
@Slf4j
public class VenueController implements Initializable {

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
    private DatePicker Veranstaltungen_Datum_From;
    @FXML
    private TextField Veranstaltungen_Kuenstler_Searchbar;
    @FXML
    private DatePicker Veranstaltungen_Datum_To1;
    @FXML
    private Button Warenkorb_Button;
    @FXML
    private Button Veranstaltung_Datum_From_Button;
    @FXML
    private Button Veranstaltung_Genre_Button;
    @FXML
    private Button Veranstaltung_Datum_To_Button;
    @FXML
    private Button Veranstaltung_Kue_Button;
    @FXML
    private Button Veranstaltung_Bez_Button;

    //endregion

    //region Table and List Arrays and Sets
    private ObservableList<VenueDto> _veranstaltungenGesamt;
    private Set<VenueDto> _allVenues;
    //endregion
    private ObservableList<String> _GenresGesamt = FXCollections.observableArrayList();
    private Set<String> _allGenres;

    //region Filters and Formatters
    private String _filterBezeichnung;
    private String _filterKuenstler;
    //endregion

    private LocalDate _filterDatumFrom;
    private LocalDate _filterDatumTo;
    private String _filterGenre;

    private final Model model;

    //region Handlers
    private final EventHandler<ActionEvent> onFilterChanged = new EventHandler<ActionEvent>() {

        @Override
        public void handle(final ActionEvent event) {
            updateFilterValues();
            Set<VenueDto> filteredSet = easyTicketService.searchVenue(_filterBezeichnung,
                    _filterGenre,
                    _filterKuenstler,
                    _filterDatumFrom == null ? null : LocalDateTime.of(_filterDatumFrom, LocalTime.now()),
                    _filterDatumTo == null ? null : LocalDateTime.of(_filterDatumTo, LocalTime.now()));
            populateVenueTable(filteredSet);
        }
    };

    private final EventHandler<ActionEvent> onWarenkorbClicked = new EventHandler<ActionEvent>() {

        @Override
        public void handle(final ActionEvent event) {

            Stage newWindow = new Stage();
            newWindow.initModality(Modality.APPLICATION_MODAL);
            Scene WarenkorbScene = new Scene(new CartView().getView());
            newWindow.setScene(WarenkorbScene);
            newWindow.showAndWait();
            updateGUI();

        }
    };

    private final EventHandler<ActionEvent> onDeleteFilterButtonPressed = new EventHandler<ActionEvent>() {

        @Override
        public void handle(final ActionEvent event) {
            if (event.getSource() == Veranstaltung_Bez_Button) {
                Veranstaltungen_Bezeichnung_Searchbar.setText("");
            }
            if (event.getSource() == Veranstaltung_Kue_Button) {
                Veranstaltungen_Kuenstler_Searchbar.setText("");
            }
            if (event.getSource() == Veranstaltung_Genre_Button) {
                Veranstaltungen_Genre_Searchbar.getSelectionModel().clearSelection();
            }
            if (event.getSource() == Veranstaltung_Datum_From_Button) {
                Veranstaltungen_Datum_From.getEditor().clear();
                Veranstaltungen_Datum_From.setValue(null);
            }
            if (event.getSource() == Veranstaltung_Datum_To_Button) {
                Veranstaltungen_Datum_To1.getEditor().clear();
                Veranstaltungen_Datum_To1.setValue(null);
            }
            updateFilterValues();
            Set<VenueDto> filteredSet = easyTicketService.searchVenue(_filterBezeichnung, _filterGenre, _filterKuenstler, _filterDatumFrom == null ? null : LocalDateTime.of(_filterDatumFrom, LocalTime.now()), _filterDatumTo == null ? null : LocalDateTime.of(_filterDatumTo, LocalTime.now()));
            populateVenueTable(filteredSet);
        }
    };
    private String _format = "dd.MM.yyyy HH:mm:ss";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(_format);
    private final EventHandler<ActionEvent> onBuyTicketclicked = new EventHandler<ActionEvent>() {

        @Override
        public void handle(final ActionEvent event) {
            Stage newWindow = new Stage();
            newWindow.initModality(Modality.APPLICATION_MODAL);
            model.setSelectedVenue(Veranstaltungen_Table.getSelectionModel().selectedItemProperty().getValue());
            Scene buyTicketScene = new Scene(new SaleView().getView());
            newWindow.setScene(buyTicketScene);
            newWindow.showAndWait();
            updateGUI();


        }
    };



    //endregion

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setLabelsDisabled();
        Veranstaltung_Verkaufen_Button.setDisable(true);

        initializeVeranstaltungTable();
        initWarenkorb();
        initVenues();

        log.info("Venues ================");
        System.out.println(_allVenues);

        populateVenueTable(_allVenues);
        populateGenreChoiceBoxU();


        //region Only FutureDates
        Veranstaltungen_Datum_From.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0);
            }
        });
        Veranstaltungen_Datum_To1.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0);
            }
        });

        //endregion

        //region OnActionHandler

        Veranstaltungen_Table.getSelectionModel().selectedItemProperty().addListener(this::onVenueChanged);
        Veranstaltungen_Bezeichnung_Searchbar.setOnAction(onFilterChanged);
        Veranstaltungen_Kuenstler_Searchbar.setOnAction(onFilterChanged);
        Veranstaltungen_Genre_Searchbar.setOnAction(onFilterChanged);
        Veranstaltungen_Datum_From.setOnAction(onFilterChanged);
        Veranstaltungen_Datum_To1.setOnAction(onFilterChanged);

        Veranstaltung_Bez_Button.setOnAction(onDeleteFilterButtonPressed);
        Veranstaltung_Kue_Button.setOnAction(onDeleteFilterButtonPressed);
        Veranstaltung_Genre_Button.setOnAction(onDeleteFilterButtonPressed);
        Veranstaltung_Datum_From_Button.setOnAction(onDeleteFilterButtonPressed);
        Veranstaltung_Datum_To_Button.setOnAction(onDeleteFilterButtonPressed);

        Veranstaltung_Verkaufen_Button.setOnAction(onBuyTicketclicked);
        Warenkorb_Button.setOnAction(onWarenkorbClicked);

        //endregionMs


    }

    public void initVenues() {
        _veranstaltungenGesamt = FXCollections.observableArrayList();
        _GenresGesamt = FXCollections.observableArrayList();
        _allVenues = easyTicketService.getAllVenues();
    }

    private void onVenueChanged(ObservableValue<? extends VenueDto> obs, VenueDto oldSelection, VenueDto newSelection) {

        if (newSelection != null) {

            Veranstaltung_Verkaufen_Button.setDisable(false);
            if (newSelection.getProgram().getDescription() != null) {
                Veranstaltungen_Bezeichnung_Label.setText(newSelection.getProgram().getDescription());
            } else {
                Veranstaltungen_Bezeichnung_Label.setText("-");
            }

            if (String.format(_format, newSelection.getDate()) != null) {
                Veranstaltungen_Datum_Label.setText(newSelection.getDate().format(formatter));
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

            if (countEmptySeats(newSelection) > 0) {
                Veranstaltungen_Verfügbar_Label.setText("Freie Plätze: " + countEmptySeats(newSelection) + "/" + newSelection.getTickets().size());
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
        Veranstaltungen_Datum_Col.setSortType(TableColumn.SortType.ASCENDING);

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

    private void populateVenueTable(Set<VenueDto> venues) {
        Veranstaltungen_Table.getItems().clear();
        _veranstaltungenGesamt.clear();
        _veranstaltungenGesamt.setAll(venues);
        Veranstaltungen_Table.setItems(_veranstaltungenGesamt);
    }

    private void populateGenreChoiceBoxU() {
        Set<String> allGenres = easyTicketService.getAllGenres();
        _GenresGesamt.setAll(allGenres);
        Veranstaltungen_Genre_Searchbar.setItems(_GenresGesamt);
    }

    private void updateFilterValues() {
        _filterBezeichnung = Veranstaltungen_Bezeichnung_Searchbar.getText();
        _filterKuenstler = Veranstaltungen_Kuenstler_Searchbar.getText();
        _filterDatumFrom = Veranstaltungen_Datum_From.getValue();
        _filterDatumTo = Veranstaltungen_Datum_To1.getValue();
        _filterGenre = Veranstaltungen_Genre_Searchbar.getValue();
    }

    private int countEmptySeats(VenueDto venue) {
        AtomicInteger free = new AtomicInteger(0);
        Set<TicketDto> tickets = venue.getTickets();

        for (TicketDto t : tickets) {
            if (t.getState().equals(TicketState.FREE)) {
                free.addAndGet(1);
            }
        }

        return free.intValue();
    }

    private void updateGUI() {
        Veranstaltungen_Table.getSelectionModel().clearSelection();
        Veranstaltungen_Bezeichnung_Label.clear();
        Veranstaltungen_Datum_Label.clear();
        Veranstaltungen_Genre_Label.clear();
        Veranstaltungen_Kuenstler_Label.clear();
        Veranstaltungen_Ort_Label.clear();
        Veranstaltungen_Verfügbar_Label.clear();
        Veranstaltungen_EMail_Label.clear();
        Veranstaltung_Verkaufen_Button.setDisable(true);
        initializeVeranstaltungTable();
        initWarenkorb();
        initVenues();

        populateVenueTable(_allVenues);
        populateGenreChoiceBoxU();
    }

    public void initWarenkorb(){
        if (model.getSelectedPerson()!= null){
            Warenkorb_Button.setDisable(false);
        } else {
            Warenkorb_Button.setDisable(true);
        }
    }




}
