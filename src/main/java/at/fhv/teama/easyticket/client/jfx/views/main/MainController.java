package at.fhv.teama.easyticket.client.jfx.views.main;

import at.fhv.teama.easyticket.rmi.EasyTicketService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;


@Component
@Scope("prototype")
@AllArgsConstructor
public class MainController {
    private final EasyTicketService easyTicketService;
}


