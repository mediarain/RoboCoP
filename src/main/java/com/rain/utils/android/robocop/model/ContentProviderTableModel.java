package com.rain.utils.android.robocop.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dustin
 * Date: 1/15/14
 * Time: 9:46 AM
 */
public class ContentProviderTableModel {

    @SerializedName("name")
    private String mTableName;

    @SerializedName("members")
    private List<ContentProviderTableFieldModel> mFields = new ArrayList<ContentProviderTableFieldModel>();

    public ContentProviderTableModel(String tableName) {
        mTableName = tableName;
    }

    public List<ContentProviderTableFieldModel> getFields() {
        return mFields;
    }

    public void addField(String fieldName, String type) {
        mFields.add(new ContentProviderTableFieldModel(type, fieldName));
    }

    public String getTableName() {
        return mTableName;
    }

    public String getTableClassName() {
        return StringUtils.convertToTitleCase(mTableName);
    }

    public String getTableConstantName() {
        return StringUtils.getConstantString(mTableName);
    }


    public static class ContentProviderTableFieldModel {

        public static final String STRING = "string";
        public static final String DOUBLE = "double";
        public static final String INT = "int";
        public static final String BOOLEAN = "boolean";
        public static final String LONG = "long";

        @SerializedName("type")
        private String mFieldType;

        @SerializedName("name")
        private String mFieldName;

        public ContentProviderTableFieldModel(String fieldType, String fieldName) {
            mFieldType = fieldType;
            mFieldName = fieldName;
        }

        public String getFieldType() {
            return mFieldType;
        }

        public String getFieldName() {
            return mFieldName;
        }

        public String getConstantString() {
            return StringUtils.getConstantString(mFieldName);
        }

        public String getTypeString() {
            if (mFieldType.equals(INT) || mFieldType.equals(BOOLEAN)) {
                return "INTEGER";
            } else if (mFieldType.equals(LONG) || mFieldType.equals(DOUBLE)) {
                return "NUMERIC";
            } else {
                return "TEXT";
            }
        }

        public String getJavaTypeString() {
            String typeLower = mFieldType.toLowerCase();
            if (typeLower.equals(BOOLEAN)) {
                return "boolean";
            }
            if (typeLower.equals(INT)) {
                return "int";
            } else if (typeLower.equals(LONG) || typeLower.equals(DOUBLE)) {
                return "double";
            } else {
                return "String";
            }
        }

        public String getJavaTypeStringGetter() {
            if (mFieldType.equals(INT) || mFieldType.equals(BOOLEAN)) {
                return "getInt";
            } else if (mFieldType.equals(LONG)) {
                return "getLong";
            } else if(mFieldType.equals(DOUBLE)) {
                return "getDouble";
            } else {
                return "getString";
            }
        }

        public String getPrivateVariableName() {
            return StringUtils.getPrivateVariableName(mFieldName);
        }

        public String getNameAsTitleCase() {
            return StringUtils.convertToTitleCase(mFieldName);
        }


    }

    @Override
    public boolean equals(Object o) {
        return ((ContentProviderTableModel)o).getTableName().equals(getTableName());
    }
}
