package droid.father.should_work;

import java.util.ArrayList;

import droid.father.should_work.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Point;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ActionProvider;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.ads.*;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TelephonyManager telephonyManager1 = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
	    String imei = telephonyManager1.getDeviceId();
		Log.d("Call Note", "Device ID: " + imei);
		SlidingMenu menu;
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidth(5);
		menu.setFadeEnabled(true);
		menu.setFadeDegree(0.4f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		Display screen = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		screen.getSize(size);
		int behindWidth = size.x / 2;
		menu.setBehindWidth(behindWidth);
		menu.setMenu(R.layout.menu_frame);
		menu.setShadowWidth(30);
		
		getActionBar().setHomeButtonEnabled(true);				
		//getActionBar().setCustomView(R.layout.sliding_list_item);
		
		
		ActionBar b;
		final ListView listview = (ListView) findViewById(R.id.menu);
		String[] values = new String[] { "Add Note", "Show Notes", "About"};

		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < values.length; ++i) {
			list.add(values[i]);
		}
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView tv = (TextView) arg1;
				//Toast.makeText(getApplicationContext(), "Menu clicked: "+tv.getText().toString()+" - "+arg2+" - "+arg3, Toast.LENGTH_SHORT).show();
				if(tv.getText().toString().equals("Add Note")){
					Fragment f = new AddNoteFragment();
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					ft.replace(R.id.main, f);
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					ft.commit();

				}
				else if(tv.getText().toString().equals("Show Notes")){
					Fragment f = new ShowNotesFragment();
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					ft.replace(R.id.main, f);
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					ft.commit();
				}
				else if(tv.getText().toString().equals("About")){
					Fragment f = new AboutFragment();
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					ft.replace(R.id.main, f);
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					ft.commit();
				}
			}
		});



		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment addnote = new AddNoteFragment();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		AddNoteFragment addnotefragment = (AddNoteFragment) fm.findFragmentById(R.id.main);

		if(addnotefragment != null){
			if(!addnotefragment.isAdded()){
				ft.add(R.id.main, addnote);
				ft.commit();
			}

		}
		else{
			ft.add(R.id.main, addnote);
			ft.commit();
		}
	
		AdView av = (AdView) this.findViewById(R.id.adView);
		av.loadAd(new AdRequest());
	}
	private ActionProvider ap, donate;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 //Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		/*donate = (ActionProvider) menu.findItem(R.id.donate).getActionProvider();
		menu.findItem(R.id.donate).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), "Add note clicked", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getApplicationContext(), DonateDialog.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplicationContext().startActivity(i);
				return true;
			}
		});*/
		ap = (ActionProvider) menu.findItem(R.id.add_note).getActionProvider();
		menu.findItem(R.id.add_note).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), "Add note clicked", Toast.LENGTH_SHORT).show();
				Fragment f = new AddNoteFragment();
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.main, f);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				ft.commit();
				return true;
			}
		});
		
		
		
		return true;
	}

}
