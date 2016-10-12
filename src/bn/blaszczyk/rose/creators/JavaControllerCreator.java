package bn.blaszczyk.rose.creators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.model.*;

public class JavaControllerCreator {
	
	public static void create(List<Entity> entities, MetaData metadata)
	{
		String fullpath = metadata.getSrcpath() + metadata.getControllerpackage().replaceAll("\\.", "/") + "/" + metadata.getControllerclass() + ".java";
		File file = new File(fullpath);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		try(FileWriter writer = new FileWriter(file))
		{
			// initial comment
			writer.write("/*\n * " + metadata.getControllerclass() + ".java\n * generated by rose\n */\n\n");
			
			// package declaration
			writer.write("package " + metadata.getControllerpackage() + ";\n\n");
			
			// imports
			writer.write("import " + metadata.getParserpackage() + ".*;\n");
			writer.write("import java.text.ParseException;\n");
			writer.write("import " + metadata.getModelpackage() + ".*;\n"
						+ "import bn.blaszczyk.rose.interfaces.*;" );
			
			// class declaration
			writer.write("\npublic class " + metadata.getControllerclass() + " implements ModelController\n{\n\n");
			
			// implement methods
			
			//public void setMember( Object entity, String name, Object value);
			writer.write("\t@Override\n\tpublic void setMember( Object entity, String name, Object value) throws ParseException\n\t{\n\t\t" );
			for(Entity entity : entities)
				writer.write("if( entity instanceof " + entity.getClassname() +" )\n\t\t\t" + JavaParserCreator.getParserName(entity, metadata) + "." 
								+ JavaParserCreator.SET_METHOD +   "( ( " + entity.getClassname() + " ) entity, name, value );\n\t\telse " );
			writer.write("\n\t\t\treturn;\n\t}\n\n" );
			
			//public void setEntityMember( Object entity, String name, Object value);
			writer.write("\t@Override\n\tpublic void setEntityMember( Object entity, String name, Object value) throws ParseException\n\t{\n\t\t" );
			for(Entity entity : entities)
				writer.write("if( entity instanceof " + entity.getClassname() +" )\n\t\t\t" + JavaParserCreator.getParserName(entity, metadata) + "." 
								+ JavaParserCreator.SET_ENTITY_METHOD +   "( ( " + entity.getClassname() + " ) entity, name, value.toString() );\n\t\telse " );
			writer.write("\n\t\t\treturn;\n\t}\n\n" );
			
			//public void addEntityMember( Object entity, String name, Object value);
			writer.write("\t@Override\n\tpublic void addEntityMember( Object entity, String name, Object value) throws ParseException\n\t{\n\t\t" );
			for(Entity entity : entities)
				writer.write("if( entity instanceof " + entity.getClassname() +" )\n\t\t\t" + JavaParserCreator.getParserName(entity, metadata) + "." 
								+ JavaParserCreator.ADD_ENTITY_METHOD +   "( ( " + entity.getClassname() + " ) entity, name, value.toString() );\n\t\telse " );
			writer.write("\n\t\t\treturn;\n\t}\n\n" );

			//public void deleteEntityMember( Object entity, String name, Object value);
			writer.write("\t@Override\n\tpublic void deleteEntityMember( Object entity, String name, Object value) throws ParseException\n\t{\n\t\t" );
			for(Entity entity : entities)
				writer.write("if( entity instanceof " + entity.getClassname() +" )\n\t\t\t" + JavaParserCreator.getParserName(entity, metadata) + "." 
								+ JavaParserCreator.DEL_ENTITY_METHOD +   "( ( " + entity.getClassname() + " ) entity, name, value.toString() );\n\t\telse " );
			writer.write("\n\t\t\treturn;\n\t}\n\n" );
			
			//public void commit();
			writer.write("\t@Override\n\tpublic void commit()\n\t{\n\t}\n\n" );
			
			writer.write("}\n");
			System.out.println( "File created: " + fullpath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	
}
