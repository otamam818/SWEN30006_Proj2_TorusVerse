package src.portals;

import java.awt.*;

public class DarkGoldPortals extends AbstractPortals {
  public DarkGoldPortals() {
    super();
  }

  @Override
  protected String getKey() {
    return "k_portalDarkGoldTile";
  }

  @Override
  protected Color getPaintColor() {
    return Color.orange;
  }
}
