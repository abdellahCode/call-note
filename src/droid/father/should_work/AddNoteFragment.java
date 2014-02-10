package droid.father.should_work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import droid.father.should_work.R;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class AddNoteFragment extends Fragment {
	View AddNoteView;
	String name=null, number=null;
	private ArrayList<Map<String, String>> mPeopleList;
	private SimpleAdapter mAdapter;
	private AutoCompleteTextView mTxtPhoneNo;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AddNoteView = inflater.inflate(R.layout.add_note_layout, container, false);
		return AddNoteView;

	}
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		try{
			final EditText subject = (EditText) AddNoteView.findViewById(R.id.subject);
			final EditText note = (EditText) AddNoteView.findViewById(R.id.note);
			final EditText contact = (EditText) AddNoteView.findViewById(R.id.contact);
			Button AddNoteButton = (Button) AddNoteView.findViewById(R.id.saveNote);
			mPeopleList = new ArrayList<Map<String, String>>();
			mTxtPhoneNo = (AutoCompleteTextView) getActivity().findViewById(R.id.contact);

			PopulatePeopleList();


			mAdapter = new SimpleAdapter(getActivity(), mPeopleList, R.layout.autocompleterow ,new String[] { "Name", "Phone"}, new int[] { R.id.cname, R.id.cnumber });

			mTxtPhoneNo.setAdapter(mAdapter);

			mTxtPhoneNo.setOnItemClickListener(new OnItemClickListener() {

				@Override public void onItemClick(AdapterView<?> av, View arg1, int index, long arg3) {
					Map<String, String> map = (Map<String, String>) av.getItemAtPosition(index);
					name = map.get("Name");
					number = map.get("Phone");
					mTxtPhoneNo.setText(""+name);

				}
			});

			AddNoteButton.setOnClickListener(new OnClickListener() {
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

		}catch(Exception e){
			Log.d("callnote", e.getMessage());
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		try{
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
				}
			}		     
		}catch(Exception e){
			Log.d("callnote", e.getMessage());
		}


	}

	public void PopulatePeopleList(){
		try{
			mPeopleList.clear();

			Cursor people = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

			while (people.moveToNext())
			{
				String contactName = people.getString(people.getColumnIndex(
						ContactsContract.Contacts.DISPLAY_NAME));

				String contactId = people.getString(people.getColumnIndex(
						ContactsContract.Contacts._ID));
				String hasPhone = people.getString(people.getColumnIndex(
						ContactsContract.Contacts.HAS_PHONE_NUMBER));

				if ((Integer.parseInt(hasPhone) > 0))
				{
					// You know have the number so now query it like this
					Cursor phones = getActivity().getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,
							null, null);
					while (phones.moveToNext()) {

						//store numbers and display a dialog letting the user select which.
						String phoneNumber = phones.getString(
								phones.getColumnIndex(
										ContactsContract.CommonDataKinds.Phone.NUMBER));

						String numberType = phones.getString(phones.getColumnIndex(
								ContactsContract.CommonDataKinds.Phone.TYPE));

						Map<String, String> NamePhoneType = new HashMap<String, String>();

						NamePhoneType.put("Name", contactName);
						NamePhoneType.put("Phone", phoneNumber);

						if(numberType.equals("0"))
							NamePhoneType.put("Type", "Work");
						else
							if(numberType.equals("1"))
								NamePhoneType.put("Type", "Home");
							else if(numberType.equals("2"))
								NamePhoneType.put("Type",  "Mobile");
							else
								NamePhoneType.put("Type", "Other");

						//Then add this map to the list.
						mPeopleList.add(NamePhoneType);
					}
					phones.close();
				}
			}
			people.close();

			getActivity().startManagingCursor(people);
		}catch(Exception e){
			Log.d("callnote", e.getMessage());
		}

	}
}
