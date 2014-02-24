package droid.father.should_work;

import droid.father.should_work.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

public class OutgoingService extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try{
			String callee;
			Database db = new Database(context);		
			callee = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			Log.i("callnote", "this is the callee number: " + callee);
			Cursor c = db.contact_by_number(callee);
			if(c.moveToFirst()){
				Log.i("callnote", "move to first");
				Log.i("callnote", "0 o " + c.getString(0));
				Log.i("callnote", "1 o " + c.getString(1));
				Log.i("callnote", "2 o " + c.getString(2));
				Log.i("callnote", "3 o " + c.getString(3));
				Log.i("callnote", "4 o " + c.getString(4));
				Log.i("callnote", "5 o " + c.getString(5));
				if(c.getString(5).equals("1")){
					Intent i = new Intent(context, Note.class);
					i.putExtra("subject", c.getString(1));
					i.putExtra("note", c.getString(2));
					i.putExtra("ID", c.getString(0));
					i.putExtra("out", "1");
					i.putExtra("contact", callee);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Log.i("callnote", "started");
					context.startActivity(i);
					Log.i("callnote", "Abort");
					SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPref.edit();
					if(sharedPref.getString("out", "0").equals("0")){
						Log.i("callnote", "Abort");
						setResultData(null);
						Log.i("callnote", "started");
						context.startActivity(i);
					}
					editor.putString("out", "0");
					editor.apply();
				}
			}
		}catch(Exception e){
			Log.e("callnote", "-> "+e.getMessage());
		}

	}


}
