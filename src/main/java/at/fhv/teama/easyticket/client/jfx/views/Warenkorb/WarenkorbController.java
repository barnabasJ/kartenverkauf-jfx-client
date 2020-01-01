package at.fhv.teama.easyticket.client.jfx.views.Warenkorb;

import at.fhv.teama.easyticket.client.jfx.views.Model;
import at.fhv.teama.easyticket.dto.TicketDto;
import at.fhv.teama.easyticket.rmi.EasyTicketService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;

@Component
@Scope("prototype")
@RequiredArgsConstructor
@Slf4j
public class WarenkorbController implements Initializable {

  private final Model model;
  private final EasyTicketService easyTicketService;
  private final EventHandler<ActionEvent> onDeleteCartClicked =
          new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent event) {

              easyTicketService.unreserveTickets(model.getShoppingCartTickets());
              model.clearAllFields();
              Node source = (Node) event.getSource();
              Stage stage = (Stage) source.getScene().getWindow();
              stage.close();
            }
          };
  @FXML
  private ListView<TicketDto> TicketList;
  @FXML
  private Button DeleteCart_Button;
  @FXML
  private Button Reserve_Button;
  @FXML
  private Button Buy_Button;
  @FXML
  private Label SumLabel;
  @FXML
  private Label Person_Label;
  private ArrayList<TicketDto> cartTickets = new ArrayList<>();
  private ObservableList<TicketDto> cartTicketsObs = FXCollections.observableArrayList();
  private final EventHandler<ActionEvent> onBuyTicketsClicked =
          new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent event) {

              Set<TicketDto> retTickets = easyTicketService.buyTickets(model.getShoppingCartTickets());
              if (retTickets.size() == 0) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Glückwunsch");
                alert.setHeaderText("Erfolgreicher Kauf");
                alert.setContentText(
                        "Glückwunsch, ihre "
                                + model.getShoppingCartTickets().size()
                                + " Tickets wurden erfolgreich gekauft.");

                alert.showAndWait();
                model.clearAllFields();
                clearAllFields();
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();

              } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Achtung");
                alert.setHeaderText(retTickets.size() + " Ticket/s konnten nicht gekauft werden.");
                String content = "";
                for (TicketDto t : retTickets) {
                  content = content + " Ticket: " + t.getX() + "/" + t.getY() + "\n";
                }
                alert.setContentText(
                        retTickets.size()
                                + " ihrer gewünschten Ticket/s konnten nicht gekauft werden.\n"
                                + content);

                alert.showAndWait();
                model.clearAllFields();
                clearAllFields();
                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
              }
            }
          };

  private final EventHandler<ActionEvent> onReserveTicketsClicked =
          new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent event) {

              Alert alert = new Alert(Alert.AlertType.INFORMATION);
              alert.setTitle("Glückwunsch");
              alert.setHeaderText("Erfolgreiche Reservierung");
              alert.setContentText(
                      "Glückwunsch, ihre "
                              + model.getShoppingCartTickets().size()
                              + " Tickets wurden erfolgreich reserviert.");

              alert.showAndWait();
              model.clearAllFields();
              clearAllFields();
              Node source = (Node) event.getSource();
              Stage stage = (Stage) source.getScene().getWindow();
              stage.close();
            }
          };

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    Person_Label.setText(
            model.getSelectedPerson().getLastname() + " " + model.getSelectedPerson().getFirstname());
    TicketList.setMouseTransparent(true);
    initTickets();
    getSumAndUpdateField();

    DeleteCart_Button.setOnAction(onDeleteCartClicked);
    Buy_Button.setOnAction(onBuyTicketsClicked);
    Reserve_Button.setOnAction(onReserveTicketsClicked);

    if (cartTickets.size() == 0) {
      setAllButtonsDisabled();
    }
  }

  public void initTickets() {
    cartTickets = model.getShoppingCartTickets();
    cartTicketsObs.addAll(cartTickets);
    TicketList.setItems(cartTicketsObs);
  }

  public void getSumAndUpdateField() {
    int sum = 0;
    for (TicketDto t : cartTickets) {
      sum = sum + t.getCategory().getPrice();
    }
    SumLabel.setText(sum / 100 + " €");
  }

  public void setAllButtonsDisabled() {
    Reserve_Button.setDisable(true);
    Buy_Button.setDisable(true);
    DeleteCart_Button.setDisable(true);
  }

  public void clearAllFields() {
    cartTickets.clear();
    cartTicketsObs.clear();
  }
}
