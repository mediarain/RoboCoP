package de.mdsdacp.workflow.config;

/**
 * Represents the workflow configuration and is a singleton
 * Only stores the data, which are required for the 
 * generation like model path, generation path and file encoding
 * 
 * @author Frederik Goetz
 */
public class ACPWorkflowConfig {
    public static final String DEFAULT_ENCODING = "UTF-8";

    private static ACPWorkflowConfig instance = null;

    private String ecorePath;
    private String genPath;
    private String genPathCodeSnippet;
    private String fileEncoding = DEFAULT_ENCODING;

    private ACPWorkflowConfig() {
    }

    public static void setInstance(ACPWorkflowConfig instance) {
        ACPWorkflowConfig.instance = instance;
    }

    public static ACPWorkflowConfig getInstance() {
        if (instance == null) {
            instance = new ACPWorkflowConfig();
        }
        return instance;
    }

    /**
     * @return the ecorePath
     */
    public final String getEcorePath() {
        return ecorePath;
    }

    /**
     * @param ecorePath the ecorePath to set
     */
    public final void setEcorePath(String ecorePath) {
        this.ecorePath = ecorePath;
    }

    /**
     * @return the genPath
     */
    public final String getGenPath() {
        return genPath;
    }

    /**
     * @param genPath the genPath to set
     */
    public final void setGenPath(String genPath) {
        this.genPath = genPath;
    }

    /**
     * @return the genPathCodeSnippet
     */
    public final String getGenPathCodeSnippet() {
        return genPathCodeSnippet;
    }

    /**
     * @param genPathCodeSnippet the genPathCodeSnippet to set
     */
    public final void setGenPathCodeSnippet(String genPathCodeSnippet) {
        this.genPathCodeSnippet = genPathCodeSnippet;
    }

    /**
     * @return the fileEncoding
     */
    public final String getFileEncoding() {
        return fileEncoding;
    }

    /**
     * @param fileEncoding the fileEncoding to set
     */
    public final void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }
}
