package at.fhv.teama.easyticket.client.jfx;

import at.fhv.teama.easyticket.client.jfx.views.Login.LoginView;
import com.airhacks.afterburner.injection.Injector;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.security.*;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class EasyTicketJfxClientApplication extends Application {

  private ConfigurableApplicationContext context;

  public static void main(String[] args) {
    launch(args);
  }

  private void setPolicy() {
    Policy.setPolicy(
        new Policy() {
          @Override
          public PermissionCollection getPermissions(CodeSource codesource) {
            Permissions p = new Permissions();
            p.add(new AllPermission());
            return p;
          }
        });
  }

  @Override
  public void init() {
    setPolicy();
    SpringApplicationBuilder builder =
        new SpringApplicationBuilder(EasyTicketJfxClientApplication.class);
    context = builder.run();
    Injector.setInstanceSupplier(context::getBean);
  }

  @Override
  public void start(Stage primaryStage) {
    LoginView view = new LoginView();
    Scene scene = new Scene(view.getView());

    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
    double width = visualBounds.getWidth();
    double height = visualBounds.getHeight();

    primaryStage.setScene(scene);
    primaryStage.setWidth(width);
    primaryStage.setHeight(height);
    primaryStage.show();
  }

  @Override
  public void stop() {
    context.close();
  }
}
