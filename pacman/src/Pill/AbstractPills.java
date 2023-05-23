package src.Pill;

import ch.aplu.jgamegrid.Location;
import src.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Describes a set of pills of any kind, differentiated by their
 * `key` value
 */
public abstract class AbstractPills {
  private final List<Location> locations = new ArrayList<>();
  public AbstractPills() {
    String propertyName = getKey().concat(".location");
    String pillsLocationString = Game
            .getInstance()
            .getProperties()
            .getProperty(propertyName);
    if (pillsLocationString != null) {
      String[] singlePillLocationStrings = pillsLocationString.split(";");
      for (String singlePillLocationString: singlePillLocationStrings) {
        String[] locationStrings = singlePillLocationString.split(",");
        locations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
      }
    }
  }

  protected abstract String getKey();

  public List<Location> getLocations() {
    return locations;
  }
}
