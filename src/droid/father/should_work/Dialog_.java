package droid.father.should_work;

import droid.father.should_work.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Dialog_ extends Activity{

	@Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dialog);
            TextView subject = (TextView) findViewById(R.id.subject);
            TextView note = (TextView) findViewById(R.id.note);
            Bundle b = getIntent().getExtras();
            subject.setText(b.getString("subject"));
            note.setText(b.getString("note"));
    }
	
}
