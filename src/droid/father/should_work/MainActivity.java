package droid.father.should_work;

import java.util.ArrayList;

import droid.father.should_work.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ActionProvider;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.ads.*;


public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try{
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
			Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
			int behindWidth = 0;
			if(android.os.Build.VERSION.SDK_INT > 10){
				screen.getSize(size);		
				behindWidth = (size.x * 45)/100;
				menu.setBehindWidth(behindWidth);
				getActionBar().setHomeButtonEnabled(true);
			}
			else{			
				int width = display.getWidth();
				menu.setBehindWidth((width* 45)/100);
			}

			menu.setMenu(R.layout.menu_frame);
			menu.setShadowWidth(30);
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
					if(tv.getText().toString().equals("Add Note")){
						Fragment f = new AddNoteFragment();
						FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
						ft.replace(R.id.main, f);
						ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
						ft.commit();

					}
					else if(tv.getText().toString().equals("Show Notes")){
						Fragment f = new ShowNotesFragment();
						FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
						ft.replace(R.id.main, f);
						ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
						ft.commit();
					}
					else if(tv.getText().toString().equals("About")){
						Fragment f = new AboutFragment();
						FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
						ft.replace(R.id.main, f);
						ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
						ft.commit();
					}
				}
			});
			
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			Fragment addnote = new ShowNotesFragment();
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
		}catch(Exception e){
			Log.d("callnote", e.getMessage());
		}
	}
	private ActionProvider ap;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.add_note).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Fragment f = new AddNoteFragment();
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.main, f);
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				ft.commit();
				return true;
			}
		});
		if(android.os.Build.VERSION.SDK_INT > 13){
			ap = (ActionProvider) menu.findItem(R.id.add_note).getActionProvider();
			menu.findItem(R.id.add_note).setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					Fragment f = new AddNoteFragment();
					FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
					ft.replace(R.id.main, f);
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					ft.commit();
					return true;
				}
			});
		}
		return true;
	}

}
