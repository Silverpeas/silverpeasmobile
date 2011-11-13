package com.silverpeas.mobile.rebind;

import java.io.PrintWriter;
import java.util.Calendar;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.silverpeas.mobile.client.rebind.ConfigurationProvider;
import com.silverpeas.mobile.server.Configurator;


/**
 * Properties generator.
 * 
 * @author svuillet
 */
public class PropertiesGenerator extends Generator {

	/**
	 * Generate a properties provider.
	 * 
	 * @param logger
	 * @param context
	 * @param typeName class name to substitute
	 * @return created class name
	 * @throws UnableToCompleteException
	 */
	@SuppressWarnings("deprecation")
	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
		ClassSourceFileComposerFactory composer = null;
		SourceWriter out = null;
		try {
			// Retreive properties list
			//ArrayList<Plugin> pluginsAvailables = new ArrayList<Plugin>();

			// Retreive all properties values

			// Generate the source file
			TypeOracle oracle = context.getTypeOracle();
			JClassType type = oracle.getType(typeName);
			PropertyOracle propertyOracle = context.getPropertyOracle();
			String packageName = type.getPackage().getName();
			String simpleName = type.getSimpleSourceName() + "_Generated_" + propertyOracle.getPropertyValue(logger, "user.agent") + "_" + Calendar.getInstance().getTimeInMillis();			
			composer = new ClassSourceFileComposerFactory(packageName, simpleName);
			// Define imports
			composer.addImport("java.util.*");
			// Define implements
			composer.addImplementedInterface(ConfigurationProvider.class.getCanonicalName());
			// Write "getDESKey" method
			PrintWriter printWriter = context.tryCreate(logger, packageName, simpleName);
			out = composer.createSourceWriter(context, printWriter);
			out.println("public String getDESKey() {");
			out.indent();
			out.println("return \"" + Configurator.getConfigValue("des.key") + "\";");
			out.outdent();
			out.println("}");
			out.outdent();
			out.commit(logger);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new UnableToCompleteException();			
		}
		
		return composer.getCreatedClassName();
	}
}
