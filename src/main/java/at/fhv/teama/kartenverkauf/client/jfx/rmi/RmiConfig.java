package at.fhv.teama.kartenverkauf.client.jfx.rmi;

import at.fhv.teama.kartenverkauf.rmi.EasyTicketService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

@Configuration
public class RmiConfig {
    @Bean
    RmiProxyFactoryBean service() {
        RmiProxyFactoryBean rmiProxyFactory = new RmiProxyFactoryBean();
        rmiProxyFactory.setServiceUrl("rmi://localhost/EasyTicketService");
        rmiProxyFactory.setServiceInterface(EasyTicketService.class);
        return rmiProxyFactory;
    }
}
