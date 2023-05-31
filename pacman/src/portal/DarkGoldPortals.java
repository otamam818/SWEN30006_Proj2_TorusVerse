package src.portal;

import java.awt.*;

public class DarkGoldPortals extends AbstractPortals {
  public DarkGoldPortals() {
    super();
  }

  @Override
  protected String getKey() {
    return "DarkGold";
  }

  @Override
  protected Color getPaintColor() {
    return Color.orange;
  }
}
