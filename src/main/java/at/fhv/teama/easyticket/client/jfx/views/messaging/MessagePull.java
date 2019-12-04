package at.fhv.teama.easyticket.client.jfx.views.messaging;

import at.fhv.teama.easyticket.client.jfx.views.Model;
import at.fhv.teama.easyticket.dto.MessageDto;
import at.fhv.teama.easyticket.rmi.EasyTicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagePull {

  private final Model model;
  private final EasyTicketService easyTicketService;

  @Scheduled(fixedRate = 500)
  public void poll() {
    log.info("Polling");
    if (model.getCurrentUser() != null) {
      Set<MessageDto> messageDtos =
          easyTicketService.getAllUnreadMessages(model.getCurrentUser().getUsername());
      int num = messageDtos != null ? messageDtos.size() : 0;
      model.updateMessagingTabDescription(num);
    }
  }
}
