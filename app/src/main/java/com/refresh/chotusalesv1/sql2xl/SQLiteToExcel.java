package com.refresh.chotusalesv1.sql2xl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SQLiteToExcel {
    private static Handler handler = new Handler(Looper.getMainLooper());
    private SQLiteDatabase database;
    private Context mContext;
    private String mDbName;
    private String mExportPath;
    private HSSFWorkbook workbook;

    public interface ExportListener {
        void onCompleted(String str);

        void onError(Exception exception);

        void onStart();
    }

    public SQLiteToExcel(Context context, String dbName) {
        this(context, dbName, Environment.getExternalStorageDirectory().toString() + File.separator);
    }

    public SQLiteToExcel(Context context, String dbName, String exportPath) {
        this.mContext = context;
        this.mDbName = dbName;
        this.mExportPath = exportPath;
        try {
            this.database = SQLiteDatabase.openOrCreateDatabase(this.mContext.getDatabasePath(this.mDbName).getAbsolutePath(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> getAllTables() {
        ArrayList<String> tables = new ArrayList();
        Cursor cursor = this.database.rawQuery("select name from sqlite_master where type='table' order by name", null);
        while (cursor.moveToNext()) {
            tables.add(cursor.getString(0));
        }
        cursor.close();
        return tables;
    }

    private ArrayList<String> getColumns(String table) {
        ArrayList<String> columns = new ArrayList();
        Cursor cursor = this.database.rawQuery("PRAGMA table_info(" + table + ")", null);
        while (cursor.moveToNext()) {
            columns.add(cursor.getString(1));
        }
        cursor.close();
        return columns;
    }

    private void exportTables(List<String> tables, String fileName) throws Exception {
        this.workbook = new HSSFWorkbook();
        for (int i = 0; i < tables.size(); i++) {
            if (!((String) tables.get(i)).equals("android_metadata")) {
                createSheet((String) tables.get(i), this.workbook.createSheet((String) tables.get(i)));
            }
        }
        OutputStream fos = new FileOutputStream(new File(this.mExportPath, fileName));
        this.workbook.write(fos);
        fos.flush();
        fos.close();
        this.workbook.close();
        this.database.close();
    }

    public void exportSingleTable(String table, String fileName, ExportListener listener) {
        List<String> tables = new ArrayList();
        tables.add(table);
        startExportTables(tables, fileName, listener);
    }

    public void exportSpecificTables(ArrayList<String> tables, String fileName, ExportListener listener) {
        startExportTables(tables, fileName, listener);
    }

    public void exportAllTables(String fileName, ExportListener listener) {
        startExportTables(getAllTables(), fileName, listener);
    }

    public void startExportTables(final List<String> tables, final String fileName, final ExportListener listener) {
        if (listener != null) {
            listener.onStart();
        }
        new Thread(new Runnable() {

            class C04401 implements Runnable {
                C04401() {
                }

                public void run() {
                    listener.onCompleted(SQLiteToExcel.this.mExportPath + fileName);
                }
            }

            public void run() {
                try {
                    SQLiteToExcel.this.exportTables(tables, fileName);
                    if (listener != null) {
                        SQLiteToExcel.handler.post(new C04401());
                    }
                } catch (final Exception e) {
                    if (SQLiteToExcel.this.database != null && SQLiteToExcel.this.database.isOpen()) {
                        SQLiteToExcel.this.database.close();
                    }
                    if (listener != null) {
                        SQLiteToExcel.handler.post(new Runnable() {
                            public void run() {
                                listener.onError(e);
                            }
                        });
                    }
                }
            }
        }).start();
    }

    private void createSheet(String table, HSSFSheet sheet) {
        HSSFRow rowA = sheet.createRow(0);
        ArrayList<String> columns = getColumns(table);
        for (int i = 0; i < columns.size(); i++) {
            rowA.createCell(i).setCellValue(new HSSFRichTextString("" + ((String) columns.get(i))));
        }
        insertItemToSheet(table, sheet, columns);
    }

    private void insertItemToSheet(String table, HSSFSheet sheet, ArrayList<String> columns) {
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        Cursor cursor = this.database.rawQuery("select * from " + table, null);
        cursor.moveToFirst();
        int n = 1;
        while (!cursor.isAfterLast()) {
            HSSFRow rowA = sheet.createRow(n);
            for (int j = 0; j < columns.size(); j++) {
                HSSFCell cellA = rowA.createCell(j);
                if (cursor.getType(j) == 4) {
                    HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) j, n, (short) (j + 1), n + 1);
                    anchor.setAnchorType(3);
                    patriarch.createPicture(anchor, this.workbook.addPicture(cursor.getBlob(j), 5));
                } else {
                    cellA.setCellValue(new HSSFRichTextString(cursor.getString(j)));
                }
            }
            n++;
            cursor.moveToNext();
        }
        cursor.close();
    }
}
