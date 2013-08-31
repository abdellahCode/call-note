package droid.father.should_work;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneService extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String caller;
		Database db = new Database(context);
		if(intent.getStringExtra("state").equals("RINGING")){
			Log.i("callnote", "ringing");
			caller = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
			Cursor c = db.contact_by_number(caller);
			if(c.moveToFirst()){
				Log.i("callnote", "more to first");
				Log.i("callnote", "0 " + c.getString(0));
				Log.i("callnote", "1 " + c.getString(1));
				Log.i("callnote", "2 " + c.getString(2));
				Log.i("callnote", "3 " + c.getString(3));
				Log.i("callnote", "4 " + c.getString(4));
				Log.i("callnote", "5 " + c.getString(5));
				if(c.getString(4).equals("1")){
					Intent i = new Intent(context, Note.class);
					i.putExtra("subject", c.getString(1));
					i.putExtra("note", c.getString(2));
					i.putExtra("ID", c.getString(0));
					i.putExtra("out", "0");
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					
					try {
						Log.i("callnote", "sleeping");
						Thread.sleep(2000L);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.i("callnote", "started");
					context.startActivity(i);
				}
			}

		}
		else if(intent.getStringExtra("state").equals("OFFHOOK")){

		}
		else {
			Log.i("callnote", "This is the state: " + intent.getStringExtra("state").toString());
		}
		
		db.close();

	}

}