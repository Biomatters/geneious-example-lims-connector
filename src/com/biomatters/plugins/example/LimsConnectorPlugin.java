package com.biomatters.plugins.example;

import com.biomatters.geneious.publicapi.plugin.DocumentOperation;
import com.biomatters.geneious.publicapi.plugin.GeneiousPlugin;
import com.biomatters.geneious.publicapi.plugin.GeneiousService;

@SuppressWarnings("unused")
public class LimsConnectorPlugin extends GeneiousPlugin {

    static final String LIMS_NAME = "Example LIMS";
    private static final String PLUGIN_VERSION = "0.0.1";

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
    public GeneiousService[] getServices() {
        return new GeneiousService[]{
                new LimsDatabaseService()
        };
    }

    @Override
    public DocumentOperation[] getDocumentOperations() {
        return new DocumentOperation[]{
                new LimsSubmissionOperation()
        };
    }
}