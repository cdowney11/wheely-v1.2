package com.alkliv.wheely;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class NearbyPreferenceActivity extends PreferenceActivity  {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prefs_activity);
		
		
	}
	
	public static class PrefsRadiusFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        // Load the preferences from an XML resource
	        addPreferencesFromResource(R.xml.prefs_radius);       
	        
	        
	        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	        
	        
	        ((CheckBoxPreference)findPreference("place_type_all_preference")).setOnPreferenceChangeListener(new OnPreferenceChangeListener() {				
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					if(((Boolean)newValue).booleanValue()){	// CheckAll
						((CheckBoxPreference)findPreference("place_type_cafe_preference")).setChecked(true);
				        ((CheckBoxPreference)findPreference("place_type_restaurant_preference")).setChecked(true);
				        ((CheckBoxPreference)findPreference("place_type_store_preference")).setChecked(true);
				        ((CheckBoxPreference)findPreference("place_type_school_preference")).setChecked(true);
				        ((CheckBoxPreference)findPreference("place_type_hospital_preference")).setChecked(true);
					}else{		// Uncheckall
						((CheckBoxPreference)findPreference("place_type_cafe_preference")).setChecked(false);
				        ((CheckBoxPreference)findPreference("place_type_restaurant_preference")).setChecked(false);
				        ((CheckBoxPreference)findPreference("place_type_store_preference")).setChecked(false);
				        ((CheckBoxPreference)findPreference("place_type_school_preference")).setChecked(false);
				        ((CheckBoxPreference)findPreference("place_type_hospital_preference")).setChecked(false);
					}
					return true;
				}
			});
	        
	    }

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			if(!key.equals("place_type_all")){		
				if (
						sharedPreferences.getBoolean("place_type_cafe_preference", false) &&
						sharedPreferences.getBoolean("place_type_restaurant_preference", false) &&
						sharedPreferences.getBoolean("place_type_store_preference", false) &&
						sharedPreferences.getBoolean("place_type_school_preference", false) &&
						sharedPreferences.getBoolean("place_type_hospital_preference", false)				
					){		// Tick "All", if all others are ticked
					((CheckBoxPreference)findPreference("place_type_all_preference")).setChecked(true);					
				}else if(
						!sharedPreferences.getBoolean("place_type_cafe_preference", false) ||
						!sharedPreferences.getBoolean("place_type_restaurant_preference", false) ||
						!sharedPreferences.getBoolean("place_type_store_preference", false) ||
						!sharedPreferences.getBoolean("place_type_school_preference", false) ||
						!sharedPreferences.getBoolean("place_type_hospital_preference", false)
						
						){ // Untick "All", if any other is unticked
					((CheckBoxPreference)findPreference("place_type_all_preference")).setChecked(false);
				}
			}
		}		
	}
}
