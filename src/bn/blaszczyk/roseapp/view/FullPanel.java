package bn.blaszczyk.roseapp.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import bn.blaszczyk.roseapp.controller.GUIController;
import bn.blaszczyk.roseapp.model.*;

@SuppressWarnings("serial")
public class FullPanel extends JPanel implements MyPanel, ThemeConstants {


	private int width = 2 * H_SPACING;
	private int height = V_SPACING;
	
	private EntityModel entityModel;
	private GUIController controller;

	
	public FullPanel( EntityModel entityModel, GUIController controller )
	{
//		System.out.println(entityModel.getEntity().hashCode() + "\t\t" + entityModel);
		this.controller = controller;
		this.entityModel = entityModel;
		setLayout(null);
		setBackground(FULL_PNL_BACKGROUND);
		addTitle( entityModel );
		addBasicPanel(entityModel);
		for(int i = 0; i < entityModel.getEntityCount(); i++)
		{
			if( entityModel.getEntityMember(i) == null )
				continue;
			else if( entityModel.getRelationType(i).isSecondMany())
			{
				List<EntityModel> entityModels = new ArrayList<>();
				Set<?> entities =  (Set<?>) entityModel.getEntityMember(i);
				for(Object entity : entities)
					entityModels.add(entityModel.createModel((Entity)entity));
				addSubTitle( entityModel.getEntityName(i) );
				addMemberTable( entityModels );
			}
			else
			{
				EntityModel subEntityModel = entityModel.createModel( (Entity) entityModel.getEntityMember(i) );
				addSubTitle( entityModel.getEntityName(i), subEntityModel );
				addBasicPanel( subEntityModel );
			}
		}
		
	}

	private void addTitle( EntityModel entityModel )
	{
		height += V_OFFSET;

		JLabel lblTitle = new JLabel( entityModel.getId() > 0 ? entityModel.getName() + " " + entityModel.getId() : "new " + entityModel.getName() );
		lblTitle.setFont(TITLE_FONT);
		lblTitle.setForeground(TITLE_FG);
		lblTitle.setBackground(TITLE_BG);
		lblTitle.setBounds(H_SPACING, height, TITLE_WIDTH, TITLE_HEIGHT);
		lblTitle.setOpaque(true);
		add(lblTitle);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.setBounds(2 * H_SPACING + SUBTITLE_WIDTH, height, 100, SUBTITLE_HEIGHT);
		final EntityModel entityModelCpy = entityModel;
		btnEdit.addActionListener( e -> {
			controller.createEditPanelDialog(entityModelCpy);
		} );
		add(btnEdit);

		computeDimensions(TITLE_HEIGHT, TITLE_WIDTH);				
	}

	private void addSubTitle( String subtitle, EntityModel entityModel )
	{	
		JButton btnView = new JButton("View");
		btnView.setBounds(2 * H_SPACING + SUBTITLE_WIDTH, height + V_OFFSET, 100, SUBTITLE_HEIGHT);
		final EntityModel entityModelCpy = entityModel;
		btnView.addActionListener( e -> controller.createFullPanelDialog(entityModelCpy) );
		add(btnView);
		
		addSubTitle(subtitle);		
	}
	private void addSubTitle( String subtitle )
	{
		height += V_OFFSET;
		JLabel lblSubTitle = new JLabel( subtitle );
		lblSubTitle.setFont(SUBTITLE_FONT);
		lblSubTitle.setForeground(SUBTITLE_FG);
		lblSubTitle.setBackground(SUBTITLE_BG);
		lblSubTitle.setBounds(H_SPACING, height, SUBTITLE_WIDTH, SUBTITLE_HEIGHT);
		lblSubTitle.setOpaque(true);
		add(lblSubTitle);
		
		computeDimensions(TITLE_HEIGHT, TITLE_WIDTH);		
	}
	
	private void addBasicPanel( EntityModel entityModel )
	{	
		MyPanel myPanel = new BasicPanel(entityModel) ;
		JPanel panel = myPanel.getPanel();
		panel.setBounds(H_SPACING, height, myPanel.getWidth() , myPanel.getHeight() );
		add(panel);
		computeDimensions( myPanel.getHeight(), myPanel.getWidth() );
	}
	
	private void addMemberTable( List<EntityModel> entityModels )
	{
		MemberTableModel tableModel = new MemberTableModel(entityModels,1);
		MemberTable table = new MemberTable( tableModel, controller );
		table.setButtonColumn(0, "view.png", e -> controller.createFullPanelDialog( e ));
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(H_SPACING, height, table.getWidth(), table.getHeight());
		add(scrollPane);
		computeDimensions( scrollPane.getHeight(), scrollPane.getWidth() );
	}
	
	private void computeDimensions( int height, int width )
	{
		this.width = Math.max(this.width, 2 * H_SPACING + width);
		this.height += V_SPACING + height;
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
	
	@Override
	public Object getShownObject()
	{
		return entityModel.getEntity();
	}
	
}