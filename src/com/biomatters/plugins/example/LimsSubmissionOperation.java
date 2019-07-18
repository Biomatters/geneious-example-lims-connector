package com.biomatters.plugins.example;

import com.biomatters.geneious.publicapi.components.Dialogs;
import com.biomatters.geneious.publicapi.documents.AnnotatedPluginDocument;
import com.biomatters.geneious.publicapi.documents.sequence.NucleotideSequenceDocument;
import com.biomatters.geneious.publicapi.plugin.*;
import com.biomatters.geneious.publicapi.utilities.StandardIcons;
import jebl.util.ProgressListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.biomatters.plugins.example.LimsConnectorPlugin.LIMS_NAME;

public class LimsSubmissionOperation extends DocumentOperation {

    private final LimsAdapter limsAdapter;

    LimsSubmissionOperation(LimsAdapter limsAdapter) {
        this.limsAdapter = limsAdapter;
    }

    @Override
    public GeneiousActionOptions getActionOptions() {
        return new GeneiousActionOptions("Submit to " + LIMS_NAME, null, StandardIcons.database.getIcons()).setInMainToolbar(true);
    }

    @Override
    public String getHelp() {
        return "Select 1 or more nucleotide sequences to submit them to " + LIMS_NAME;
    }

    @Override
    public DocumentSelectionSignature[] getSelectionSignatures() {
        return new DocumentSelectionSignature[]{
                new DocumentSelectionSignature(NucleotideSequenceDocument.class, 1, Integer.MAX_VALUE)
        };
    }

    @Override
    public List<AnnotatedPluginDocument> performOperation(AnnotatedPluginDocument[] annotatedDocuments, ProgressListener progressListener, Options options, SequenceSelection sequenceSelection) throws DocumentOperationException {
        /*
        First check if which sequences are already in the lims, either they have a LIMS ID on them because they've
        previously been submitted or there's a sequence in the LIMS with exactly the same residues.
        */
        List<AnnotatedPluginDocument> sequencesAlreadyInLims = new ArrayList<>();
        List<AnnotatedPluginDocument> sequencesWithIdsAlready = new ArrayList<>();
        for (AnnotatedPluginDocument annotatedDocument : annotatedDocuments) {
            NucleotideSequenceDocument sequence = (NucleotideSequenceDocument) annotatedDocument.getDocument();
            Optional<String> idOnDocument = LimsAdapter.getIdFromDocumentNotes(annotatedDocument);
            if (idOnDocument.isPresent()) {
                sequencesWithIdsAlready.add(annotatedDocument);
            } else {
                List<String> searchResults = limsAdapter.searchForSequences(sequence.getCharSequence());
                if (!searchResults.isEmpty()) {
                    sequencesAlreadyInLims.add(annotatedDocument);
                }
            }
        }

        //warn about duplicate sequences
        if (!sequencesAlreadyInLims.isEmpty()) {
            if (!Dialogs.showContinueCancelDialog("<html><b>Sequences have been found in " + LIMS_NAME + " that have residues matching these sequences.</b><br><br>" +
                            "New entries will be submitted which may result in duplication of data.</html>",
                    "Matching Sequences Found in " + LIMS_NAME, null, Dialogs.DialogIcon.INFORMATION)) {
                throw new DocumentOperationException.Canceled();
            }
        }

        if (!sequencesWithIdsAlready.isEmpty()) {
            if (!Dialogs.showContinueCancelDialog("<html><b>Some selected sequences already have an ID from " + LIMS_NAME + ".</b><br><br>" +
                            "Submitting will overwrite existing entries in " + LIMS_NAME + ".</html>",
                    "Sequences Already Submitted", null, Dialogs.DialogIcon.WARNING)) {
                throw new DocumentOperationException.Canceled();
            }
        }

        //do submission
        limsAdapter.submitSequences(annotatedDocuments);

        Dialogs.DialogOptions dialogOptions = new Dialogs.DialogOptions(Dialogs.OK_ONLY, "Submission Complete", null, Dialogs.DialogIcon.INFORMATION);
        Dialogs.showDialogWithDontShowAgain(dialogOptions, "Sequences submitted successfully", LimsConnectorPlugin.getLimsCode() + "SubmissionComplete", "Don't Show Again");

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
