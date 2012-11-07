package de.mdsdacp.workflow;

import org.eclipse.emf.mwe.core.WorkflowContextDefaultImpl;
import org.eclipse.emf.mwe.core.issues.IssuesImpl;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;
import org.eclipse.emf.mwe.utils.DirectoryCleaner;
import org.eclipse.emf.mwe.utils.Reader;
import org.eclipse.xpand2.Generator;
import org.eclipse.xpand2.output.JavaBeautifier;
import org.eclipse.xpand2.output.Outlet;
import org.eclipse.xtend.typesystem.MetaModel;
import org.eclipse.xtend.typesystem.emf.EmfMetaModel;

import de.mdsdacp.workflow.config.ACPWorkflowConfig;

/**
 * This Class Represents a Model Workflow Engine file, but is realized programatically
 * 
 * @author Frederik Goetz
 */
public class ACPWorkflowImpl {
    private static final String TAG = "de.mdsdacp.workflow.ACPWorkflow";
    private static final boolean DEBUG = false;

    private static final String EXPAND_DATABASE = "de::mdsdacp::template::DatabaseTemplate::root FOR model";
    private static final String EXPAND_CONTENT_PROVIDER = "de::mdsdacp::template::ContentProviderTemplate::root FOR model";
    private static final String EXPAND_MODEL = "de::mdsdacp::template::ModelTemplate::root FOR model";
    private static final String EXPAND_CODE_SNIPPET = "de::mdsdacp::template::ManifestTemplate::root FOR model";

    private final ACPWorkflowConfig config;
    private final WorkflowContextDefaultImpl ctx;
    private final IssuesImpl issues;

    /**
     * Constructor
     * @param config - Workflow Configuration
     */
    public ACPWorkflowImpl(final ACPWorkflowConfig config) {
        this.config = config;
        this.ctx = new WorkflowContextDefaultImpl();
        this.issues = new IssuesImpl();
    }

    /**
     * Create the Readers and Generators and execute them
     *  
     * @param monitor - ProgressMonitor, can be null
     */
    public void run(final ProgressMonitor monitor) {
        if (DEBUG) {
            System.out.println("DEBUG: " + TAG);
        }

        MetaModel emfMetaModel = new EmfMetaModel(org.eclipse.emf.ecore.EcorePackage.eINSTANCE);

        Outlet javaOutlet = new Outlet();
        javaOutlet.addPostprocessor(new JavaBeautifier());
        javaOutlet.setPath(config.getGenPath());

        Outlet propertiesOutlet = new Outlet();
        propertiesOutlet.setPath(config.getGenPathCodeSnippet());

        /*
         * Create Reader
         */
        Reader reader = new Reader();
        // String "file:" is required for external files in Eclipse Plugins
        reader.setUri("file:/" + config.getEcorePath());
        reader.setModelSlot("model");

        /*
         * Directory Cleaner
         */
        DirectoryCleaner cleaner = new DirectoryCleaner();
        cleaner.setDirectory(config.getGenPath());
        cleaner.addExclude("Database.java");
        cleaner.addExclude("gen.properties");

        /*
         * Create Database Generator
         */
        Generator databaseGen = new Generator();
        databaseGen.setExpand(EXPAND_DATABASE);
        databaseGen.setFileEncoding(config.getFileEncoding());
        databaseGen.addMetaModel(emfMetaModel);
        databaseGen.addOutlet(javaOutlet);
        databaseGen.setPrDefaultExcludes(true);
        databaseGen.setPrSrcPaths(config.getGenPath());

        /*
         * Create Content Provider Generator
         */
        Generator contentProviderGen = new Generator();
        contentProviderGen.setExpand(EXPAND_CONTENT_PROVIDER);
        contentProviderGen.setFileEncoding(config.getFileEncoding());
        contentProviderGen.addMetaModel(emfMetaModel);
        contentProviderGen.addOutlet(javaOutlet);

        /*
         * Create Model Generator
         */
        Generator modelGen = new Generator();
        modelGen.setExpand(EXPAND_MODEL);
        modelGen.setFileEncoding(config.getFileEncoding());
        modelGen.addMetaModel(emfMetaModel);
        modelGen.addOutlet(javaOutlet);

        /*
         * Create Code Snippet Generator
         */
        Generator snippetGen = new Generator();
        snippetGen.setExpand(EXPAND_CODE_SNIPPET);
        snippetGen.setFileEncoding(config.getFileEncoding());
        snippetGen.addMetaModel(emfMetaModel);
        snippetGen.addOutlet(propertiesOutlet);

        /*
         * Execute Reader and Generators
         */
        reader.invoke(ctx, monitor, issues);
        cleaner.invoke(ctx, monitor, issues);
        databaseGen.invoke(ctx, monitor, issues);
        contentProviderGen.invoke(ctx, monitor, issues);
        modelGen.invoke(ctx, monitor, issues);
        snippetGen.invoke(ctx, monitor, issues);
    }
}
