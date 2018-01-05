package com.refresh.chotusalesv1.techicalservices;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Real database connector, provides all CRUD operation.
 * database tables are created here.
 * 
\developed by Sri Haridev Software Solutions
 *
 */
public class AndroidDatabase extends SQLiteOpenHelper implements Database {

	private static final int DATABASE_VERSION = 9;

	private static String AddShop =
			"ALTER TABLE " +DatabaseContents.TABLE_SETTINGS+
					" ADD COLUMN ShopName TEXT DEFAULT ''";

	private static String AddAddrline1 ="ALTER TABLE " +DatabaseContents.TABLE_SETTINGS+
			" ADD COLUMN AddressLine1 TEXT DEFAULT ''";

	private static String AddAddrline2 ="ALTER TABLE " +DatabaseContents.TABLE_SETTINGS+

			" ADD COLUMN AddressLine2 TEXT DEFAULT ''";

	private static String PrintGSTProds ="ALTER TABLE " +DatabaseContents.TABLE_SETTINGS+

			" ADD COLUMN CheckPrintGSTProds INT DEFAULT 0";

	private static String CheckPrintTran ="ALTER TABLE " +DatabaseContents.TABLE_SETTINGS+

			" ADD COLUMN CheckPrintTranGST INT DEFAULT 0";

	private static String CGSTPer ="ALTER TABLE " +DatabaseContents.TABLE_SETTINGS+

			" ADD COLUMN CGSTPercent DOUBLE DEFAULT 0";

	private static String SGSTPer ="ALTER TABLE " +DatabaseContents.TABLE_SETTINGS+

			" ADD COLUMN SGSTPercent DOUBLE DEFAULT 0";

	private static String AddDupReceipt ="ALTER TABLE " +DatabaseContents.TABLE_SETTINGS+

			" ADD COLUMN PrintDupReceipt INT DEFAULT 0";



	private static String AddDiscount ="ALTER TABLE " +DatabaseContents.TABLE_SALE+

			" ADD COLUMN discount DOUBLE DEFAULT 0";

	private static String AddTranTax ="ALTER TABLE " +DatabaseContents.TABLE_SALE+

			" ADD COLUMN trantax Boolean DEFAULT 0";

	//        s.ShopName = ShopName.getText().toString();
//        s.AddressLine1 = AddressLine1.getText().toString();
//        s.AddressLine2 = AddressLine2.getText().toString();
//        s.CheckPrintGSTProds = CheckPrintGSTProds.isChecked();
//        s.CheckPrintTranGST = CheckPrintTranGST.isChecked();
//            s.CGSTPercent = Double.valueOf(EdtCGSTPercent.getText().toString());
//            s.SGSTPercent = Double.valueOf(EdtSGSTPercent.getText().toString());

	/**
	 * Constructs a new AndroidDatabase.
	 * @param context The current stage of the application.
	 */
	public AndroidDatabase(Context context) {
		super(context, DatabaseContents.DATABASE.toString(), null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		
		database.execSQL("CREATE TABLE " + DatabaseContents.TABLE_PRODUCT_CATALOG + "("
				
				+ "_id INTEGER PRIMARY KEY,"
				+ "name TEXT(100),"
				+ "barcode TEXT(100),"
				+ "unit_price DOUBLE,"
				+ "status TEXT(10),"
				+"taxid INTEGER"  //taxChanges: adding Taxid to product_catalog

				+ ");");
		Log.d("CREATE DATABASE", "Create " + DatabaseContents.TABLE_PRODUCT_CATALOG + " Successfully.");
		
		database.execSQL("CREATE TABLE "+ DatabaseContents.TABLE_STOCK + "(" 
				
				+ "_id INTEGER PRIMARY KEY,"
				+ "product_id INTEGER,"
				+ "quantity INTEGER,"
				+ "cost DOUBLE,"
				+ "date_added DATETIME"
				
				+ ");");
		Log.d("CREATE DATABASE", "Create " + DatabaseContents.TABLE_STOCK + " Successfully.");
		
		database.execSQL("CREATE TABLE "+ DatabaseContents.TABLE_SALE + "("
				
				+ "_id INTEGER PRIMARY KEY,"
				+ "status TEXT(40),"
				+ "payment TEXT(50),"
				+ "total DOUBLE,"
				+ "subtotal DOUBLE," //taxChanges: adding subtotal to TABLE_STOCK
				+ "tax DOUBLE," //taxChanges: adding tax to TABLE_STOCK
				+ "start_time DATETIME,"
				+ "end_time DATETIME,"
				+ "orders INTEGER,"
				+ "buyerid INTEGER"
				+ ");");
		Log.d("CREATE DATABASE", "Create " + DatabaseContents.TABLE_SALE + " Successfully.");
		
		database.execSQL("CREATE TABLE "+ DatabaseContents.TABLE_SALE_LINEITEM + "("
				
				+ "_id INTEGER PRIMARY KEY,"
				+ "sale_id INTEGER,"
				+ "product_id INTEGER,"
				+ "quantity INTEGER,"
				+ "unit_price DOUBLE,"
				+"tax DOUBLE" //taxChanges: adding tax amount to TABLE_SALE_LINEITEM
				
				+ ");");
		Log.d("CREATE DATABASE", "Create " + DatabaseContents.TABLE_SALE_LINEITEM + " Successfully.");


		// this _id is product_id but for update method, it is easier to use name _id
		database.execSQL("CREATE TABLE " + DatabaseContents.TABLE_STOCK_SUM + "("
				
				+ "_id INTEGER PRIMARY KEY,"
				+ "quantity INTEGER"
				
				+ ");");
		Log.d("CREATE DATABASE", "Create " + DatabaseContents.TABLE_STOCK_SUM + " Successfully.");
		
		database.execSQL("CREATE TABLE " + DatabaseContents.LANGUAGE + "("
				
				+ "_id INTEGER PRIMARY KEY,"
				+ "language TEXT(5)"
				
				+ ");");

		Log.d("CREATE DATABASE", "Create " + DatabaseContents.LANGUAGE + " Successfully.");

		database.execSQL("CREATE TABLE " + DatabaseContents.TABLE_SETTINGS + "("

				+ "_id INTEGER PRIMARY KEY,"
				+ "userid TEXT,"
				+ "vatnumber TEXT,"
				+ "printerHeader TEXT,"
				+ "printerFooter TEXT,"
				+ "bluetoothAddress TEXT,"
				+ "SMSKey TEXT,"
				+ "SMSSenderID TEXT,"
				+ "SMSUsername TEXT,"
				+ "SMSenabled BOOLEAN DEFAULT 0"
				+ ");");


//		s.SMSKey = content.getAsString("SMSKey");
//		s.SMSSenderID = content.getAsString("SMSSenderID");
//		s.SMSUsername = content.getAsString("SMSUsername");


		// this _id is product_id but for update method, it is easier to use name _id
		database.execSQL("CREATE TABLE " + DatabaseContents.TABLE_TAX + "("

				+ "_id INTEGER PRIMARY KEY,"
				+ "taxname TEXT,"
				+ "taxpercent DOUBLE,"
				+ "IsPriceInclusive BOOLEAN DEFAULT 0"
				+ ");");


		Log.d("CREATE DATABASE", "Create " + DatabaseContents.TABLE_TAX + " Successfully.");

		database.execSQL("CREATE TABLE " + DatabaseContents.TABLE_USERS + "("

				+ "_id INTEGER PRIMARY KEY,"
				+ "username TEXT,"
				+ "userpin INTEGER,"
				+ "usertype TEXT,"
				+ " CONSTRAINT pinunique UNIQUE (userpin)"

				+ ");");


		Log.d("CREATE DATABASE", "Create " + DatabaseContents.TABLE_USERS + " Successfully.");

		database.execSQL("CREATE TABLE " + DatabaseContents.TABLE_BUYERS + "("

				+ "_id INTEGER PRIMARY KEY,"
				+ "buyername TEXT,"
				+ "buyerphone TEXT,"
				+ "buyerAddress TEXT"
				+ ");");

		Log.d("CREATE DATABASE", "Create " + DatabaseContents.TABLE_BUYERS+ " Successfully.");

		
		Log.d("CREATE DATABASE", "Create Database Successfully.");

		String AddPayType = "ALTER TABLE " +DatabaseContents.TABLE_SALE+" ADD COLUMN PayType TEXT";

		database.execSQL(AddPayType);
//
		database.execSQL(AddShop);

		database.execSQL(AddAddrline1);

		database.execSQL(AddAddrline2);

		database.execSQL(CheckPrintTran);
//
		database.execSQL(PrintGSTProds);

		database.execSQL(CGSTPer);

		database.execSQL(SGSTPer);

		database.execSQL(AddDupReceipt);

		database.execSQL(AddDiscount);

		database.execSQL(AddTranTax);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

		switch (oldVersion) {
			case 1:
				// update to version 2
				// do _not_ break; -- fall through!
			case 2:
				// update to version 3
				// again, do not break;
			case 3:

//				database.execSQL("CREATE TABLE " + DatabaseContents.TABLE_BUYERS + "("
//
//						+ "_id INTEGER PRIMARY KEY,"
//						+ "buyername TEXT,"
//						+ "buyerphone TEXT,"
//						+ "buyerAddress TEXT"
//						+ ");");
//
//				database.execSQL("ALTER TABLE " +DatabaseContents.TABLE_SALE+" ADD COLUMN buyerid INTEGER DEFAULT -1");


			case 4:

				String AddPayType = "ALTER TABLE " + DatabaseContents.TABLE_SALE + " ADD COLUMN PayType TEXT";

				database.execSQL(AddPayType);

			case 5:

				//
				database.execSQL(AddShop);

				database.execSQL(AddAddrline1);

				database.execSQL(AddAddrline2);

				database.execSQL(CheckPrintTran);
//
				database.execSQL(PrintGSTProds);

				database.execSQL(CGSTPer);

				database.execSQL(SGSTPer);


			case 6:
				database.execSQL(AddDupReceipt);

			case 7:
				database.execSQL(AddDiscount);
			case 8:
				database.execSQL(AddTranTax);
		}

//		if(oldVersion==1)
//		db.execSQL("ALTER TABLE " +Da
// tabaseContents.TABLE_TAX +" ADD COLUMN IsPriceInclusive Boolean DEFAULT 0");
//		if(oldVersion==2)
//		db.execSQL("ALTER TABLE " +DatabaseContents.TABLE_PRODUCT_CATALOG+" ADD COLUMN taxid INTEGER");
	}

	@Override
	public List<Object> select(String queryString) {
		try {
			SQLiteDatabase database = this.getWritableDatabase();
			List<Object> list = new ArrayList<Object>();
			Cursor cursor = database.rawQuery(queryString, null);

			if (cursor != null) {
				if (cursor.moveToFirst()) {
					do {
						ContentValues content = new ContentValues();
						String[] columnNames = cursor.getColumnNames();
						for (String columnName : columnNames) {
							content.put(columnName, cursor.getString(cursor
									.getColumnIndex(columnName)));
						}
						list.add(content);
					} while (cursor.moveToNext());
				}
			}
			cursor.close();
			database.close();
			return list;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int insert(String tableName, Object content) {
		try {
			SQLiteDatabase database = this.getWritableDatabase();
			int id = (int) database.insert(tableName, null,
					(ContentValues) content);
			database.close();
			return id;
		} catch (Exception e) {
			Log.e("error",e.getMessage());
			e.printStackTrace();
			return -1;
		}

	}

	@Override
	public boolean update(String tableName, Object content) {
		try {
			SQLiteDatabase database = this.getWritableDatabase();
			ContentValues cont = (ContentValues) content;
			// this array will always contains only one element. 
			String[] array = new String[]{cont.get("_id")+""};
			int i= database.update(tableName, cont, " _id = ?", array);
			Log.i("Update AndroidDatabase","Update no. of rows returned"+ String.valueOf(i));
			return true;
			
		} catch (Exception e) {
			Log.e("update error", e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

    @Override
    public boolean delete(String tableName, int id) {
            try {
                    SQLiteDatabase database = this.getWritableDatabase();
                    database.delete(tableName, " _id = ?", new String[]{id+""});
                    return true;
                    
            } catch (Exception e) {
                    e.printStackTrace();
                    return false;
            }
    }
//////////////////
	@Override
	public boolean execute(String query) {
		try{
			SQLiteDatabase database = this.getWritableDatabase();
			database.execSQL(query);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

}
