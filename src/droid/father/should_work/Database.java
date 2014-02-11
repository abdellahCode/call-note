package droid.father.should_work;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

public class Database extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "CallNote.db";
	private static final String NOTES_TABLE = "NOTES";
	private static final String NOTE_ID = "_id";
	private static final String NOTE_SUBJECT = "subject";
	private static final String NOTE_TEXT = "note";
	private static final String NOTE_CONTACT = "contact";
	private static final String NOTE_CONTACT_NAME = "contact_name";
	private static final String INCOMING = "incoming";
	private static final String OUTGOING = "outgoing";

	private static final String CREATE_NOTES_TABLE = "create table " + NOTES_TABLE + " ( " + NOTE_ID + " integer primary key " +
	"autoincrement, " + NOTE_SUBJECT + " TEXT, " + NOTE_TEXT + " TEXT, " + NOTE_CONTACT + " TEXT, " + INCOMING + " TEXT, " + OUTGOING + " TEXT, " + NOTE_CONTACT_NAME + " TEXT ); ";

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_NOTES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE);
		onCreate(db);
	}

	protected void insert_note(String subject, String note, String contact, String incoming, String outgoing, String name){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(NOTE_SUBJECT, subject);
		cv.put(NOTE_TEXT, note);
		cv.put(NOTE_CONTACT, contact);
		cv.put(INCOMING, incoming);
		cv.put(OUTGOING, outgoing);
		cv.put(NOTE_CONTACT_NAME, name);
		db.insert(NOTES_TABLE, null, cv);
		db.close();
	}

	protected Cursor contact_by_number(String number){
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "select _id, "+ NOTE_CONTACT +" from " + NOTES_TABLE;// + " where " + NOTE_CONTACT;// + " = '" + number + "'";
		String id = "-1";
		Cursor c = db.rawQuery(query, null);
		while(c.moveToNext()){
			Log.d("callnote", "those are the numbers: " + number + " -- and: " + c.getString(1));
			if(PhoneNumberUtils.compare(number, c.getString(1))){
				id = c.getString(0);
				break;
			}                	
		}
		c = null;
		if(!id.equals("-1")){
			String query_1 = "select * from " + NOTES_TABLE + " where _id  = '" + id +"'";// + " = '" + number + "'";
			c = db.rawQuery(query_1, null);
		}

		return c;

	}

	protected Cursor getNotes(){
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "select * from " + NOTES_TABLE;
		Cursor c = db.rawQuery(query, null);
		return c;

	}
	protected Cursor getNotesByID(long id){
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "select * from " + NOTES_TABLE+ " where " + NOTE_ID + " = '" + id + "'";
		Cursor c = db.rawQuery(query, null);
		return c;

	}

	protected void ModifyNote(long id, String subject, String note, String in, String out){
		SQLiteDatabase db = this.getWritableDatabase();
		String[] key = new String[1];
		key[0] = id+"";
		ContentValues cv = new ContentValues();
		cv.put(NOTE_SUBJECT, subject);
		cv.put(NOTE_TEXT, note);
		cv.put(INCOMING, in);
		cv.put(OUTGOING, out);

		String query = "update " + NOTES_TABLE+ " set subject = '"+subject+"', note = '"+note+"', incoming = '" +in+"', outgoing = '"+out+"' where " + NOTE_ID + " = ?";
		db.update(NOTES_TABLE, cv, NOTE_ID + " = " + id, null);


	}
	protected void DeleteNote(String ID){
		String q = "delete from " + NOTES_TABLE + " where " + NOTE_ID + " = " + ID;
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(q);

	}


}
