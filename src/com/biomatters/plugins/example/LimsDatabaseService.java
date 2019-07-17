package com.biomatters.plugins.example;

import com.biomatters.geneious.publicapi.databaseservice.*;
import com.biomatters.geneious.publicapi.documents.URN;
import com.biomatters.geneious.publicapi.plugin.Icons;
import com.biomatters.geneious.publicapi.utilities.StandardIcons;

public class LimsDatabaseService extends DatabaseService {

    @Override
    public QueryField[] getSearchFields() {
        return new QueryField[0];
    }

    @Override
    public void retrieve(Query query, RetrieveCallback retrieveCallback, URN[] urns) throws DatabaseServiceException {

    }

    @Override
    public String getUniqueID() {
        return LimsConnectorPlugin.LIMS_NAME.replace(" ", "");
    }

    @Override
    public String getName() {
        return LimsConnectorPlugin.LIMS_NAME;
    }

    @Override
    public String getDescription() {
        return "Access sequences in " + LimsConnectorPlugin.LIMS_NAME;
    }

    @Override
    public String getHelp() {
        return "";
    }

    @Override
    public Icons getIcons() {
        return StandardIcons.database.getIcons();
    }

//    todo
//    @Override
//    public boolean isBrowsable() {
//        return true;
//    }
}
