package bn.blaszczyk.rose.creators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import bn.blaszczyk.rose.MetaData;
import bn.blaszczyk.rose.model.*;
import bn.blaszczyk.roseapp.model.RelationType;

public class JavaControllerCreator {
	

	public static final String SET_METHOD = "setMember";
	public static final String SET_ENTITY_METHOD = "setEntity";
	public static final String ADD_ENTITY_METHOD = "addEntity";
	public static final String DELETE_METHOD = "delete";
	
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
			
			// class declaration
			writer.write("\npublic class " + metadata.getControllerclass() + " implements bn.blaszczyk.roseapp.controller.BasicModelController\n{\n\n");
			
			// implement methods
			
			//public void setMember( Entity entity, String name, Object value);
			writer.write("\t@Override\n\tpublic void setMember( bn.blaszczyk.roseapp.model.Entity entity, String name, Object value)\n\t{\n\t\t" );
			for(Entity entity : entities)
				writer.write("if( entity instanceof " + entity.getClassName() +" )\n\t\t\t" + getControllerName(entity, metadata) + "." 
								+ SET_METHOD +   "( ( " + entity.getClassName() + " ) entity, name, value );\n\t\telse " );
			writer.write("\n\t\t\treturn;\n\t}\n\n" );
			
			//public void setEntityMember( Entity entity, String name, Entity value);
			writer.write("\t@Override\n\tpublic void setEntityMember( bn.blaszczyk.roseapp.model.Entity entity, String name, bn.blaszczyk.roseapp.model.Entity value)\n\t{\n\t\t" );
			for(Entity entity : entities)
				writer.write("if( entity instanceof " + entity.getClassName() +" )\n\t\t\t" + getControllerName(entity, metadata) + "." 
								+ SET_ENTITY_METHOD +   "( ( " + entity.getClassName() + " ) entity, name, value );\n\t\telse " );
			writer.write("\n\t\t\treturn;\n\t}\n\n" );
			
			//public void addEntityMember( Entity entity, String name, Entity value);
			writer.write("\t@Override\n\tpublic void addEntityMember( bn.blaszczyk.roseapp.model.Entity entity, String name, bn.blaszczyk.roseapp.model.Entity value)\n\t{\n\t\t" );
			for(Entity entity : entities)
				writer.write("if( entity instanceof " + entity.getClassName() +" )\n\t\t\t" + getControllerName(entity, metadata) + "." 
								+ ADD_ENTITY_METHOD +   "( ( " + entity.getClassName() + " ) entity, name, value );\n\t\telse " );
			writer.write("\n\t\t\treturn;\n\t}\n\n" );

			//public void delete( Entity entity );
			writer.write("\t@Override\n\tpublic void delete( bn.blaszczyk.roseapp.model.Entity entity )\n\t{\n\t\t" );
			for(Entity entity : entities)
				writer.write("if( entity instanceof " + entity.getClassName() +" )\n\t\t\t" + getControllerName(entity, metadata) + "." 
								+ DELETE_METHOD +   "( ( " + entity.getClassName() + " ) entity );\n\t\telse " );
			writer.write("\n\t\t\treturn;\n\t}\n\n" );

			
			//public Entity createNew( String className);
			writer.write("\t@Override\n\tpublic bn.blaszczyk.roseapp.model.Entity createNew( String className )\n\t{\n\t\tswitch( className.toLowerCase() )\n\t\t{\n" );
			for(Entity entity : entities)
			{
				writer.write("\t\tcase \"" + entity.getSimpleClassName().toLowerCase() + "\":\n\t\t\t" + entity.getClassName() + " " + entity.getObjectName() 
							+ " = new " + entity.getClassName() + "();\n" );
				for(EntityMember entityMember : entity.getEntityMembers())
					if(entityMember.getType() == RelationType.ONETOONE )
						writer.write( "\t\t\t" + entity.getObjectName() + "." + JavaModelCreator.getSetterName(entityMember) + "( (" 
									+ entityMember.getEntity().getClassName() + ") createNew( \"" + entityMember.getEntity().getSimpleClassName() + "\" ) );\n" );
				writer.write("\t\t\treturn " + entity.getObjectName() + ";\n");
			}
			writer.write("\t\t}\n\t\treturn null;\n\t}\n\n");
			
			// public EntityModel createModel( Entity entity );
			writer.write("\t@Override\n\tpublic bn.blaszczyk.roseapp.model.EntityModel createModel( bn.blaszczyk.roseapp.model.Entity entity )\n\t{\n\t\treturn " 
						+ metadata.getEntitymodelpackage() + "." + metadata.getEntitymodelfactoryclass() + ".createModel( entity );\n\t}\n\n");
			
			writer.write("}\n");
			System.out.println( "File created: " + fullpath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	

	public static void create(Entity entity, MetaData metadata)
	{
		String classname = getControllerName(entity, metadata);
		String fullpath = metadata.getSrcpath() + metadata.getControllerpackage().replaceAll("\\.", "/") + "/" + classname + ".java";
		File file = new File(fullpath);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		try(FileWriter writer = new FileWriter(file))
		{
			// initial comment
			writer.write("/*\n * " + classname + ".java\n * generated by rose\n */\n\n");
			
			// package declaration
			writer.write("package " + metadata.getControllerpackage() + ";\n\n");
			
			// class declaration
			writer.write("\npublic class " + classname + "\n{\n");
			
			// setMember
			writer.write("\tpublic static void " + SET_METHOD + "( " + entity.getClassName() + " " + entity.getObjectName() 
						+ ", String name, Object value )\n\t{\n" );
			writer.write("\t\tswitch( name.toLowerCase() )\n\t\t{\n");
			for(Member member : entity.getMembers())
			{
				writer.write("\t\tcase \"" + member.getName().toLowerCase() + "\":\n\t\t\t" + entity.getObjectName() + "." + JavaModelCreator.getSetterName(member) + "( " );
				switch(member.getType())
				{
				case VARCHAR:
				case CHAR:
					writer.write( "value.toString()" );
					break;
				case INT:
					writer.write( "(Integer) value" );
					break;
				case DATE:
					writer.write( "(java.util.Date) value" );
					break;
				case NUMERIC:
					writer.write( "(java.math.BigDecimal) value" );
					break;
				case BOOLEAN:
					writer.write( "(Boolean) value" ) ;
				}
				writer.write( " );\n\t\t\tbreak;\n" );
			}			
			writer.write("\t\tdefault:\n\t\t\tSystem.out.println( \"Unknown Member: \" + name + \" in " + entity.getClassName() + "\");\n" );
			writer.write("\t\t}\n\t}\n\n");

			// setEntity
			writer.write("\tpublic static void " + SET_ENTITY_METHOD + "( " + entity.getClassName() + " " + entity.getObjectName() 
						+ ", String name, Object value )\n\t{\n" );
			writer.write("\t\tswitch( name.toLowerCase() )\n\t\t{\n");
			for(EntityMember entityMember : entity.getEntityMembers())
				if( !entityMember.getType().isSecondMany() )
				{
					writer.write("\t\tcase \"" + entityMember.getName().toLowerCase() + "\":\n"
							+ "\t\t\tif( value instanceof " + entityMember.getEntity().getClassName() + " )\n\t\t\t{\n");
					if(entityMember.getType().isFirstMany())
						writer.write( "\t\t\t\tif(" + entity.getObjectName() + "." + JavaModelCreator.getGetterName(entityMember) + "() != null)\n"
								+ "\t\t\t\t\t" + entity.getObjectName() + "." + JavaModelCreator.getGetterName(entityMember) + "()."
								+ JavaModelCreator.getGetterName(entityMember.getCouterpart()) + "().remove(" + entity.getObjectName() + ");\n"
								+ "\t\t\t\t((" + entityMember.getEntity().getClassName() +  ")value)." 
								+ JavaModelCreator.getGetterName(entityMember.getCouterpart()) + "().add(" + entity.getObjectName() + ");\n");
					writer.write( "\t\t\t\t" + entity.getObjectName() + "." + JavaModelCreator.getSetterName(entityMember) 
							+ "( (" + entityMember.getEntity().getClassName() + ")value );\n" 
							+ "\t\t\t}\n"
							+ "\t\t\telse\n"
							+ "\t\t\t\tSystem.out.println(\"Wrong class for \" + name + \" in " + entity.getClassName() + "\" );\n"
							+ "\t\t\tbreak;\n" );
				}			
			writer.write("\t\tdefault:\n\t\t\tSystem.out.println( \"Unknown Single Entitymember: \" + name + \" in " + entity.getClassName() + "\");\n" );
			writer.write("\t\t}\n\t}\n\n");
			
			// addEntity()
			writer.write("\tpublic static void " + ADD_ENTITY_METHOD + "( " + entity.getClassName() + " " + entity.getObjectName() 
						+ ", String name, Object value )\n\t{\n" );
			writer.write("\t\tswitch( name.toLowerCase() )\n\t\t{\n");
			for(EntityMember entityMember : entity.getEntityMembers())
				if( entityMember.getType().isSecondMany() )
				{
					writer.write("\t\tcase \"" + entityMember.getName().toLowerCase() + "s\":\n"
							+ "\t\t\tif( value instanceof " + entityMember.getEntity().getClassName() + " )\n"
							+ "\t\t\t\t" + getControllerName(entityMember.getEntity(), metadata) + "." + SET_ENTITY_METHOD + "((" 
							+ entityMember.getEntity().getClassName() + ")value, \"" + entityMember.getCounterName() + "\", " + entity.getObjectName() +");\n"
							+ "\t\t\telse\n"
							+ "\t\t\t\tSystem.out.println(\"Wrong class for \" + name + \" in " + entity.getClassName() + "\" );\n"
							+ "\t\t\tbreak;\n" );
				}			
			writer.write("\t\tdefault:\n\t\t\tSystem.out.println( \"Unknown Multiple Entitymember: \" + name + \" in " + entity.getClassName() + "\");\n" );
			writer.write("\t\t}\n\t}\n\n");
			
			// delete()
			writer.write("\tpublic static void " + DELETE_METHOD + "( " + entity.getClassName() + " " + entity.getObjectName() + " )\n\t{\n");
			for(EntityMember entityMember : entity.getEntityMembers())
				switch(entityMember.getType())
				{
				case MANYTOONE:
					writer.write("\t\tif(" + entity.getObjectName() +"." + JavaModelCreator.getGetterName(entityMember) + "() != null)\n"
							+ "\t\t\t" + entity.getObjectName() +"." + JavaModelCreator.getGetterName(entityMember) 
							+ "()." + JavaModelCreator.getGetterName(entityMember.getCouterpart()) + "().remove( " + entity.getObjectName() +" );\n");
					break;
				case ONETOMANY:
					writer.write( "\t\tfor( " + entityMember.getEntity().getClassName() + " " + entityMember.getName() + " : " + entity.getObjectName() +"." 
							+ JavaModelCreator.getGetterName(entityMember) + "() )\n"
							+ "\t\t\t" + JavaControllerCreator.getControllerName(entityMember.getEntity(), metadata) + "." + DELETE_METHOD + "( " + entityMember.getName() + " );\n");
					break;
				case ONETOONE:
					writer.write( "\t\t" + JavaControllerCreator.getControllerName(entityMember.getEntity(), metadata) + "." + DELETE_METHOD + "( " 
							+ entity.getObjectName() + "." + JavaModelCreator.getGetterName(entityMember) + "() );\n");
					break;
				case MANYTOMANY:
					break;
				}
			writer.write( "\t}\n\n");
			
			writer.write("}\n");
			System.out.println( "File created: " + fullpath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}


	public static String getControllerName(Entity entity, MetaData metadata)
	{
		return String.format(metadata.getControllerformat(), entity.getSimpleClassName());
	}
	
	
}
