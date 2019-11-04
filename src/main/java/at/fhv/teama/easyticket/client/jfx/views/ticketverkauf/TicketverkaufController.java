package at.fhv.teama.easyticket.client.jfx.views.ticketverkauf;


import at.fhv.teama.easyticket.rmi.EasyTicketService;
import javafx.fxml.Initializable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
@RequiredArgsConstructor
@Slf4j
public class TicketverkaufController implements Initializable {


    private final EasyTicketService easyTicketService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
