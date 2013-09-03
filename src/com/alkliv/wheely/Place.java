package com.alkliv.wheely;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable{
	// Latitude of the place
	String mLat="";
	
	// Longitude of the place
	String mLng="";
	
	// Place Name
	String mPlaceName="";
	
	// Reference of the place
	String mReference="";
	
	// Place ID
	String mPlaceId="";
	
	// Accessibility
	String mAccessibility="";
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}		
	
	/** Writing Place object data to Parcel */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mLat);
		dest.writeString(mLng);
		dest.writeString(mPlaceName);
		dest.writeString(mReference);
		dest.writeString(mPlaceId);
		dest.writeString(mAccessibility);
	}
	
	public Place(){		
	}
	
	/** Initializing Place object from Parcel object */
	private Place(Parcel in){
		this.mLat = in.readString();
		this.mLng = in.readString();
		this.mPlaceName = in.readString();
		this.mReference = in.readString();
		this.mPlaceId = in.readString();
		this.mAccessibility = in.readString();
	}
	
	
	/** Generates an instance of Place class from Parcel */
	public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>(){
		@Override
		public Place createFromParcel(Parcel source) {			
			return new Place(source);
		}

		@Override
		public Place[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}		
	};
}