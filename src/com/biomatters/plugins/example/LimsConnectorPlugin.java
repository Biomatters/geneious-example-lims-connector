package com.biomatters.plugins.example;

import com.biomatters.geneious.publicapi.plugin.DocumentOperation;
import com.biomatters.geneious.publicapi.plugin.GeneiousPlugin;
import com.biomatters.geneious.publicapi.plugin.GeneiousService;

import java.io.File;

@SuppressWarnings("unused")
public class LimsConnectorPlugin extends GeneiousPlugin {

    static final String LIMS_NAME = "Example LIMS";
    private static final String PLUGIN_VERSION = "0.0.1";

    private InMemoryLimsAdapter limsAdapter;

    /**
     * @return a string for use in programmatic identifiers
     */
    static String getLimsCode() {
        return LIMS_NAME.replace(" ", "");
    }

    @Override
    public String getName() {
        return LIMS_NAME + " Connector";
    }

    @Override
    public String getDescription() {
        return "Allows submission and retrieval of sequences to/from " + LIMS_NAME;
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getAuthors() {
        return "Biomatters Ltd.";
    }

    @Override
    public String getVersion() {
        return PLUGIN_VERSION;
    }

    @Override
    public String getMinimumApiVersion() {
        return "4.201900";
    }

    @Override
    public int getMaximumApiVersion() {
        return 4;
    }

    @Override
    public String getEmailAddressForCrashes() {
        return "your.name@yourcompany.com";
    }

    @Override
    public void initialize(File pluginUserDirectory, File pluginDirectory) {
        limsAdapter = new InMemoryLimsAdapter();
    }

    @Override
    public GeneiousService[] getServices() {
        return new GeneiousService[]{
                new LimsDatabaseService(limsAdapter)
        };
    }

    @Override
    public DocumentOperation[] getDocumentOperations() {
        return new DocumentOperation[]{
                new LimsSubmissionOperation(limsAdapter)
        };
    }
}