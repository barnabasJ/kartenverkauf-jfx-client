package at.fhv.teama.easyticket.client.jfx.ejb;

import at.fhv.teama.easyticket.rmi.EasyTicketService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

@Configuration
public class EjbConfigurer {
  @Bean
  public Context context() throws NamingException {
    Properties jndiProps = new Properties();
    jndiProps.put("java.naming.factory.initial",
            "org.jboss.naming.remote.client.InitialContextFactory");
    jndiProps.put("jboss.naming.client.ejb.context", true);
    jndiProps.put("java.naming.provider.url",
            "http-remoting://localhost:8080");
    return new InitialContext(jndiProps);
  }

  @Bean
  public EasyTicketService easyTicketService(Context context) throws NamingException {
    return (EasyTicketService)
            context.lookup(this.getFullName(EasyTicketService.class));
  }

  private String getFullName(Class classType) {
    String moduleName = "-test/";
    String beanName = classType.getSimpleName();
    String viewClassName = classType.getName();
    return moduleName + beanName + "!" + viewClassName;
  }
}
