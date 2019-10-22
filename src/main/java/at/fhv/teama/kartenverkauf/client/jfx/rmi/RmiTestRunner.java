package at.fhv.teama.kartenverkauf.client.jfx.rmi;

import at.fhv.teama.kartenverkauf.dto.ProgramDto;
import at.fhv.teama.kartenverkauf.rmi.EasyTicketService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class RmiTestRunner implements CommandLineRunner {
    private EasyTicketService easyTicketService;

    public RmiTestRunner(EasyTicketService easyTicketService) {
        this.easyTicketService = easyTicketService;
    }

    @Override
    public void run(String... args) throws Exception {
        Authentication auth
                = new UsernamePasswordAuthenticationToken("bob", "password");
        auth.setAuthenticated(false);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);
        ProgramDto programDto = new ProgramDto();
        easyTicketService.saveProgram(programDto);
    }
}
