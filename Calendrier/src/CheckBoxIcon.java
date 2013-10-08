/**
 * 
 * Create plain color for Jchekboxes
 * bb - october 2013
 * 
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class CheckBoxIcon implements Icon {
    public Color colEdgeu;
    public Color colFillu;
  
	public void paintIcon(Component component, Graphics g, int x, int y) {
    //AbstractButton abstractButton = (AbstractButton)component;
    //ButtonModel buttonModel = abstractButton.getModel();
    //if (buttonModel.isSelected()) not needed for this use
    {
       g.setColor(colFillu);
        g.fillRect(5, 6, 12, 12); 
        g.setColor(colEdgeu);
        g.drawRect(4, 5, 13,13);
        
    }
  }
  public int getIconWidth() {
    return 14;
  }
  public int getIconHeight() {
    return 14;
  }
  
}
