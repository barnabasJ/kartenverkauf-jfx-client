package at.fhv.teama.easyticket.client.jfx.views.Warenkorb;

import at.fhv.teama.easyticket.client.jfx.views.Model;
import at.fhv.teama.easyticket.dto.TicketDto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
@RequiredArgsConstructor
@Slf4j
public class WarenkorbController implements Initializable {



    @FXML
    private ListView<?> TicketList;

    @FXML
    private Button DeleteCart_Button;

    @FXML
    private Button Reserve_Button;

    @FXML
    private Button Buy_Button;

    @FXML
    private Label SumLabel;

    @FXML
    private Label Person_Label;



    private Model model = Model.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
