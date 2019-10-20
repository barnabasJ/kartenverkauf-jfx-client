package at.fhv.teama.kartenverkauf.client.jfx;

import at.fhv.teama.kartenverkauf.client.jfx.views.main.MainView;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.airhacks.afterburner.injection.Injector;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class KartenverkaufClientApplication extends Application {

  private ConfigurableApplicationContext context;

  @Override
  public void init() throws Exception {
    SpringApplicationBuilder builder = new SpringApplicationBuilder(KartenverkaufClientApplication.class);
    context = builder.run();
    Injector.setInstanceSupplier(context::getBean);
  }

  @Override
  public void start(Stage primaryStage) {
    MainView view = new MainView();
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
  public void stop() throws Exception {
    context.close();
  }
}
