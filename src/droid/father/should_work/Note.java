package droid.father.should_work;

import droid.father.should_work.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Note extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note);
		TextView subject = (TextView) findViewById(R.id.subject);
		TextView note = (TextView) findViewById(R.id.note);
		final Bundle b = getIntent().getExtras();
		subject.setText(b.getString("subject"));
		note.setText(b.getString("note"));
		Button dismiss = (Button)findViewById(R.id.button1);
		final Database db = new Database(getApplicationContext());
		dismiss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		Button delete_note = (Button) findViewById(R.id.button2);
		delete_note.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				db.DeleteNote(b.getString("ID"));
				Toast.makeText(getApplicationContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
				finish();

			}
		});

		Button call = (Button) findViewById(R.id.button3);
		String contact = null;

		Display screen = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		screen.getSize(size);

		if(b.getString("out").equals("1")){
			Log.i("callnote", "out is 1");
			call.setVisibility(View.VISIBLE);
			contact = b.getString("contact");
		}
		LayoutParams params = getWindow().getAttributes();
		params.gravity = Gravity.CENTER;
		params.width = size.x - 10;
		params.alpha = (float) 0.9;
		getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
		getWindow().getDecorView().setBackgroundColor(Color.parseColor("darkgray"));

		final String c = contact;
		call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:"+c));
				SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
						getApplicationContext().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString("out", "1");
				editor.apply();
				startActivity(callIntent);
				finish();


			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.note, menu);
		return true;
	}

}
