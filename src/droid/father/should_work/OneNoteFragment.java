package droid.father.should_work;

import droid.father.should_work.R;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
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

public class OneNoteFragment extends Fragment{

	View ShowNoteView;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final String id = getArguments().getString("id");
		final Database db = new Database(getActivity().getApplicationContext());
		final Cursor c = db.getNotesByID(id);
		Log.i("callnote", "the id: "+id);
		c.moveToFirst();
		ShowNoteView = inflater.inflate(R.layout.one_note, container, false);
		TextView subject =  (TextView) ShowNoteView.findViewById(R.id.subject);
		subject.setText(c.getString(c.getColumnIndex("subject")));
		TextView contact =  (TextView) ShowNoteView.findViewById(R.id.contact);
		if(c.getString(c.getColumnIndex("contact_name")) != null)
		{
			contact.setText(c.getString(c.getColumnIndex("contact_name")));
		}
		else{
			contact.setText(c.getString(c.getColumnIndex("contact")));
		}
		TextView note =  (TextView) ShowNoteView.findViewById(R.id.note);
		note.setText(c.getString(c.getColumnIndex("note")));
		TextView in =  (TextView) ShowNoteView.findViewById(R.id.in);
		TextView out =  (TextView) ShowNoteView.findViewById(R.id.out);
		if(c.getString(c.getColumnIndex("incoming")).equals("0"))
			in.setVisibility(View.GONE);
		else
			in.setVisibility(View.VISIBLE);
		if(c.getString(c.getColumnIndex("outgoing")).equals("0"))
			out.setVisibility(View.GONE);
		else 
			out.setVisibility(View.VISIBLE);

		final Button modSave = (Button) ShowNoteView.findViewById(R.id.modSave);
		modSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				EditText subject = null, note = null;
				if(modSave.getText().equals("Modify")){
					modSave.setText("Save");
					LinearLayout show = (LinearLayout) ShowNoteView.findViewById(R.id.show);
					show.setVisibility(View.GONE);
					LinearLayout modify = (LinearLayout) ShowNoteView.findViewById(R.id.modify);
					modify.setVisibility(View.VISIBLE);
					subject =  (EditText) ShowNoteView.findViewById(R.id.subject_mod);
					subject.setText(c.getString(c.getColumnIndex("subject")));
					//					EditText contact =  (EditText) ShowNoteView.findViewById(R.id.contact_mod);
					//					contact.setText("Contact: \t:"+c.getString(c.getColumnIndex("contact")));
					note =  (EditText) ShowNoteView.findViewById(R.id.note_mod);
					note.setText(c.getString(c.getColumnIndex("note")));
					CheckBox in =  (CheckBox) ShowNoteView.findViewById(R.id.in_mod);
					CheckBox out =  (CheckBox) ShowNoteView.findViewById(R.id.out_mod);
					if(c.getString(c.getColumnIndex("incoming")).equals("1"))
						in.setChecked(true);
					if(c.getString(c.getColumnIndex("outgoing")).equals("1"))
						out.setChecked(true);
				}
				else if(modSave.getText().equals("Save")){
					modSave.setText("Modify");
					LinearLayout show = (LinearLayout) ShowNoteView.findViewById(R.id.show);
					show.setVisibility(View.VISIBLE);
					LinearLayout modify = (LinearLayout) ShowNoteView.findViewById(R.id.modify);
					modify.setVisibility(View.GONE);
					subject =  (EditText) ShowNoteView.findViewById(R.id.subject_mod);
					note =  (EditText) ShowNoteView.findViewById(R.id.note_mod);
					String in_m = "0", out_m ="0";
					CheckBox cbIn = (CheckBox)ShowNoteView.findViewById(R.id.in_mod);
					CheckBox cbOut = (CheckBox)ShowNoteView.findViewById(R.id.out_mod);
					if(cbIn.isChecked())
						in_m = "1";
					if(cbOut.isChecked())
						out_m = "1";
					db.ModifyNote(id, subject.getText().toString(), note.getText().toString(), in_m, out_m);
					final Cursor c = db.getNotesByID(id);
					Log.i("callnote", "the id: "+id);
					c.moveToFirst();
					TextView subject_m =  (TextView) ShowNoteView.findViewById(R.id.subject);
					subject_m.setText(c.getString(c.getColumnIndex("subject")));
					TextView contact =  (TextView) ShowNoteView.findViewById(R.id.contact);
					if(c.getString(c.getColumnIndex("contact_name")) == null)
						contact.setText(c.getString(c.getColumnIndex("contact")));
					else
						contact.setText(c.getString(c.getColumnIndex("contact_name")));
					TextView note_m =  (TextView) ShowNoteView.findViewById(R.id.note);
					note_m.setText(c.getString(c.getColumnIndex("note")));
					TextView in =  (TextView) ShowNoteView.findViewById(R.id.in);
					TextView out =  (TextView) ShowNoteView.findViewById(R.id.out);
					if(c.getString(c.getColumnIndex("incoming")).equals("0"))
						in.setVisibility(View.GONE);
					if(c.getString(c.getColumnIndex("outgoing")).equals("0"))
						out.setVisibility(View.GONE);


				}
			}
		});
		return ShowNoteView;


	}
}
