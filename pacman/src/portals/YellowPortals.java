package src.portals;

import java.awt.*;

public class YellowPortals extends AbstractPortals {
  public YellowPortals() {
    super();
  }

  @Override
  protected String getKey() {
    return "j_portalYellowTile";
  }

  @Override
  protected Color getPaintColor() {
    return Color.yellow;
  }
}
