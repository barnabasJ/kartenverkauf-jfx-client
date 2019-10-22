package at.fhv.teama.kartenverkauf.client.jfx.views.main;

import at.fhv.teama.kartenverkauf.dto.AddressDto;
import at.fhv.teama.kartenverkauf.rmi.EasyTicketService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;


@Component
@Scope("prototype")
public class MainController {
    private EasyTicketService easyTicketService;

    public MainController(EasyTicketService easyTicketService) throws RemoteException {
        this.easyTicketService = easyTicketService;
    }
}


