package com.rogueai.snmp2bean.codegen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import net.percederberg.mibble.Mib;
import net.percederberg.mibble.MibLoader;
import net.percederberg.mibble.MibLoaderException;
import net.percederberg.mibble.MibValueSymbol;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.rogueai.snmp2bean.codegen.model.MibContainerNode;
import com.rogueai.snmp2bean.codegen.model.builder.GenModelBuilder;

/**
 * 
 * @author zugnom
 * 
 */
public class Generator {

	public static void main(String[] args) throws IOException, MibLoaderException {
		MibLoader loader = new MibLoader();

		File file = new File("/home/zugnom/dev/etc/isilon/ISILON-MIB.txt");
		loader.addDir(file.getParentFile());
		Mib mib = loader.load(file);
		MibValueSymbol root = mib.getRootSymbol();

		GenModelBuilder builder = new GenModelBuilder();
		List<MibContainerNode> parents = builder.build(root);
		generate(parents);
	}

	public static void generate(List<MibContainerNode> parents) {

		for (MibContainerNode node : parents) {
			VelocityEngine engine = new VelocityEngine();
			engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
			engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
			engine.init();

			VelocityContext context = new VelocityContext();
			String packageName = "com.vce.fm.isilon.snmp.model";
			context.put("package", packageName);
			context.put("node", node);

			StringBuilder sb = new StringBuilder();
			sb.append("/home/zugnom/dev/etc/isilon/codegen/");
			sb.append(packageName.replaceAll("\\.", "/"));
			sb.append("/");
			sb.append(node.getCapitalizedName());
			sb.append(".java");

			try {
				Path classFile = Paths.get(sb.toString());
				Files.createDirectories(classFile.getParent());
				try (BufferedWriter writer = Files.newBufferedWriter(classFile, StandardCharsets.UTF_8)) {
					Template template = engine.getTemplate("mib-class.vm");
					template.merge(context, writer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
