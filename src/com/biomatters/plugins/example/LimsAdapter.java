package com.biomatters.plugins.example;

import com.biomatters.geneious.publicapi.databaseservice.DatabaseService;
import com.biomatters.geneious.publicapi.databaseservice.Query;
import com.biomatters.geneious.publicapi.databaseservice.QueryField;
import com.biomatters.geneious.publicapi.databaseservice.RetrieveCallback;
import com.biomatters.geneious.publicapi.documents.*;
import com.biomatters.geneious.publicapi.documents.sequence.SequenceCharSequence;
import com.biomatters.geneious.publicapi.plugin.DocumentOperationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface LimsAdapter {

    /**
     * @param sequence sequence residues to search in LIMS for
     * @return list of all IDs of matching sequence in LIMS if there are any (never null)
     */
    List<String> searchForSequences(SequenceCharSequence sequence) throws DocumentOperationException;

    /**
     * Submit sequences to the LIMS. New entries are created for any documents that don't have an ID stored in
     * DocumentNotes. LIMS will overwrite existing entries with the new sequence if an ID is present in DocumentNotes.
     * This method will add/update DocumentNotes on the sequence and save it with the LIMS ID.
     *
     * @param documents NucleotideSequenceDocuments to submit
     * @return id of the
     */
    void submitSequences(AnnotatedPluginDocument... documents);

    /**
     * Delegation of {{@link com.biomatters.geneious.publicapi.databaseservice.DatabaseService#retrieve(Query, RetrieveCallback, URN[])}}
     */
    void retrieve(Query query, RetrieveCallback retrieveCallback);

    /**
     * Delegation of {{@link DatabaseService#getSearchFields()}}
     */
    QueryField[] getSearchFields();

    /**
     * @param annotatedDocument document to get LIMS ID from
     * @return ID for this LIMS that has been stored on this document
     */
    static Optional<String> getIdFromDocumentNotes(AnnotatedPluginDocument annotatedDocument) {
        return Optional.ofNullable((String) annotatedDocument.getFieldValue(NOTE_TYPE_CODE + "." + FIELDS.get(0).getCode()));
    }

    String NOTE_TYPE_CODE = LimsConnectorPlugin.getLimsCode();
    String ID_CODE = "id";

    List<DocumentNoteField> FIELDS = Collections.singletonList(
            DocumentNoteField.createTextNoteField("ID", null, ID_CODE, new ArrayList<>(), false)
    );

    static void addIdToDocumentNotes(AnnotatedPluginDocument annotatedDocument, String id) {
        DocumentNoteType noteType = DocumentNoteUtilities.getNoteType(NOTE_TYPE_CODE);
        if (noteType == null || noteType.getFields().size() != FIELDS.size()) {
            noteType = DocumentNoteUtilities.createNewNoteType(LimsConnectorPlugin.LIMS_NAME, NOTE_TYPE_CODE, "Meta data from " + LimsConnectorPlugin.LIMS_NAME,
                    FIELDS, true);
            DocumentNoteUtilities.setNoteType(noteType);
        }

        DocumentNote documentNote = noteType.createDocumentNote();
        documentNote.setFieldValue(ID_CODE, id);

        AnnotatedPluginDocument.DocumentNotes documentNotes = annotatedDocument.getDocumentNotes(true);
        documentNotes.setNote(documentNote);
        documentNotes.saveNotes();
    }
}
