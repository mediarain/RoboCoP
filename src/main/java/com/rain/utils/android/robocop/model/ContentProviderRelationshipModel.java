package com.rain.utils.android.robocop.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created with IntelliJ IDEA.
 * User: dustin
 * Date: 2/4/14
 * Time: 4:52 PM
 */
public class ContentProviderRelationshipModel {

    public static final String RELATIONSHIP_TYPE_TO_ONE = "to_one";
    public static final String RELATIONSHIP_TYPE_TO_MANY = "to_many";
    public static final String RELATIONSHIP_TYPE_MANY_TO_MANY = "many_to_many";

    @SerializedName("type")
    private String mReferenceType;

    @SerializedName("name")
    private String mCustomName;

    @SerializedName("left_table")
    private String mLeftTableName;

    private ContentProviderTableModel mLeftTableModel;

    @SerializedName("right_table")
    private String mRightTableName;

    private ContentProviderTableModel mRightTableModel;

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ContentProviderRelationshipModel)) {
            System.out.println("not a ContentProviderRelationshipModel");
            return false;
        }
        ContentProviderRelationshipModel other = (ContentProviderRelationshipModel) obj;

        if(other.getLeftTableName().equals(getLeftTableName()) &&
                other.getRightTableName().equals(getRightTableName())) {
            return true;
        }
        return false;
    }

    public ContentProviderRelationshipModel(String referenceType, String customName, String leftTableName, String rightTableName) {
        mReferenceType = referenceType;
        mCustomName = customName;
        mLeftTableName = leftTableName;
        mRightTableName = rightTableName;
    }

    public String getReferenceType() {
        return mReferenceType;
    }

    public ContentProviderTableModel getLeftTableModel() {
        return mLeftTableModel;
    }

    public void setLeftTableModel(ContentProviderTableModel leftTableModel) {
        mLeftTableModel = leftTableModel;
    }

    public ContentProviderTableModel getRightTableModel() {
        return mRightTableModel;
    }

    public void setRightTableModel(ContentProviderTableModel rightTableModel) {
        mRightTableModel = rightTableModel;
    }

    public String getLeftTableName() {
        return mLeftTableName;
    }

    public String getRightTableName() {
        return mRightTableName;
    }

    public String getLeftTableClassName() {
        return StringUtils.convertToTitleCase(mLeftTableName);
    }

    public String getRightTableClassName() {
        return StringUtils.convertToTitleCase(mRightTableName);
    }

    public String getLeftTableConstantName() {
        return StringUtils.getConstantString(mLeftTableName);
    }

    public String getRightTableConstantName() {
        return StringUtils.getConstantString(mRightTableName);
    }

    public String getLeftTableForeignKey() {
        return StringUtils.getConstantString(mLeftTableName) + "_ID";
    }

    public String getCustomName() {
        return mCustomName;
    }

    public void setCustomName(String customName) {
        mCustomName = customName;
    }

    public String getForeignKeyNameForTable(ContentProviderTableModel table) {
        if (table == null) return null;
        if (mReferenceType.equals(RELATIONSHIP_TYPE_TO_MANY) && mRightTableModel == table) {
            return mLeftTableModel.getTableConstantName() + "_ID";
        }
        return null;
    }

    public String getForeignKeyPrivateVariableNameForTable(ContentProviderTableModel table) {
        String constantName = getForeignKeyNameForTable(table);
        if (constantName == null) return null;
        constantName = StringUtils.convertToTitleCase(constantName);
        return "m"+constantName;
    }

    public String getForeignKeyVariableNameForTable(ContentProviderTableModel table) {
        String constantName = getForeignKeyNameForTable(table);
        if (constantName == null) return null;
        return StringUtils.convertToCamelCase(constantName);
    }

    public String getForeignKeyVariableAsTitleCase(ContentProviderTableModel table) {
        String constantName = getForeignKeyNameForTable(table);
        if (constantName == null) return null;
        return StringUtils.convertToTitleCase(constantName);
    }
}
