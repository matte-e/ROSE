package bn.blaszczyk.roseapp.model;


public interface EntityModel {

	public String getName();
	public int getId();
	public Entity getEntity();
	
	public int getMemberCount();
	public String getMemberName( int index );
	public Object getMemberValue( int index );
	
	public int getEntityCount();
	public Object getEntityMember( int index );
	public String getEntityName( int index );
	public RelationType getRelationType( int index );

	public int getLength1( int index );	
	public int getLength2( int index );	
	// to create instances is Factory
	public EntityModel createModel( Entity entity );
	
	/*
	 *  TODO: setters (parser?, controller?)
	 */
}