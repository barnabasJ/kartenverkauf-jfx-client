package at.fhv.teama.easyticket.client.jfx.views.Login;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class User {
  private String username;
  private Set<String> roles;
}
