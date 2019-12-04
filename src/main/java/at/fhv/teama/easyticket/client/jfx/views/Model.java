package at.fhv.teama.easyticket.client.jfx.views;

import at.fhv.teama.easyticket.client.jfx.views.login.User;
import at.fhv.teama.easyticket.dto.PersonDto;
import at.fhv.teama.easyticket.dto.TicketDto;
import at.fhv.teama.easyticket.dto.VenueDto;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Component
public class Model {
  private VenueDto selectedVenue;
  private PersonDto selectedPerson;
  private ArrayList<TicketDto> shoppingCartTickets;
  private User currentUser;
  private Scene mainScene;


  public void setScene(Scene scene){
    mainScene = scene;
  }

  public void updateMessagingTabDescription(int num){

      /*
    Parent parent = mainScene.getRoot();
    ObservableList<Node> nodes = ((VBox)parent).getChildren();
    Node pane = nodes.get(1);
    ObservableList<Node> children= ((AnchorPane)pane).getChildren();
    Tab tab = ((TabPane)children).getTabs().get(3);
    //get number of new messages
    tab.setText("Nachrichten ("+num+")");
 */
  }

  public void addShoppingCartTickets(Collection<TicketDto> ticketDtos) {
    if (shoppingCartTickets != null) {
      shoppingCartTickets.addAll(ticketDtos);
    } else {
      shoppingCartTickets = new ArrayList<>(ticketDtos);
    }
  }

  public void clearAllFields() {
    selectedPerson = null;
    selectedVenue = null;
    shoppingCartTickets = null;
  }
}
