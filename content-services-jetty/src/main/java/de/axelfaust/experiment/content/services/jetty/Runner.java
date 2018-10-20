package de.axelfaust.experiment.content.services.jetty;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * @author Axel Faust
 */
public class Runner {
	public static int DEFAULT_PORT = 8080;
	private static String alfresco_configurationpath = "./config/content-services";
	private static String alfresco_favicon = "webapps/alfresco/favicon.ico";
	private static String alfresco_contextpath = "/alfresco";
	
	private static final String USAGE_HEADER = "\nAlfresco Content Services\n\n";
	private static final String USAGE_FOOTER = "\nFurther information: https://github.com/Alfresco/acs-community-packaging";

	public static void main(final String[] args) throws Exception {
		Option o_port = Option.builder("p")
				.desc("Port [integer]. Default " + DEFAULT_PORT)
				.longOpt("port")
				.hasArg()
				.type(Integer.class)
				.build();

		Option o_cp = Option.builder("c")
				.longOpt("config")
				.desc("Configuration path containing alfresco-global.properties [string]. Default '" + alfresco_configurationpath + "'")
				.hasArg()
				.build();
		Option o_favicon = Option.builder("favicon")
				.desc("Alfresco favicon [string]. Default '" + alfresco_favicon + "'")
				.hasArg()
				.build();
		Option o_context = Option.builder("contextpath")
				.desc("Alfresco webapp context path [string]. Default '" + alfresco_contextpath + "'")
				.hasArg()
				.build();

		Option o_help = Option.builder("h")
				.desc("This information")
				.longOpt("help")
				.build();

		Options options = new Options();
		options.addOption(o_port);
		options.addOption(o_help);

		options.addOption(o_cp);
		options.addOption(o_context);
		options.addOption(o_favicon);

		try {

			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("h")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("java -jar <jar>", USAGE_HEADER, options, USAGE_FOOTER, true);
				return;
			}

			int port = DEFAULT_PORT;
			if (cmd.hasOption("p")) {
				try {
					port = Integer.parseInt(cmd.getOptionValue("p"));
				} catch (NumberFormatException e) {
					throw new ParseException("Parameter 'p' not an integer");
				}
			}
			if (cmd.hasOption("c")) {
				alfresco_configurationpath = cmd.getOptionValue("c");
			}
			if (cmd.hasOption("favicon")) {
				alfresco_favicon = cmd.getOptionValue("favicon");
			}
			if (cmd.hasOption("contextpath")) {
				alfresco_contextpath = cmd.getOptionValue("contextpath");
			}

			final Server server = new Server(port);
			System.out.println("Running on port: " + port);
			System.out.println("Alfresco: " + alfresco_contextpath + "\n - configuration path = " + alfresco_configurationpath + "\n - favicon = " + alfresco_favicon);

			final Configuration.ClassList classlist = Configuration.ClassList.setServerDefault(server);
			classlist.addBefore(org.eclipse.jetty.webapp.JettyWebXmlConfiguration.class.getName(),
					org.eclipse.jetty.annotations.AnnotationConfiguration.class.getName());
			classlist.addAfter(org.eclipse.jetty.webapp.FragmentConfiguration.class.getName(),
					org.eclipse.jetty.plus.webapp.EnvConfiguration.class.getName(),
					org.eclipse.jetty.plus.webapp.PlusConfiguration.class.getName());

			final Handler handlers = prepareHandlers();
			server.setHandler(handlers);

			server.start();

			server.join();

		} catch (ParseException exception) {
			System.err.print("Error: ");
			System.err.println(exception.getMessage());

			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar <jar>", USAGE_HEADER, options, USAGE_FOOTER, true);
		}
	}

	protected static Handler prepareHandlers() {
		final List<Supplier<Handler>> contextHandlerSuppliers = Arrays.asList(Runner::prepareAlfrescoContextHandler,
				Runner::prepareRootContextHandler);

		final List<Handler> handlers = new ArrayList<>();
		contextHandlerSuppliers.forEach(handlerSupplier -> handlers.add(handlerSupplier.get()));
		final HandlerList handlerList = new HandlerList(handlers.toArray(new Handler[0]));

		final StatisticsHandler statisticsHandler = new StatisticsHandler();
		statisticsHandler.setHandler(handlerList);
		return statisticsHandler;
	}

	protected static Handler prepareRootContextHandler() {
		try {
			final WebAppContext context = new WebAppContext();
			context.setContextPath("/");

			// resolve concrete file to determine directory URL
			final URL rootFaviconResource = Runner.class.getClassLoader().getResource("webapps/ROOT/favicon.ico");

			String file = rootFaviconResource.getFile();
			file = file.substring(0, file.lastIndexOf('/') + 1);
			final URL webAppResource = new URL(rootFaviconResource.getProtocol(), rootFaviconResource.getHost(),
					rootFaviconResource.getPort(), file);
			// TODO We need to deal with class loader and separate core Jetty et al from the app itself
			context.setBaseResource(Resource.newResource(webAppResource));

			return context;
		} catch (final Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
			throw new RuntimeException(e);
		}
	}

	protected static Handler prepareAlfrescoContextHandler() {
		try {
			final WebAppContext context = new WebAppContext();
			context.setContextPath(alfresco_contextpath);

			// resolve concrete file to determine directory URL
			final URL rootFaviconResource = Runner.class.getClassLoader().getResource(alfresco_favicon);

			String file = rootFaviconResource.getFile();
			file = file.substring(0, file.lastIndexOf('/') + 1);
			final URL webAppResource = new URL(rootFaviconResource.getProtocol(), rootFaviconResource.getHost(),
					rootFaviconResource.getPort(), file);
			// TODO We need to deal with class loader and separate core Jetty et al from the app itself
			context.setBaseResource(Resource.newResource(webAppResource));
			context.setExtraClasspath(alfresco_configurationpath);

			return context;
		} catch (final Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
			throw new RuntimeException(e);
		}
	}
}
