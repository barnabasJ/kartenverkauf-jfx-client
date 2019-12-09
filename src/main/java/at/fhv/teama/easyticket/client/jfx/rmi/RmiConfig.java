package at.fhv.teama.easyticket.client.jfx.rmi;

import at.fhv.teama.easyticket.rmi.EasyTicketService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.remoting.support.RemoteInvocationFactory;
import org.springframework.security.remoting.rmi.ContextPropagatingRemoteInvocationFactory;

@Configuration
@ConfigurationProperties(prefix = "server")
public class RmiConfig {
    @Getter @Setter private int port;
    @Getter @Setter private String hostname;

    @Bean
    RemoteInvocationFactory factory() {
        return new ContextPropagatingRemoteInvocationFactory();
    }

    RmiProxyFactoryBean service(RemoteInvocationFactory factory) {
        System.setSecurityManager(new SecurityManager());
        RmiProxyFactoryBean rmiProxyFactory = new RmiProxyFactoryBean();
        rmiProxyFactory.setServiceUrl("rmi://" + hostname + ":" + port + "/EasyTicketService");
        rmiProxyFactory.setServiceInterface(EasyTicketService.class);
        rmiProxyFactory.setRemoteInvocationFactory(factory);
        return rmiProxyFactory;
    }
}
