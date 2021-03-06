package at.fhv.teama.easyticket.client.jfx.rmi;

import at.fhv.teama.easyticket.rmi.EasyTicketService;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.remoting.support.RemoteInvocationFactory;
import org.springframework.security.remoting.rmi.ContextPropagatingRemoteInvocationFactory;

@Data
@Configuration
@ConfigurationProperties(prefix = "easy-ticket.rmi")
public class RmiConfigurer {
  private int port;
  private String hostname;

  @Bean
  RemoteInvocationFactory factory() {
    return new ContextPropagatingRemoteInvocationFactory();
  }

  @Bean
  @ConditionalOnProperty(
          prefix = "easy-ticket",
          name = "server.connection",
          havingValue = "rmi",
          matchIfMissing = true)
  RmiProxyFactoryBean service(RemoteInvocationFactory factory) {
    RmiProxyFactoryBean rmiProxyFactory = new RmiProxyFactoryBean();
    rmiProxyFactory.setServiceUrl("rmi://" + hostname + ":" + port + "/EasyTicketService");
    rmiProxyFactory.setServiceInterface(EasyTicketService.class);
    rmiProxyFactory.setRemoteInvocationFactory(factory);
    return rmiProxyFactory;
  }
}
