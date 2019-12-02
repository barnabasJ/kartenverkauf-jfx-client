package at.fhv.teama.easyticket.client.jfx.views;

import at.fhv.teama.easyticket.client.jfx.views.login.User;
import at.fhv.teama.easyticket.dto.PersonDto;
import at.fhv.teama.easyticket.dto.TicketDto;
import at.fhv.teama.easyticket.dto.VenueDto;
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
