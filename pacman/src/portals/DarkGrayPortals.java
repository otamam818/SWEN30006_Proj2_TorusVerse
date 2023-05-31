package src.portals;

import java.awt.*;

public class DarkGrayPortals extends AbstractPortals {
  public DarkGrayPortals() {
    super();
  }

  @Override
  protected String getKey() {
    return "l_portalDarkGrayTile";
  }

  @Override
  protected Color getPaintColor() {
    return Color.darkGray;
  }
}
