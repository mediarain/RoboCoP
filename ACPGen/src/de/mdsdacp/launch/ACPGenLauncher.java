package de.mdsdacp.launch;

import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;

import de.mdsdacp.workflow.ACPWorkflowImpl;
import de.mdsdacp.workflow.config.ACPWorkflowConfig;

/**
 * ACPGenLauncher
 * To generate the content provider call a run method of this class. 
 * This will execute the workflow, which is necessary.
 * 
 * @author Frederik Goetz
 */
//TODO Maybe offer this as service?
public class ACPGenLauncher {
    private static final String TAG = "de.mdsdacp.launch.ACPGenLauncher";
    private static final boolean DEBUG = false;

    /**
     * Call this method to generate the content provider.
     * File encoding: UTF-8
     * Snippet path: Same like genPath
     * 
     * @param monitor - ProgressMonitor instance, can be null
     * @param ecorePath - Path to the ecore file, which represents the model
     * @param genPath - Generation path, in which the content provider should be generate
     */
    public static void run(final ProgressMonitor monitor, final String ecorePath, final String genPath) {
        if (DEBUG) {
            System.out.println("DEBUG: " + TAG);
        }

        run(monitor, ecorePath, genPath, ACPWorkflowConfig.DEFAULT_ENCODING);
    }

    /**
     * Call this method to generate the content provider.
     * Snippet path: Same like genPath
     * 
     * @param monitor - ProgressMonitor instance, can be null
     * @param ecorePath - Path to the ecore file, which represents the model
     * @param genPath - Generation path, in which the content provider should be generate
     * @param fileEncoding - File encoding like "UTF-8"
     */
    public static void run(final ProgressMonitor monitor, final String ecorePath, final String genPath,
            final String fileEncoding) {
        if (DEBUG) {
            System.out.println("DEBUG: " + TAG);
        }

        run(monitor, ecorePath, genPath, genPath, fileEncoding);
    }

    /**
     * Call this method to generate the content provider.
     * 
     * @param monitor - ProgressMonitor instance, can be null
     * @param ecorePath - Path to the ecore file, which represents the model
     * @param genPath - Generation path, in which the content provider should be generate
     * @param snippetPath - Path where the XML Snippet is generated. Usually in the same folder like the genPath
     * @param fileEncoding - File encoding like "UTF-8"
     */
    public static void run(final ProgressMonitor monitor, final String ecorePath, final String genPath,
            final String snippetPath, final String fileEncoding) {
        if (DEBUG) {
            System.out.println("DEBUG: " + TAG);
        }

        ACPWorkflowConfig config = ACPWorkflowConfig.getInstance();

        config.setFileEncoding(fileEncoding);
        config.setGenPath(genPath);
        config.setGenPathCodeSnippet(snippetPath);

        /*
         * Windows requires another path format as unix/linux and mac os
         */
        String osName = System.getProperty("os.name");
        if (osName.contains("Windows")) {
            config.setEcorePath(ecorePath.replaceAll("\\\\", "/"));
        } else {
            config.setEcorePath(ecorePath);
        }

        new ACPWorkflowImpl(config).run(monitor);
    }
}
