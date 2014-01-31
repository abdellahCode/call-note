package droid.father.should_work;

import droid.father.should_work.R;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class AddNoteFragment extends Fragment {
	View AddNoteView;
	String name=null, number=null;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Point size = new Point();
		Display screen = getActivity().getWindowManager().getDefaultDisplay();
		screen.getSize(size);
		Log.d("Call Note", "the screen size: " + size.x + " --y: " + size.y);
		AddNoteView = inflater.inflate(R.layout.add_note_layout, container, false);
		return AddNoteView;

	}
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		final EditText subject = (EditText) AddNoteView.findViewById(R.id.subject);
		final EditText note = (EditText) AddNoteView.findViewById(R.id.note);
		final EditText contact = (EditText) AddNoteView.findViewById(R.id.contact);

		Button b = (Button) AddNoteView.findViewById(R.id.saveNote);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(subject.getText().toString().length() == 0){
					Toast.makeText(getActivity(), "Please enter a subject", Toast.LENGTH_SHORT).show();
				}
				else if(note.getText().toString().length() == 0){
					Toast.makeText(getActivity(), "Please enter a note", Toast.LENGTH_SHORT).show();
				}
				else if(contact.getText().toString().length() == 0){
					Toast.makeText(getActivity(), "Please select a contact or enter one", Toast.LENGTH_SHORT).show();
				}
				else{
					// TODO Auto-generated method stub
					String in = "0", out ="0";
					CheckBox cbIn = (CheckBox)AddNoteView.findViewById(R.id.checkBoxIn);
					CheckBox cbOut = (CheckBox)AddNoteView.findViewById(R.id.checkBoxOut);
					if(cbIn.isChecked())
						in = "1";
					if(cbOut.isChecked())
						out = "1";
					Database db = new Database(getActivity().getApplicationContext());
					String contact_clean = null;
					if(number == null)
					{
						contact_clean = contact.getText().toString().replaceAll("[\\s\\-()]", "");
					}
					else
					{
						contact_clean = number.toString().replaceAll("[\\s\\-()]", "");
					}
					db.insert_note(subject.getText().toString(), note.getText().toString(), contact_clean,in, out, name);
					db.close();
					Toast.makeText(getActivity(), "Note Saved", Toast.LENGTH_SHORT).show();
					cbIn.setChecked(false);
					cbOut.setChecked(false);
					subject.setText("");
					note.setText("");
					contact.setText("");
				}
			}
		});

		Button buttonPickContact = (Button)AddNoteView.findViewById(R.id.browseContacts);
		buttonPickContact.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
				startActivityForResult(intent, 1);
			}});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == 1){
			if(resultCode != 0){
				Uri contactData = data.getData();
				Cursor cursor =  getActivity().managedQuery(contactData, null, null, null, null);
				cursor.moveToFirst();
				name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

				number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
				EditText contact = (EditText) AddNoteView.findViewById(R.id.contact);
				if(name==null)
				{

					contact.setText(number);
				}
				else
				{
					contact.setText(name);
				}
				//contactEmail.setText(email);
			}
		}		     


	}

}
