package com.biomatters.plugins.example;

import com.biomatters.geneious.publicapi.documents.AnnotatedPluginDocument;
import com.biomatters.geneious.publicapi.plugin.*;
import jebl.util.ProgressListener;

import java.util.Collections;
import java.util.List;

public class LimsSubmissionOperation extends DocumentOperation {

    @Override
    public GeneiousActionOptions getActionOptions() {
        return new GeneiousActionOptions("Submit to " + LimsConnectorPlugin.LIMS_NAME).setInMainToolbar(true);
    }

    @Override
    public String getHelp() {
        return "Select 1 or more nucleotide sequences to submit them to " + LimsConnectorPlugin.LIMS_NAME;
    }

    @Override
    public DocumentSelectionSignature[] getSelectionSignatures() {
        return new DocumentSelectionSignature[]{
                DocumentSelectionSignature.forNucleotideSequences(1, Integer.MAX_VALUE, false)
        };
    }

    @Override
    public List<AnnotatedPluginDocument> performOperation(AnnotatedPluginDocument[] annotatedDocuments, ProgressListener progressListener, Options options, SequenceSelection sequenceSelection) throws DocumentOperationException {
        return Collections.emptyList();
    }

    @Override
    public boolean isDocumentGenerator() {
        return false;
    }

    @Override
    public Options getOptions(DocumentOperationInput operationInput) throws DocumentOperationException {
        return super.getOptions(operationInput);
    }
}
