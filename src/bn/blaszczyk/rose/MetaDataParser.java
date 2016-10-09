/*
 * MetaDataParser.java
 * generated by rose
 */

package bn.blaszczyk.rose;


public class MetaDataParser
{
	public static void parseProperty( MetaData metaData, String property, String value )
	{
		switch( property.toLowerCase() )
		{
		case "metadata_id":
			metaData.setMetaData_id( Integer.parseInt( value ) );
			break;
		case "modelpackage":
			metaData.setModelpackage( value );
			break;
		case "panelpackage":
			metaData.setPanelpackage( value );
			break;
		case "fullpanelformat":
			metaData.setFullpanelformat( value );
			break;
		case "simplepanelformat":
			metaData.setSimplepanelformat( value );
			break;
		case "srcpath":
			metaData.setSrcpath( value );
			break;
		case "sqlpath":
			metaData.setSqlpath( value );
			break;
		case "usingforeignkeys":
			metaData.setUsingForeignKeys( Boolean.parseBoolean( value ) );
			break;
		case "usingannotations":
			metaData.setUsingAnnotations( Boolean.parseBoolean( value ) );
			break;
		case "database":
			metaData.setDatabase( value );
			break;
		case "basicpanelclass":
			metaData.setBasicpanelclass( value );
			break;
		case "fullpanelclass":
			metaData.setFullpanelclass( value );
			break;
		default:
			System.out.println( "Unknown Property: " + property + " in MetaData");
		}
	}
}