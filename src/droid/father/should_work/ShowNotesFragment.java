package droid.father.should_work;

import droid.father.should_work.R;

import droid.father.should_work.SwipeDetector.Action;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowNotesFragment extends ListFragment {
	View ShowNotesView;
	Database db = null;
	final SwipeDetector swipeDetector = new SwipeDetector();
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			db = new Database(getActivity().getApplicationContext());
			Cursor c = db.getNotes();
			ListAdapter listadapter = new mycursoradapter(getActivity(), R.layout.row, c, new String[] {"subject", "contact", "note"},
					new int[] {R.id.subject, R.id.contact, R.id.note});
			setListAdapter(listadapter);
			ShowNotesView = inflater.inflate(R.layout.show_notes, container, false);

		}catch(Exception e){
			Log.d("callnote", e.getMessage());
		}
		return ShowNotesView;	  
	}

	public void onResume(){
		super.onResume();
		try{
			final ListView lv = getListView();
			lv.setOnTouchListener(swipeDetector);
		}catch(Exception e){
			Log.d("callnote", e.getMessage());
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		try{
			if(swipeDetector.swipeDetected() == Action.RL){
				final TextView t = (TextView)v.findViewById(R.id.id);
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Delete Note");
				builder.setPositiveButton("Delete", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						db.DeleteNote(t.getText().toString());
						Log.i("callnote", "right to left: " + t.getText());
						//Toast.makeText(getActivity(), "Right to Left: " + t.getText()t, Toast.LENGTH_SHORT).show();
						Fragment f = new ShowNotesFragment();
						FragmentTransaction ft = getFragmentManager().beginTransaction();
						ft.replace(R.id.main, f);
						ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
						ft.commit();
						Toast.makeText(getActivity(), "Note Deleted", Toast.LENGTH_SHORT).show();
					}
				});
				builder.setNegativeButton("Cancel", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
			else if (swipeDetector.swipeDetected() != Action.LR && swipeDetector.swipeDetected() != Action.RL){
				//Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
				Bundle b = new Bundle();
				b.putString("id", ""+id);
				OneNoteFragment onenotefragment = new OneNoteFragment();
				onenotefragment.setArguments(b); 
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.main, onenotefragment);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				ft.commit();
			}
		}catch(Exception e){
			Log.d("callnote", e.getMessage());
		}
	}
}
class ViewHolder {
	TextView subject;
	TextView contact;
	TextView note;
	ImageView in;
	ImageView out;
	TextView id;
}
class mycursoradapter extends SimpleCursorAdapter{
	Context context;
	@SuppressWarnings("deprecation")
	public mycursoradapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		// TODO Auto-generated constructor stub
		Log.i("myadapter", "constructor");
		this.context = context;
	}
	@SuppressWarnings("null")
	@Override
	public void bindView(View view, final Context context, Cursor cursor) {
		ViewHolder holder;
		Log.i("myadapter", "bindView");
		try{
			if (view != null) {
				holder = new ViewHolder();
				holder.subject = (TextView) view.findViewById(R.id.subject);
				holder.contact = (TextView) view.findViewById(R.id.contact);
				holder.note = (TextView) view.findViewById(R.id.note);
				holder.in = (ImageView) view.findViewById(R.id.in);
				holder.out = (ImageView) view.findViewById(R.id.out);
				holder.id = (TextView) view.findViewById(R.id.id);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			String in = cursor.getString(cursor.getColumnIndex("incoming"));
			String out = cursor.getString(cursor.getColumnIndex("outgoing"));
			if(in.equals("1")){
				holder.in.setVisibility(View.VISIBLE);
			}
			else
				holder.in.setVisibility(View.GONE);
			if(out.equals("1")){
				holder.out.setVisibility(View.VISIBLE);
			}
			else
				holder.out.setVisibility(View.GONE);

			holder.subject.setText(cursor.getString(cursor.getColumnIndex("subject")));
			holder.note.setText(cursor.getString(cursor.getColumnIndex("note")));
			holder.id.setText(cursor.getString(cursor.getColumnIndex("_id")));
			if(cursor.getString(cursor.getColumnIndex("contact_name")) == null)
			{
				holder.contact.setText(cursor.getString(cursor.getColumnIndex("contact")));
			}
			else {
				holder.contact.setText(cursor.getString(cursor.getColumnIndex("contact_name")));
			}
		}catch(Exception e){
			Log.d("callnote", e.getMessage());
		}
	}
}