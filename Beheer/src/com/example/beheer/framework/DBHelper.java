package com.example.beheer.framework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	public static final String TAG = "DB";
	public static int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "GrooverMembers.db";

	private static DBHelper singleton;

	public static DBHelper getDBHelper(Context context) {

		if (singleton == null) {
			singleton = new DBHelper(context);

		}
		return singleton;

	}

	private DBHelper(Context context) {
		super(context, DATABASE_NAME, null,
				DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	public Cursor getMembers(){
		
		SQLiteDatabase db;					
		db= getReadableDatabase();
		
		return  db.query(MemberTable.TABLE_NAME, 
				new String[]{MemberTable.COLUMN_ID,MemberTable.COLUMN_FIRST_NAME, MemberTable.COLUMN_LAST_NAME, MemberTable.COLUMN_BALANCE}, 
				null, 
				null, 
				null, 
				null,
				MemberTable.COLUMN_FIRST_NAME + " COLLATE NOCASE");
	}
	
	@SuppressWarnings("finally")
	public boolean insertOrIgnore(String table, ContentValues values){
		
		boolean res = false;
		Log.d(TAG, "insertOrIgnore on " + values);
		SQLiteDatabase db = getWritableDatabase();
		try {
			db.insertOrThrow(table, null, values);
			res = true;

		} catch (SQLException e) {
			res = false;
		} finally {
			db.close();
			return res;
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		// TODO Auto-generated method stub
		db.execSQL(MemberTable.SQL_CREATE_TABLE);
		db.execSQL(GroupTable.SQL_CREATE_TABLE);
		db.execSQL(GroupMembers.SQL_CREATE_TABLE);
		db.execSQL(ItemList.SQL_CREATE_TABLE);
		db.execSQL(AccountList.SQL_CREATE_TABLE);
		db.execSQL(Order.SQL_CREATE_TABLE);
		db.execSQL(Consumption.SQL_CREATE_TABLE);
		db.execSQL(GroupClearances.SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(MemberTable.SQL_DELETE_ENTRIES);
		db.execSQL(GroupTable.SQL_DELETE_ENTRIES);
		db.execSQL(GroupMembers.SQL_DELETE_ENTRIES);
		db.execSQL(ItemList.SQL_DELETE_ENTRIES);
		db.execSQL(AccountList.SQL_DELETE_ENTRIES);
		db.execSQL(Order.SQL_DELETE_ENTRIES);
		db.execSQL(Consumption.SQL_DELETE_ENTRIES);
		db.execSQL(GroupClearances.SQL_DELETE_ENTRIES);

		onCreate(db);
		DATABASE_VERSION = newVersion;
	}

	public static abstract class MemberTable implements BaseColumns {

		public static final String TABLE_NAME = "members";
		public static final String COLUMN_ID = _ID;
		public static final String COLUMN_FIRST_NAME = "first_name";
		public static final String COLUMN_LAST_NAME = "last_name";
		public static final String COLUMN_ACCOUNT = "account";
		public static final String COLUMN_BALANCE = "balance";

		public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME
				+ " ("
				+ COLUMN_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ COLUMN_FIRST_NAME
				+ " TEXT NOT NULL"
				+ ","
				+ COLUMN_LAST_NAME
				+ " TEXT NOT NULL"
				+ ","
				+ COLUMN_ACCOUNT
				+ " INTEGER "
				+ ","
				+ COLUMN_BALANCE
				+ " DOUBLE" 
				+ ","
				+ "FOREIGN KEY("
				+ COLUMN_ACCOUNT
				+ ") REFERENCES "
				+ AccountList.TABLE_NAME
				+ "("
				+ AccountList.COLUMN_ACCOUNT
				+")"
				+ " )";

		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
				+ TABLE_NAME;
	}

	public static abstract class GroupTable implements BaseColumns {

		public static final String TABLE_NAME = "groups";
		public static final String COLUMN_GROUP_NAME = "group_name";
		public static final String COLUMN_GROUP_ACCOUNT = "group_account";
		public static final String COLUMN_GROUP_BALANCE = "group_balance";

		public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME
				+ " ("
				+ _ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ COLUMN_GROUP_NAME
				+ " TEXT NOT NULL ,"
				+ COLUMN_GROUP_ACCOUNT
				+ " INTEGER ,"
				+ COLUMN_GROUP_BALANCE 
				+ " DOUBLE "
				+ ","
				+ "FOREIGN KEY("
				+ COLUMN_GROUP_ACCOUNT
				+ ") REFERENCES "
				+ AccountList.TABLE_NAME
				+ "("
				+ AccountList.COLUMN_ACCOUNT
				+")"
				+")";

		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
				+ TABLE_NAME;
	}

	public static abstract class GroupMembers implements BaseColumns {

		public static final String TABLE_NAME = "group_members";
		public static final String COLUMN_NAME_GROUP_ID = "group_id";
		public static final String COLUMN_NAME_MEMBER_ID = "member_id";

		public static final String SQL_CREATE_TABLE = "CREATE TABLE  IF NOT EXISTS "
				+ TABLE_NAME
				+ " ("
				+ COLUMN_NAME_GROUP_ID
				+ " INT"
				+ ","
				+ COLUMN_NAME_MEMBER_ID
				+ " INT"
				+ ","
				+ "PRIMARY KEY("
				+ COLUMN_NAME_GROUP_ID
				+ ","
				+ COLUMN_NAME_MEMBER_ID
				+ "), "
				+ "FOREIGN KEY("
				+ COLUMN_NAME_GROUP_ID
				+ ") REFERENCES "
				+ GroupTable.TABLE_NAME
				+ "("
				+ _ID
				+ "), FOREIGN KEY("
				+ COLUMN_NAME_MEMBER_ID
				+ ") REFERENCES "
				+ MemberTable.TABLE_NAME + "(" + _ID + "))";

		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
				+ TABLE_NAME;
	}

	public static abstract class ItemList implements BaseColumns {

		public static final String TABLE_NAME = "item_list";
		public static final String COLUMN_ID = _ID;
		public static final String COLUMN_NAME_ITEM = "item_name";
		public static final String COLUMN_NAME_PRICE = "item_price";
		public static final String COLUMN_NAME_CAT = "item_category";

		public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_NAME
				+ " ("
				+ _ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COLUMN_NAME_ITEM
				+ " TEXT NOT NULL , "
				+ COLUMN_NAME_PRICE
				+ " DOUBLE ,"
				+ COLUMN_NAME_CAT + " TEXT DEFAULT 'overig' )";
		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
				+ TABLE_NAME;

	}

	public static abstract class AccountList implements BaseColumns {

		public static final String TABLE_NAME = "account_list";
		public static final String COLUMN_ACCOUNT = _ID;
		public static final String COLUMN_TYPE = "type";
		public static final String COLUMN_BALANCE = "balance";
		public static final String SQL_CREATE_TABLE = "CREATE TABLE  IF NOT EXISTS "
				+ TABLE_NAME
				+ " ("
				+ COLUMN_ACCOUNT
				+ " INTEGER PRIMARY KEY AUTOINCREMENT"
				+ ","
				+ COLUMN_TYPE
				+ " TEXT NOT NULL" 
				+ ","
				+ COLUMN_BALANCE
				+ " DOUBLE"
				+")";

		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
				+ TABLE_NAME;
	}

	public static abstract class Order implements BaseColumns {

		public static final String TABLE_NAME = "orders";
		public static final String COLUMN_ID = _ID;
		public static final String COLUMN_TOTAL = "total_amount";
		public static final String COLUMN_ACCOUNT = "client_account";
		public static final String COLUMN_TS_CREATED = "ts_created";
		public static final String COLUMN_TS_SETTLED = "ts_settled";

		public static final String SQL_CREATE_TABLE = "CREATE TABLE  IF NOT EXISTS "
				+ TABLE_NAME
				+ " ("
				+ COLUMN_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT"
				+ ","
				+ COLUMN_TOTAL
				+ " DOUBLE"
				+ ","
				+ COLUMN_ACCOUNT
				+ " INTEGER NOT NULL"
				+ ","
				+ COLUMN_TS_CREATED
				+ " TIMESTAMP"
				+ ","
				+ COLUMN_TS_SETTLED
				+ " TIMESTAMP" + ")";

		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
				+ TABLE_NAME;
	}

	public static abstract class Consumption implements BaseColumns {

		public static final String TABLE_NAME = "consumptions";
		public static final String COLUMN_ID = _ID;
		public static final String COLUMN_ARTICLE = "article";
		public static final String COLUMN_AMMOUNT = "ammount";

		public static final String SQL_CREATE_TABLE = "CREATE TABLE  IF NOT EXISTS "
				+ TABLE_NAME
				+ " ("
				+ COLUMN_ID
				+ " INTEGER PRIMARY KEY"
				+ ","
				+ COLUMN_ARTICLE
				+ " INTEGER"
				+ ","
				+ COLUMN_AMMOUNT
				+ " INTEGER" 
				+ ","
				+ "FOREIGN KEY("
				+ COLUMN_ID
				+ ") REFERENCES "
				+ Order.TABLE_NAME
				+ "("
				+ Order.COLUMN_ID
				+")"
				+")";

		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
				+ TABLE_NAME;
	}

	public static abstract class GroupClearances implements BaseColumns {

		public static final String TABLE_NAME = "clearance";
		public static final String COLUMN_ID = _ID;
		public static final String COLUMN_GROUP = "group_id";
		public static final String COLUMN_GROUP_NAME = "group_name";

		public static final String SQL_CREATE_TABLE = "CREATE TABLE  IF NOT EXISTS "
				+ TABLE_NAME
				+ " ("
				+ COLUMN_ID
				+ " INTEGER PRIMARY KEY"
				+ ","
				+ COLUMN_GROUP
				+ " INTEGER"
				+ ","
				+ COLUMN_GROUP_NAME
				+ " TEXT"
				+ ","
				+ "FOREIGN KEY("
				+ COLUMN_ID
				+ ") REFERENCES "
				+ Order.TABLE_NAME
				+ "("
				+ Order.COLUMN_ID
				+")"
				+ ")";

		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
				+ TABLE_NAME;
	}
}