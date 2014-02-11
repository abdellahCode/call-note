package droid.father.should_work;

import droid.father.should_work.R;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OneNote extends Activity{


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.one_note);
		try{
			Bundle b = getIntent().getExtras();
			final long id = b.getLong("id");
			final Database db = new Database(getApplicationContext());
			final Cursor c = db.getNotesByID(id);
			Log.i("callnote", "the id: "+id);
			c.moveToFirst();
			TextView subject =  (TextView) findViewById(R.id.subject);
			subject.setText(c.getString(c.getColumnIndex("subject")));
			TextView contact =  (TextView) findViewById(R.id.contact);
			if(c.getString(c.getColumnIndex("contact_name")) != null)
			{
				contact.setText(c.getString(c.getColumnIndex("contact_name")));
			}
			else{
				contact.setText(c.getString(c.getColumnIndex("contact")));
			}
			TextView note =  (TextView) findViewById(R.id.note);
			note.setText(c.getString(c.getColumnIndex("note")));
			TextView in =  (TextView) findViewById(R.id.in);
			TextView out =  (TextView) findViewById(R.id.out);
			if(c.getString(c.getColumnIndex("incoming")).equals("0"))
				in.setVisibility(View.GONE);
			else
				in.setVisibility(View.VISIBLE);
			if(c.getString(c.getColumnIndex("outgoing")).equals("0"))
				out.setVisibility(View.GONE);
			else 
				out.setVisibility(View.VISIBLE);

			final Button modSave = (Button) findViewById(R.id.modSave);
			modSave.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					EditText subject = null, note = null;
					if(modSave.getText().equals("Modify")){
						modSave.setText("Save");
						LinearLayout show = (LinearLayout) findViewById(R.id.show);
						show.setVisibility(View.GONE);
						LinearLayout modify = (LinearLayout) findViewById(R.id.modify);
						modify.setVisibility(View.VISIBLE);
						subject =  (EditText) findViewById(R.id.subject_mod);
						subject.setText(c.getString(c.getColumnIndex("subject")));
						note =  (EditText) findViewById(R.id.note_mod);
						note.setText(c.getString(c.getColumnIndex("note")));
						CheckBox in =  (CheckBox) findViewById(R.id.in_mod);
						CheckBox out =  (CheckBox) findViewById(R.id.out_mod);
						if(c.getString(c.getColumnIndex("incoming")).equals("1"))
							in.setChecked(true);
						if(c.getString(c.getColumnIndex("outgoing")).equals("1"))
							out.setChecked(true);
					}
					else if(modSave.getText().equals("Save")){
						modSave.setText("Modify");
						LinearLayout show = (LinearLayout) findViewById(R.id.show);
						show.setVisibility(View.VISIBLE);
						LinearLayout modify = (LinearLayout) findViewById(R.id.modify);
						modify.setVisibility(View.GONE);
						subject =  (EditText) findViewById(R.id.subject_mod);
						note =  (EditText) findViewById(R.id.note_mod);
						String in_m = "0", out_m ="0";
						CheckBox cbIn = (CheckBox) findViewById(R.id.in_mod);
						CheckBox cbOut = (CheckBox) findViewById(R.id.out_mod);
						if(cbIn.isChecked())
							in_m = "1";
						if(cbOut.isChecked())
							out_m = "1";
						db.ModifyNote(id, subject.getText().toString(), note.getText().toString(), in_m, out_m);
						final Cursor c = db.getNotesByID(id);
						Log.i("callnote", "the id: "+id);
						c.moveToFirst();
						TextView subject_m =  (TextView) findViewById(R.id.subject);
						subject_m.setText(c.getString(c.getColumnIndex("subject")));
						TextView contact =  (TextView) findViewById(R.id.contact);
						if(c.getString(c.getColumnIndex("contact_name")) == null)
							contact.setText(c.getString(c.getColumnIndex("contact")));
						else
							contact.setText(c.getString(c.getColumnIndex("contact_name")));
						TextView note_m =  (TextView) findViewById(R.id.note);
						note_m.setText(c.getString(c.getColumnIndex("note")));
						TextView in =  (TextView) findViewById(R.id.in);
						TextView out =  (TextView) findViewById(R.id.out);
						if(c.getString(c.getColumnIndex("incoming")).equals("0"))
							in.setVisibility(View.GONE);
						if(c.getString(c.getColumnIndex("outgoing")).equals("0"))
							out.setVisibility(View.GONE);
					}
				}
			});
		}catch(Exception e){
			Log.d("callnote", e.getMessage());
		}
	}
}
