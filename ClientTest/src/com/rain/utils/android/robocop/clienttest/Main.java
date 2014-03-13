package com.rain.utils.android.robocop.clienttest;

import com.rain.utils.android.robocop.generator.ContentProviderWriter;

/**
 * Created with IntelliJ IDEA.
 * User: dustin
 * Date: 1/15/14
 * Time: 9:43 AM
 */
public class Main {
    public static void main(String[] args) {
//        ContentProviderTableModel tableA = new ContentProviderTableModel("First");
//        tableA.addField("integerField", ContentProviderTableModel.ContentProviderTableFieldModel.FieldType.Number);
//        tableA.addField("booleanField", ContentProviderTableModel.ContentProviderTableFieldModel.FieldType.Bool);
//        tableA.addField("numericField", ContentProviderTableModel.ContentProviderTableFieldModel.FieldType.Numeric);
//        tableA.addField("textField", ContentProviderTableModel.ContentProviderTableFieldModel.FieldType.String);
//
//
//        ContentProviderTableModel tableB = new ContentProviderTableModel("Second");
//        tableB.addField("integerField", ContentProviderTableModel.ContentProviderTableFieldModel.FieldType.Number);
//        tableB.addField("booleanField", ContentProviderTableModel.ContentProviderTableFieldModel.FieldType.Bool);
//        tableB.addField("numericField", ContentProviderTableModel.ContentProviderTableFieldModel.FieldType.Numeric);
//        tableB.addField("textField", ContentProviderTableModel.ContentProviderTableFieldModel.FieldType.String);
//
//        List<ContentProviderTableModel> tables = new ArrayList<ContentProviderTableModel>();
//        tables.add(tableA);
//        tables.add(tableB);
//        ContentProviderModel model = new ContentProviderModel("com.rain.app", "Rain", 1, tables);
//
        ContentProviderWriter writer = new ContentProviderWriter();
        writer.createContentProvider("ClientTest/resources/contentProviderSchema_simple.json", "ClientTest/src-gen/");
//        writer.createContentProvider(model,"ClientTest/src-gen/");
    }
}
