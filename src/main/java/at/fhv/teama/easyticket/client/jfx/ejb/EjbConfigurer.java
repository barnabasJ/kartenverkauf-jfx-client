package at.fhv.teama.easyticket.client.jfx.ejb;

import at.fhv.teama.easyticket.rmi.EasyTicketService;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

@Data
@Configuration
@ConfigurationProperties(prefix = "easy-ticket.ejb")
public class EjbConfigurer {
  private int port;
  private String hostname;

  @Bean
  @ConditionalOnProperty(prefix = "easy-ticket", name = "server.connection", havingValue = "ejb")
  public Context context() throws NamingException {
    Properties jndiProps = new Properties();
    jndiProps.put(
            "java.naming.factory.initial", "org.jboss.naming.remote.client.InitialContextFactory");
    jndiProps.put("jboss.naming.client.ejb.context", true);
    jndiProps.put("java.naming.provider.url", "http-remoting://" + hostname + ":" + port);
    jndiProps.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", false);
    jndiProps.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", false);
    jndiProps.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
    jndiProps.put(Context.SECURITY_PRINCIPAL, "client");
    jndiProps.put(Context.SECURITY_CREDENTIALS, "ws2019");
    return new InitialContext(jndiProps);
  }

  @Bean
  @ConditionalOnBean(name = "context")
  public EasyTicketService easyTicketService(Context context) throws NamingException {
    return (EasyTicketService) context.lookup(this.getFullName(EasyTicketService.class));
  }

  private String getFullName(Class classType) {
    String moduleName = "EasyTicketServer-0.0.1-SNAPSHOT/";
    String beanName = classType.getSimpleName();
    String viewClassName = classType.getName();
    return moduleName + beanName + "!" + viewClassName;
  }
}
