package com.alkliv.wheely;

/**
 * Created with IntelliJ IDEA.
 * User: ServusKevin
 * Date: 5/11/13
 * Time: 4:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConstantValues {
	
	public static final String[] PLACE_TYPES = new String[] { "cafe", "restaurant", "store", "school", "hospital" };
	//public static final String PLACE_TYPES = "cafe|restaurant|store|school|hospital";
	
	
	public static final String SENSOR = "false";
	public static final String BROWSER_KEY = "AIzaSyC6t87fsR1PaJ5TX_yp6aon0X8ONVfIGR8";
	public static final String RANK_BY_DISTANCE = "distance";
	
	public static final String GOOGLE_PLACE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
													"key=" + BROWSER_KEY +
													"&sensor=" + SENSOR ; 
											//		"&types=" + PLACE_TYPES ;
											//		"&radius=" + RADIUS ;
											//		"&rankby=" + RANK_BY_DISTANCE ;  // on uncommenting this, comment radius
													 												
	
	
	public static final String GOOGLE_PLACE_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?" +
													"key=" + BROWSER_KEY + 
													"&sensor=" + SENSOR ;
	
	
	public static final String GOOGLE_PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo?" +
													"key=" + BROWSER_KEY +
													"&sensor=" + SENSOR ;
	
	public static final String GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json?" +
													"sensor=" + SENSOR ;
	
	
	public static final String SERVER_IP = "46.105.49.245"; // 	46.105.49.245
	
	public static final String ACCESSIBILITY_GET_URL = "http://"+ SERVER_IP +"/androidconnector/getplace.php";
	public static final String ACCESSIBILITY_INSERT_URL = "http://"+ SERVER_IP +"/androidconnector/insertplace.php";
	
	public static final String PAVEMENT_INSERT_URL = "http://"+ SERVER_IP +"/androidconnector/insertpavement.php";
	public static final String PAVEMENT_GET_URL = "http://"+ SERVER_IP +"/androidconnector/getpavements.php";
	
	
	
	
	/**
	 * Extra data passed between activities
	 */
	public static final String EXTRA_REFERENCE = "EXTRA_REFERENCE" ;
    public static final String EXTRA_LATITUDE = "EXTRA_LATITUDE";
    public static final String EXTRA_LONGITUDE = "EXTRA_LONGITUDE";
    public static final String EXTRA_NAME = "EXTRA_NAME";
    public static final String EXTRA_FORMATTED_ADDRESS = "EXTRA_FORMATTED_ADDRESS";
    public static final String EXTRA_FORMATTED_PHONE_NUMBER = "EXTRA_FORMATTED_PHONE_NUMBER";
    public static final String EXTRA_PLACE_ID = "EXTRA_PLACE_ID";
    public static final String EXTRA_RATING = "EXTRA_RATING";
    public static final String EXTRA_PHOTO_REFERENCE = "EXTRA_PHOTO_REFERENCE";
    public static final String EXTRA_ALL_NEARBY_PLACES = "EXTRA_ALL_NEARBY_PLACES";    
    
    public static final String EXTRA_ACCESSIBILITY_PLACE_ID = "EXTRA_ACCESSIBILITY_PLACE_ID";
    public static final String EXTRA_ACCESSIBILITY = "EXTRA_ACCESSIBILITY";
    public static final String EXTRA_ACCESSIBILITY_ICON = "EXTRA_ACCESSIBILITY_ICON";
    
    public static final String EXTRA_PAVEMENT_LATITUDE = "EXTRA_PAVEMENT_LATITUDE";
    public static final String EXTRA_PAVEMENT_LONGITUDE = "EXTRA_PAVEMENT_LONGITUDE";
    public static final String EXTRA_PAVEMENT_COMMENT = "EXTRA_PAVEMENT_COMMENT";
    public static final String EXTRA_PAVEMENT_LAT_LNG = "EXTRA_PAVEMENT_LAT_LNG";
    
    public static final String EXTRA_MAP_VIA = "EXTRA_MAP_VIA";
    public static final String EXTRA_MAP_VIA_MAIN_ACTIVITY = "EXTRA_MAP_VIA_MAIN_ACTIVITY";
    public static final String EXTRA_MAP_VIA_LIST = "EXTRA_MAP_VIA_LIST";
    public static final String EXTRA_MAP_VIA_PAVEMENT_DETAILS = "EXTRA_MAP_VIA_PAVEMENT_DETAILS";
    
    /**
     * Google Places JSON Fields
     */    
    public static final String FIELD_NAME = "name";
    public static final String FIELD_REFERENCE = "reference";
    public static final String FIELD_LATITUDE = "lat";
    public static final String FIELD_LONGITUDE = "lng";
    public static final String FIELD_FORMATTED_ADDRESS = "formatted_address";
    public static final String FIELD_FORMATTED_PHONE_NUMBER = "formatted_phone_number";
    public static final String FIELD_LOCATION = "location";
    public static final String FIELD_GEOMETRY = "geometry";
    public static final String FIELD_RESULT = "result";						// For Place Details API
    public static final String FIELD_RESULTS = "results";					// For Places API
    public static final String FIELD_ID = "id";								// Place ID
    public static final String FIELD_RATING = "rating";						// Place Rating
    public static final String FIELD_PHOTOS = "photos";						// Place Photos
    public static final String FIELD_PHOTO_REFERENCE = "photo_reference";	// Place Photo Reference    
    
    
    /**
     * Accessibility URL fields
     */
    
    public static final String FIELD_ACCESSIBILITY_PLACE_ID = "placeid";
    public static final String FIELD_ACCESSIBILITY = "accessibility";
    
    
    /**
     * Pavements URL fields
     */
    public static final String FIELD_PAVEMENT_PAVEMENTS = "pavements";	// Array containing comments
    public static final String FIELD_PAVEMENT_LATITUDE = "lat";			// latitude
    public static final String FIELD_PAVEMENT_LONGITUDE = "lng";		// longitude
    public static final String FIELD_PAVEMENT_COMMENT = "comment";		// Comment  
    
    public static final String PAVEMENT_MARKER_TITLE = "Pavement";
    
    
}