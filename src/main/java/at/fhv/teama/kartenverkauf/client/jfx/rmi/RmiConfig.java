package at.fhv.teama.kartenverkauf.client.jfx.rmi;

import at.fhv.teama.kartenverkauf.rmi.EasyTicketService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.remoting.support.RemoteInvocationFactory;
import org.springframework.security.remoting.rmi.ContextPropagatingRemoteInvocationFactory;


@Configuration
public class RmiConfig {
   @Bean
   RemoteInvocationFactory factory() {
       return new ContextPropagatingRemoteInvocationFactory();
   }

    @Bean
    RmiProxyFactoryBean service(RemoteInvocationFactory factory) {
       System.setSecurityManager(new SecurityManager());
        RmiProxyFactoryBean rmiProxyFactory = new RmiProxyFactoryBean();
        rmiProxyFactory.setServiceUrl("rmi://localhost/EasyTicketService");
        rmiProxyFactory.setServiceInterface(EasyTicketService.class);
        rmiProxyFactory.setRemoteInvocationFactory(factory);
        return rmiProxyFactory;
    }
}
