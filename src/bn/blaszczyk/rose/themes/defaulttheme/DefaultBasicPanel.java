package bn.blaszczyk.rose.themes.defaulttheme;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import bn.blaszczyk.rose.themes.BasicPanel;

@SuppressWarnings("serial")
public class DefaultBasicPanel extends JPanel implements BasicPanel {

	private static final int LBL_PROPERTY_WIDTH = 150;
	private static final int LBL_VALUE_WIDTH = 300;
	private static final int LBL_HEIGHT = 30;
	private static final int H_SPACING = 10;
	private static final int V_SPACING = 10;

	private static final Font PROPERTY_FONT = new Font("Arial", Font.BOLD, 20);
	private static final Font VALUE_FONT = new Font("Arial", Font.BOLD, 20);

	private static final Color PROPERTY_BG = Color.BLACK;
	private static final Color PROPERTY_FG = Color.RED;
	private static final Color VALUE_BG = Color.BLACK;
	private static final Color VALUE_FG = Color.GREEN;
	
	private static final Color BACKGROUND = Color.DARK_GRAY;
	
	private int width = 3 * H_SPACING + LBL_PROPERTY_WIDTH + LBL_VALUE_WIDTH;
	private int height = V_SPACING;

	public DefaultBasicPanel()
	{
		setLayout(null);
		setBackground(BACKGROUND);
	}

	@Override
	public void addValue(String property, String value)
	{
		JLabel lblProperty = new JLabel(property + ": ", SwingConstants.RIGHT);
		lblProperty.setBounds( H_SPACING, height, LBL_PROPERTY_WIDTH, LBL_HEIGHT );
		lblProperty.setFont(PROPERTY_FONT);
		lblProperty.setOpaque(true);
		lblProperty.setForeground(PROPERTY_FG);
		lblProperty.setBackground(PROPERTY_BG);
		add(lblProperty);
		
		JLabel lblValue = new JLabel(value);
		lblValue.setBounds( 2 * H_SPACING + LBL_PROPERTY_WIDTH , height, LBL_VALUE_WIDTH, LBL_HEIGHT);
		lblValue.setFont(VALUE_FONT);
		lblValue.setOpaque(true);
		lblValue.setForeground(VALUE_FG);
		lblValue.setBackground(VALUE_BG);
		add(lblValue);
				
		height += LBL_HEIGHT + V_SPACING;
	}

	@Override
	public int getWidth()
	{
		return width;
	}

	@Override
	public int getHeight()
	{
		return height;
	}

	@Override
	public JPanel getPanel()
	{
		return this;
	}
	
}