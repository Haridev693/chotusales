package com.refresh.chotusalesv1.sql2xl;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelToSQLite {
    private static Handler handler = new Handler(Looper.getMainLooper());
    private SQLiteDatabase database;
    private boolean dropTable = false;
    private Context mContext;
    private String mDbName;

    public interface ImportListener {
        void onCompleted(String str);

        void onError(Exception exception);

        void onStart();
    }

    public ExcelToSQLite(Context context, String dbName) {
        this.mContext = context;
        this.mDbName = dbName;
        try {
            this.database = SQLiteDatabase.openOrCreateDatabase(this.mContext.getDatabasePath(this.mDbName).getAbsolutePath(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ExcelToSQLite(Context context, String dbName, boolean dropTable) {
        this.mContext = context;
        this.mDbName = dbName;
        this.dropTable = dropTable;
        try {
            this.database = SQLiteDatabase.openOrCreateDatabase(this.mContext.getDatabasePath(this.mDbName).getAbsolutePath(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void importFromAsset(final String assetFileName, final ImportListener listener) {
        if (listener != null) {
            listener.onStart();
        }
        new Thread(new Runnable() {

            class C04341 implements Runnable {
                C04341() {
                }

                public void run() {
                    listener.onCompleted(ExcelToSQLite.this.mDbName);
                }
            }

            public void run() {
                try {
                    ExcelToSQLite.this.working(ExcelToSQLite.this.mContext.getAssets().open(assetFileName));
                    if (listener != null) {
                        ExcelToSQLite.handler.post(new C04341());
                    }
                } catch (final Exception e) {
                    if (ExcelToSQLite.this.database != null && ExcelToSQLite.this.database.isOpen()) {
                        ExcelToSQLite.this.database.close();
                    }
                    if (listener != null) {
                        ExcelToSQLite.handler.post(new Runnable() {
                            public void run() {
                                listener.onError(e);
                            }
                        });
                    }
                }
            }
        }).start();
    }

    public void importFromFile(String filePath, ImportListener listener) {
        importFromFile(new File(filePath), listener);
    }

    private void importFromFile(final File file, final ImportListener listener) {
        if (listener != null) {
            listener.onStart();
        }
        new Thread(new Runnable() {

            class C04371 implements Runnable {
                C04371() {
                }

                public void run() {
                    listener.onCompleted(ExcelToSQLite.this.mDbName);
                }
            }

            public void run() {
                try {
                    ExcelToSQLite.this.working(new FileInputStream(file));
                    if (listener != null) {
                        ExcelToSQLite.handler.post(new C04371());
                    }
                } catch (final Exception e) {
                    if (ExcelToSQLite.this.database != null && ExcelToSQLite.this.database.isOpen()) {
                        ExcelToSQLite.this.database.close();
                    }
                    if (listener != null) {
                        ExcelToSQLite.handler.post(new Runnable() {
                            public void run() {
                                listener.onError(e);
                            }
                        });
                    }
                }
            }
        }).start();
    }

    private void working(InputStream stream) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook(stream);
        int sheetNumber = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetNumber; i++) {
            createTable(workbook.getSheetAt(i));
        }
        this.database.close();
    }

    private void createTable(Sheet sheet) {
        StringBuilder createTableSql = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        createTableSql.append(sheet.getSheetName());
        createTableSql.append("(");
        Iterator<Row> rit = sheet.rowIterator();
        Row rowHeader = (Row) rit.next();
        List<String> columns = new ArrayList();
        for (int i = 0; i < rowHeader.getPhysicalNumberOfCells(); i++) {
            createTableSql.append(rowHeader.getCell(i).getStringCellValue());
            if (i == rowHeader.getPhysicalNumberOfCells() - 1) {
                createTableSql.append(" TEXT");
            } else {
                createTableSql.append(" TEXT,");
            }
            columns.add(rowHeader.getCell(i).getStringCellValue());
        }
        createTableSql.append(")");
        if (this.dropTable) {
            this.database.execSQL("DROP TABLE IF EXISTS " + sheet.getSheetName());
        }
        this.database.execSQL(createTableSql.toString());
        for (String column : columns) {
            if (this.database.rawQuery("SELECT * FROM " + sheet.getSheetName(), null).getColumnIndex(column) < 0) {
                this.database.execSQL("ALTER TABLE " + sheet.getSheetName() + " ADD COLUMN " + column + " " + "TEXT" + " NULL;");
            }
        }
        while (rit.hasNext()) {
            Row row = (Row) rit.next();
            ContentValues values = new ContentValues();
            for (int n = 0; n < row.getPhysicalNumberOfCells(); n++) {
                if (row.getCell(n).getCellType() == 0) {
                    values.put((String) columns.get(n), Double.valueOf(row.getCell(n).getNumericCellValue()));
                } else {
                    values.put((String) columns.get(n), row.getCell(n).getStringCellValue());
                }
            }
            if (this.database.insertWithOnConflict(sheet.getSheetName(), null, values, 4) < 0) {
                throw new RuntimeException("Insert value failed!");
            }
        }
    }
}
