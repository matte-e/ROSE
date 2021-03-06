package bn.blaszczyk.rose.model;

import java.text.ParseException;

public class PrimitiveField implements Field{
	private String name;
	private String capitalName;
	private String sqlType;
	private PrimitiveType type = null;
	private String defValue = null;
	
	private int length1 = 1;
	private int length2 = 0;
	
	public PrimitiveField( String sqltype, String name, String defvalue) throws ParseException
	{
		this.name = name;
		this.capitalName = name.substring(0,1).toUpperCase() + name.substring(1);
		this.sqlType = sqltype;
		this.defValue = defvalue;
		for(PrimitiveType primitiveType : PrimitiveType.values() )
			if( sqltype.toLowerCase().startsWith( primitiveType.getSqlname().toLowerCase() ) )
				this.type = primitiveType;
		String[] split;
		switch(type)
		{
		case VARCHAR:
		case CHAR:
			split = sqltype.split("\\(|\\)");
			if(split.length > 1)
				length1 = Integer.parseInt(split[1]);
			break;
		case NUMERIC:
			split = sqltype.split("\\(|\\)|\\,|\\.");
			if(split.length > 2)
			{
				length1 = Integer.parseInt(split[1]);
				length2 = Integer.parseInt(split[2]);
			}
			break;
		case DATE:
		case BOOLEAN:
		case INT:
			break;
		}
		if(type == null)
			throw new ParseException("Unknown SQL type: " + sqltype, 0);
	}

	public PrimitiveField( String sqltype, String name ) throws ParseException
	{
		this(sqltype,name,null);
	}

	public String getDefValue()
	{
		if(defValue == null || defValue == "")
			return getType().getDefValue();
		return defValue;
	}

	public void setDefValue(String defValue)
	{
		this.defValue = defValue;
	}

	@Override
	public String getName()
	{
		return name;
	}
	
	@Override
	public String getCapitalName()
	{
		return capitalName;
	}

	@Override
	public String getSqlType()
	{
		return sqlType;
	}

	public PrimitiveType getType()
	{
		return type;
	}

	public int getLength1()
	{
		return length1;
	}

	public int getLength2()
	{
		return length2;
	}
	
	@Override
	public String toString()
	{
		return capitalName;
	}
	
	
	
	
}
