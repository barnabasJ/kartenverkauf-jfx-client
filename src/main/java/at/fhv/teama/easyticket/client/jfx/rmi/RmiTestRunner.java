package at.fhv.teama.easyticket.client.jfx.rmi;

import at.fhv.teama.easyticket.rmi.EasyTicketService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RmiTestRunner implements CommandLineRunner {
    private final EasyTicketService easyTicketService;

    @Override
    public void run(String... args) {
        Authentication auth
                = new UsernamePasswordAuthenticationToken("bob", "password");
        auth.setAuthenticated(false);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);
    }
}
