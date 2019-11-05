package at.fhv.teama.easyticket.client.jfx.views;

import at.fhv.teama.easyticket.dto.PersonDto;
import at.fhv.teama.easyticket.dto.VenueDto;
import javafx.stage.Modality;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Model {

    private static Model modelInstance;

    private VenueDto selectedVenue;
    private PersonDto selectedPerson;


    private Model (){}

    public static Model getInstance(){
        if (modelInstance ==null){
            modelInstance = new Model();
        }

        return modelInstance;
    }

}
