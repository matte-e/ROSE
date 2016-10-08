package bn.blaszczyk.rose.creators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import bn.blaszczyk.rose.*;
import bn.blaszczyk.rose.model.*;


public class CreateJavaModel {

	public static void createModel(Entity entity, MetaData metadata)
	{
		String fullpath = metadata.getSrcpath() + metadata.getModelpackage().replaceAll("\\.", "/") + "/" + entity.getClassname() + ".java";
		File file = new File(fullpath);
		if(!file.getParentFile().exists())
			file.getParentFile().mkdirs();
		try(FileWriter writer = new FileWriter(file))
		{
			// initial comment
			writer.write("/*\n * " + entity.getClassname() + ".java\n * generated by rose\n */\n\n");
			
			// package declaration
			writer.write("package " + metadata.getModelpackage() + ";\n\n");
			
			// imports
			if(metadata.isUsingAnnotations())
				writer.write("import javax.persistence.*;\n\n");
			for(String importpackage : entity.getImports())
				if(importpackage != null)
					writer.write("import " + importpackage + ";\n");
			
			// annotations
			if(metadata.isUsingAnnotations())
				writer.write("\n@Entity\n@Table(name=\"" + entity.getClassname() + "\")");
			
			// class declaration
			writer.write("\npublic class " + entity.getClassname() + "\n{");
			
			// member variables
			for(Member member : entity.getMembers())
			{
				// annotations
				if(metadata.isUsingAnnotations())
				{
					if(member.isPrimary())
						writer.write("\n\t@Id\n\t@GeneratedValue");
					writer.write("\n\t@Column(name=\"" + member.getName() + "\")");	
				}
				// declaration
				writer.write("\n\tprivate " + member.getType().getJavaname() + " " + member.getName() );
				if(member.isPrimary())
					writer.write( " = " + member.getType().getDefValue());
				else if(member.getDefvalue() != null && member.getDefvalue() != "" )
					writer.write( " = " + String.format( member.getType().getDefFormat(), member.getDefvalue() ) );
				else
					writer.write( " = " + member.getType().getDefValue() );
				writer.write( ";\n" );
			}
			
			// entitymember variables
			for(EntityMember entitymember : entity.getEntityMembers())
			{
				if(metadata.isUsingAnnotations())
				{
					if(entitymember.isMany())
						writer.write("\n\t@OneToMany(mappedBy=\"" + entitymember.getCouterpart().getName() + "\")");
					else
						writer.write("\n\t@ManyToOne\n\t@JoinColumn(name=\"" + entitymember.getName() + "\")" );
				}
				if(entitymember.isMany())
					writer.write("\n\tprivate Set<" + entitymember.getEntity().getClassname() + "> " + entitymember.getName() + "s = new HashSet<>();\n");
				else
					writer.write("\n\tprivate " + entitymember.getEntity().getClassname() + " " + entitymember.getName() + ";\n");
			}
	
			// default constructor
			writer.write("\n\n\tpublic " + entity.getClassname() + "()\n\t{\n\t}\n\n");
			
			// constructor for non-primaries
			if( entity.getMembers().size() > 1 )
			{
				writer.write("\tpublic " + entity.getClassname() + "( ");
				boolean first = true;
				for(Member member : entity.getMembers())
				{
					if(member.isPrimary())
						continue;
					if(!first)
						writer.write(", ");
					else
						first = false;
					writer.write( member.getType().getJavaname() + " " + member.getName());
				}
				writer.write(" )\n\t{\n");
				for(Member member : entity.getMembers())
					if(!member.isPrimary())
						writer.write("\t\tthis." + member.getName() + " = " + member.getName() + ";\n");
				writer.write("\t}\n\n");
			}

			// for each member:
			for(Member member : entity.getMembers())
			{				
				// getter
				writer.write("\tpublic " + member.getType().getJavaname());
				if(member.getType().equals(MemberType.BOOLEAN))
					writer.write(" is");
				else
					writer.write(" get");
				writer.write( member.getCapitalName() + "()\n\t{\n\t\treturn " + member.getName() + ";\n\t}\n" );
				
				// setter
				writer.write("\n\tpublic void set" + member.getCapitalName() + "( " + member.getType().getJavaname() + " " 
							+ member.getName() + " )\n\t{\n\t\tthis." + member.getName() + " = " + member.getName() + ";\n\t}\n\n" );
			}

			// for each entitymember:
			for(EntityMember entityMember : entity.getEntityMembers())
			{
				// for Lists
				if(entityMember.isMany())
				{
					// getter
					writer.write("\tpublic Set<" + entityMember.getEntity().getClassname() + "> get" + entityMember.getCapitalName() 
								+ "s()\n\t{\n\t\treturn " + entityMember.getName() + "s;\n\t}\n" );
				
					// setter
					writer.write("\n\tpublic void set" + entityMember.getCapitalName()
					+ "s( Set<" + entityMember.getEntity().getClassname() + "> " + entityMember.getName() 
					+ "s )\n\t{\n\t\tthis." + entityMember.getName() + "s = " + entityMember.getName() + "s;\n\t}\n\n" );
				}
				// Singles
				else
				{
					// getter
					writer.write("\tpublic " + entityMember.getEntity().getClassname() + " get" + entityMember.getCapitalName() 
								+ "()\n\t{\n\t\treturn " + entityMember.getName() + ";\n\t}\n" );
				
					// setter
					writer.write("\n\tpublic void set" + entityMember.getCapitalName() + "( " + entityMember.getEntity().getClassname() 
							+ " " 	+ entityMember.getName() + " )\n\t{\n\t\tthis." + entityMember.getName() + " = " 
							+ entityMember.getName() + ";\n\t}\n\n" );
				}
			}
			// TODO? toString, equals, hashValue	
			// fin
			writer.write("}\n");
			System.out.println( "File created: " + fullpath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
}
