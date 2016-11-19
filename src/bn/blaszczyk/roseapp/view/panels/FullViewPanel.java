package bn.blaszczyk.roseapp.view.panels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import bn.blaszczyk.roseapp.controller.GUIController;
import bn.blaszczyk.roseapp.model.*;
import bn.blaszczyk.roseapp.model.Readable;
import bn.blaszczyk.roseapp.view.tools.EntityTable;
import bn.blaszczyk.roseapp.view.tools.EntityTableModel;

@SuppressWarnings("serial")
public class FullViewPanel extends AlignPanel {
	
	private Readable entity;

	
	public FullViewPanel( Readable entity, GUIController guiController, boolean showTitle )
	{
		super( guiController);
		this.entity = entity;
		if(showTitle)
			setTitle( entity.getId() > 0 ? entity.getEntityName() + " " + entity.getId() : "new " + entity.getEntityName() );
		addBasicPanel(null, null, entity);
		for(int i = 0; i < entity.getEntityCount(); i++)
		{
			if( entity.getEntityValue(i) == null )
				continue;
			switch( entity.getRelationType(i) )
			{
			case MANYTOMANY:
			case ONETOMANY:
				if(!((Set<?>)entity.getEntityValue(i)).isEmpty())
					addEntityTable(i);
				break;
			case MANYTOONE:
				if(entity.getEntityValue(i)!= null)
					addBasicPanel( entity.getEntityName(i), createViewButton(i), (Readable) entity.getEntityValue(i) );
				break;
			case ONETOONE:
				if(entity.getEntityValue(i)!= null)
					addFullPanel( null, null, (Readable) entity.getEntityValue(i)  );
				break;
			}
		}
		realign();
		
	}

	private JButton createViewButton( int index )
	{	
		JButton button = null;
		if(entity.getRelationType(index).equals(RelationType.MANYTOONE))
		{
			button = new JButton("View");
			try
			{
				button.setIcon( new ImageIcon(ImageIO.read(getClass().getClassLoader().getResourceAsStream("bn/blaszczyk/roseapp/resources/view.png"))) );
			}
			catch (IOException e)
			{	
				e.printStackTrace();
			}
			button.addActionListener( e -> guiController.openEntityTab( (Readable) entity.getEntityValue(index) , false) );
		}		
		return button;
	}
	
	
	private void addBasicPanel( String title, JButton button,  Readable entity )
	{	
		super.addPanel( title, button, new BasicViewPanel(entity));
	}
	
	private void addFullPanel( String title, JButton button, Readable entity )
	{
		super.addPanel( title, button, new BasicViewPanel(entity) );
	}
	
	@SuppressWarnings("unchecked")
	private void addEntityTable( int index )
	{
		List<Readable> entities = new ArrayList<>();
		entities.addAll( (Set<? extends Readable>) entity.getEntityValue(index) );
		EntityTableModel<Readable> tableModel = new EntityTableModel<>(entities,1);
		EntityTable<Readable> table = new EntityTable<>( tableModel, BASIC_WIDTH, SUBTABLE_HEIGTH );
		table.setButtonColumn(0, "view.png", e -> guiController.openEntityTab( e, false ));
		JScrollPane scrollPane = new JScrollPane(table);
		
		super.addPanel( entity.getEntityName(index), createViewButton(index), scrollPane, BASIC_WIDTH, SUBTABLE_HEIGTH);
	}

	@Override
	public JPanel getPanel()
	{
		return this;
	}
	
	@Override
	public Object getShownObject()
	{
		return entity;
	}

	@Override
	public boolean hasChanged()
	{
		return false;
	}
	
	
}
