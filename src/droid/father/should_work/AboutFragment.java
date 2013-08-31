package droid.father.should_work;

import droid.father.should_work.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutFragment extends Fragment {
	View AboutView;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		AboutView = inflater.inflate(R.layout.about_layout, container, false);
		return AboutView;
		
		
	}
}
