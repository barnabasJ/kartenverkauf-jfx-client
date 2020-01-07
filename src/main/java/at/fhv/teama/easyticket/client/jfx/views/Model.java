package at.fhv.teama.easyticket.client.jfx.views;

import at.fhv.teama.easyticket.client.jfx.views.Login.User;
import at.fhv.teama.easyticket.client.jfx.views.Main.MainController;
import at.fhv.teama.easyticket.dto.MessageDto;
import at.fhv.teama.easyticket.dto.PersonDto;
import at.fhv.teama.easyticket.dto.TicketDto;
import at.fhv.teama.easyticket.dto.VenueDto;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Component
public class Model {
  private  VenueDto selectedVenue;
  private PersonDto selectedPerson;
  private ArrayList<TicketDto> shoppingCartTickets = new ArrayList<>();
  private TableView<MessageDto> messageDtoTableView;
  private User currentUser;
  private int NumberMessages;
  private boolean publishing;



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

  public boolean getPublishing() {
    return publishing;
  }
}
