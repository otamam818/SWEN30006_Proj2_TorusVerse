package src.portals;

import java.awt.*;

public class WhitePortals extends AbstractPortals {
  public WhitePortals() {
    super();
  }

  @Override
  protected String getKey() {
    return "i_portalWhiteTile";
  }

  @Override
  protected Color getPaintColor() {
    return Color.white;
  }
}
