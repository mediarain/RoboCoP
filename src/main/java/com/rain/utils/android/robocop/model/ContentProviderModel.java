package com.rain.utils.android.robocop.model;

import com.google.gson.annotations.SerializedName;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

public class ContentProviderModel {

    @SerializedName("packageName")
    private String mPackage;

    @SerializedName("providerName")
    private String mProviderName;

    @SerializedName("databaseVersion")
    private int mDatabaseVersion;

    @SerializedName("tables")
    private List<ContentProviderTableModel> mTables;

    @SerializedName("relationships")
    private List<ContentProviderRelationshipModel> mRelationships;

    public ContentProviderModel(String packageName, String providerName, int databaseVersion, List<ContentProviderTableModel> tables, List<ContentProviderRelationshipModel> relationships) {
        mPackage = packageName;
        mProviderName = providerName;
        mDatabaseVersion = databaseVersion;
        mTables = tables;
        mRelationships = relationships;
    }

    public String getProviderName() {

        return StringUtils.convertToTitleCase(mProviderName);
    }

    public List<ContentProviderTableModel> getTables() {
        return mTables;
    }

    public String getPackage() {
        return mPackage;
    }

    public int getDatabaseVersion() {
        return mDatabaseVersion;
    }

    public List<ContentProviderRelationshipModel> getRelationships() {
        return mRelationships;
    }

    public List<ContentProviderRelationshipModel> getRelationshipsForTable(ContentProviderTableModel tableModel) {
        System.out.println();
        System.out.println("Check relationship for " + tableModel.getTableName() + "!");
        if (tableModel == null || mRelationships == null) return null;
        List<ContentProviderRelationshipModel> includedRelationships = new ArrayList<ContentProviderRelationshipModel>();
        for (ContentProviderRelationshipModel relationship : mRelationships) {
            if (relationship.getLeftTableModel() != tableModel && relationship.getRightTableModel() == tableModel) {
                System.out.println("Adding " + relationship.getLeftTableName() + " -> " + relationship.getRightTableName() + "!");
                includedRelationships.add(relationship);
            }
        }
        System.out.println("included Relationships size = " + includedRelationships.size());
        return includedRelationships;
    }


    public List<ContentProviderRelationshipModel> getExternalRelationshipsForTable(ContentProviderTableModel tableModel) {
        System.out.println();
        System.out.println("Check external relationship for " + tableModel.getTableName() + "!");
        if (tableModel == null || mRelationships == null) return null;
        List<ContentProviderRelationshipModel> includedRelationships = new ArrayList<ContentProviderRelationshipModel>();
        for (ContentProviderRelationshipModel relationship : mRelationships) {
            if (relationship.getLeftTableModel() == tableModel && relationship.getRightTableModel() != tableModel) {
                System.out.println("Adding " + relationship.getLeftTableName() + " -> " + relationship.getRightTableName() + "!");
                includedRelationships.add(relationship);
            }
        }
        System.out.println("included external Relationships size = " + includedRelationships.size());
        return includedRelationships;
    }

    public void inflateRelationships() {
        if (mRelationships != null) {
            for (ContentProviderRelationshipModel relationship : mRelationships) {
                String leftTableName = relationship.getLeftTableName();
                String rightTableName = relationship.getRightTableName();
                if (leftTableName == null || leftTableName.length() == 0 || rightTableName == null || rightTableName.length() == 0) {
                    //invalid relationship config, bail
                    System.out.println("invalid relationship config!!! one of the table names is missing or is blank");
                    return;
                }
                ContentProviderTableModel leftTable = null;
                ContentProviderTableModel rightTable = null;
                for (ContentProviderTableModel table : mTables) {
                    if (leftTable != null && rightTable != null) {
                        break;
                    }
                    if (table.getTableName().equals(leftTableName)) {
                        leftTable = table;
                    }
                    if (table.getTableName().equals(rightTableName)) {
                        rightTable = table;
                    }
                }
                if (leftTable == null || rightTable == null) {
                    // the referenced tables could not be found
                    System.out.println("one or both of the referenced tables in a relationship could not be found in the table definition. please check your spelling");
                    return;
                }
                relationship.setLeftTableModel(leftTable);
                relationship.setRightTableModel(rightTable);
            }
        }
    }
}
