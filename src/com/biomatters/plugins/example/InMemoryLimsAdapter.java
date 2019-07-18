package com.biomatters.plugins.example;

import com.biomatters.geneious.publicapi.databaseservice.BasicSearchQuery;
import com.biomatters.geneious.publicapi.databaseservice.Query;
import com.biomatters.geneious.publicapi.databaseservice.QueryField;
import com.biomatters.geneious.publicapi.databaseservice.RetrieveCallback;
import com.biomatters.geneious.publicapi.documents.AnnotatedPluginDocument;
import com.biomatters.geneious.publicapi.documents.DocumentUtilities;
import com.biomatters.geneious.publicapi.documents.sequence.SequenceCharSequence;
import com.biomatters.geneious.publicapi.documents.sequence.SequenceDocument;
import com.biomatters.geneious.publicapi.plugin.DocumentOperationException;
import com.biomatters.geneious.publicapi.utilities.CharSequenceUtilities;

import java.util.*;

public class InMemoryLimsAdapter implements LimsAdapter {

    private static final Map<String, AnnotatedPluginDocument> SEQUENCES = Collections.synchronizedMap(new HashMap<>());

    @Override
    public List<String> searchForSequences(SequenceCharSequence sequence) throws DocumentOperationException {
        List<String> matchingIds = new ArrayList<>();
        synchronized (SEQUENCES) {
            for (Map.Entry<String, AnnotatedPluginDocument> entry : SEQUENCES.entrySet()) {
                SequenceDocument sequenceDocument = (SequenceDocument) entry.getValue().getDocument();
                if (CharSequenceUtilities.equalsIgnoreCase(sequenceDocument.getCharSequence(), sequence)) {
                    matchingIds.add(entry.getKey());
                }
            }
        }
        return matchingIds;
    }

    @Override
    public void submitSequences(AnnotatedPluginDocument... documents) {
        for (AnnotatedPluginDocument inputDocument : documents) {
            AnnotatedPluginDocument documentCopy = DocumentUtilities.duplicateDocument(inputDocument, true); //defensive copy
            Optional<String> idFromDocumentNotes = LimsAdapter.getIdFromDocumentNotes(inputDocument);
            if (idFromDocumentNotes.isPresent()) {
                SEQUENCES.put(idFromDocumentNotes.get(), documentCopy);
            } else {
                String id = UUID.randomUUID().toString();
                LimsAdapter.addIdToDocumentNotes(inputDocument, id);
                LimsAdapter.addIdToDocumentNotes(documentCopy, id);
                SEQUENCES.put(id, documentCopy);
            }
        }

        System.out.println();
        SEQUENCES.forEach((key, value) -> {
            try {
                System.out.println(key + " -> " + ((SequenceDocument) value.getDocument()).getCharSequence());
            } catch (DocumentOperationException e) {
                //ignore
            }
        });
    }

    @Override
    public QueryField[] getSearchFields() {
        return null; // implement this to enable advanced search.
    }

    @Override
    public void retrieve(Query query, RetrieveCallback retrieveCallback) {
        String queryString = ((BasicSearchQuery) query).getSearchText();
        for (Map.Entry<String, AnnotatedPluginDocument> entry : SEQUENCES.entrySet()) {
            if (entry.getKey().toUpperCase().contains(queryString.toUpperCase()) || entry.getValue().getName().toUpperCase().contains(queryString.toUpperCase())) {
                retrieveCallback.add(DocumentUtilities.duplicateDocument(entry.getValue(), true), null);
            }
        }
    }
}
