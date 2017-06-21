package com.balamurugan.autotest;

import android.content.ContentValues;
import android.content.Context;
import android.content.IntentSender;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.balamurugan.autotest.mapnavigator.Navigator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sothree.slidinguppanel.ScrollableViewHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.balamurugan.autotest.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, PlaceSelectionListener {

    private GoogleMap mMap;

    private Location mLastLocation;

    Button btn_show_all;

    Button btn_simulate;

    Navigator nav;


    AsyncTask task;





    SlidingUpPanelLayout slidingUpPanelLayout;

    int h2;


    //boolean polyDone = false;


    //PolygonOptions rectOptions = new PolygonOptions();

    PolylineOptions polylineOptions = new PolylineOptions();


    boolean stop = false;
    Context context;

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "CoordinatesManager";

    // Contacts table name
    private static String TABLE_COORDINATES = "coordinates";

    int height;


    public CardView breaker;

    TextView price;
    TextView type;
    TextView id;
    TextView date;

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LON = "lon";
    private static final String KEY_PRICE = "price";
    private static final String KEY_MEMBERS = "members";
    private static final String KEY_TYPE = "type";
    private static final String KEY_DATE = "date";

    Projection projection;
    public double latitude;
    public double longitude;


    Context cont2 = this;

    boolean filteredList = false;

    Polyline polyline;

    static boolean skippedSecond = false;


    class House{
        public String id;
        public Double lat;
        public Double lon;
        public Double price;
        public Double members;
        public String type;
        public String date;

        public  House(){
            id = null;
            lat = lon = null;
            price = null;
            members = null;
            type = null;
            date = null;
        }
    }

    House db = new House();
    DatabaseHandler dbase;


    List<House> filtered = new ArrayList<>();


    HashMap<Marker, Integer> markerMap = new HashMap<Marker, Integer>();

    HashMap<Marker, Integer> filteredMap = new HashMap<Marker, Integer>();


    private static LatLngBounds myCity;
    LatLng cityLatLng;
    String cityName;

    LatLng localityLatLng;
    String localityName;


    public int selectedTemp;


    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private CoordinatorLayout coordinatorLayout;

    FrameLayout fram_map ;
    Button btn_draw_State;
    static boolean Is_MAP_Moveable = false;

    // List<LatLng> val = ;

    ArrayList<LatLng> val = new ArrayList<>();


    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    ArrayList<MarkerOptions> markerList = new ArrayList<>();
    boolean marked = false;
    boolean isLoading = false;
    Place mPlace;
    Marker mMarker;

    ImageButton myLoc;
    ImageButton myHam;

    Toolbar toolbar;

    Context mContext;


    String source;
    String desti;


    Double mylat;
    Double mylng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        context = this;

        dbase = new DatabaseHandler();


        mContext = this;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        breaker = (CardView) findViewById(R.id.speedAlert);



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        myLoc = (ImageButton) findViewById(R.id.myMapLocationButton);
        myHam = (ImageButton) findViewById(R.id.hamButton);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;

     //   task = new setSimulation();




     //   RelativeLayout ns = (RelativeLayout) findViewById(R.id.nested);
     //   h2 = ns.getHeight();
        h2 = height/6;



        myLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( mLastLocation != null) {
                    LatLng home = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(home).title("My location"));
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(home)
                            .zoom(12).build();
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                else {
                    // show error retieving location
                    curLocError();
                }
            }
        });

        myHam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(drawerLayout != null) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });


        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Source");
        autocompleteFragment.setOnPlaceSelectedListener(this);

        PlaceAutocompleteFragment autocompleteFragment2 = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);
        autocompleteFragment2.setHint("Destination");
        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                desti = place.getAddress().toString();

            }

            @Override
            public void onError(Status status) {

            }
        });
        //   autocompleteFragment.setBoundsBias(myCity);



        btn_simulate = (Button) findViewById(R.id.btn_simulate);
        btn_simulate.setVisibility(View.GONE);

        btn_simulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // new setSimulation().execute();

                if(stop){
                    slidingUpPanelLayout.setPanelHeight(0);
                    btn_simulate.setText("Start trip");
                    stop =false;
                }else {
                    slidingUpPanelLayout.setPanelHeight(h2);
                    btn_simulate.setText("Stop trip");
                    stop =true;
                }

            }
        });

        btn_show_all = (Button) findViewById(R.id.btn_show_all);
        btn_show_all.setVisibility(View.GONE);

        btn_show_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();

                setMaps(source,desti);

                showSnackBar("Loading.....");
             //   GetData task = new GetData();
             //   task.purl = "http://abhishekvasudevan.com/house.php";
             //   task.execute();
            }
        });


        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        NestedScrollableViewHelper helper = new NestedScrollableViewHelper();
        slidingUpPanelLayout.setScrollableViewHelper(helper);

        slidingUpPanelLayout.setPanelHeight(0);




    }





    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setUpMap();

        // Add a marker in Sydney and move the camera
        //  LatLng sydney = new LatLng(-34, 151);
        //  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //CameraPosition cameraPosition = null;

        if( mLastLocation != null) {
            LatLng home = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(home).title("My location"));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(home)
                    .zoom(12).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        else {
            // show error retieving location
            curLocError();
        }

        btn_show_all.setVisibility(View.VISIBLE);

       btn_simulate.setVisibility(View.VISIBLE);




    }



    @Override
    public void onPlaceSelected(Place place) {
        //  Log.i(LOG_TAG, "Place Selected: " + place.getName());
        // triggerButton.setText(place.getName());
        //  MainActivity.locdone = true;

        LatLng temp = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
        /*    SharedPreferences locSave2 = getSharedPreferences("locality", 0);
        SharedPreferences.Editor editor = locSave2.edit();

        editor.putLong("latitude2",Double.doubleToLongBits(place.getLatLng().latitude));
        editor.putLong("longitude2",Double.doubleToLongBits(place.getLatLng().longitude));
        editor.commit();
*/


        source = place.getAddress().toString();

        mMap.addMarker(new MarkerOptions().position(temp).title(place.getName().toString()));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(temp)
                .zoom(12).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //filterLocality(temp);

        /*locationTextView.setText(getString(R.string.formatted_place_data, place
                .getName(), place.getAddress(), place.getPhoneNumber(), place
                .getWebsiteUri(), place.getRating(), place.getId()));
        if (!TextUtils.isEmpty(place.getAttributions())){
            attributionsTextView.setText(Html.fromHtml(place.getAttributions().toString()));
        }*/

    }

    @Override
    public void onError(Status status) {
        //   Log.e(LOG_TAG, "onError: Status = " + status.toString());
        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }








    private void setUpMap() {
        mMap.getUiSettings().setZoomControlsEnabled(true); // true to enable

        mMap.getUiSettings().setZoomGesturesEnabled(true);

        mMap.getUiSettings().setCompassEnabled(true);

        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        mMap.getUiSettings().setAllGesturesEnabled(true);

        //Marker marker_latlng = null; // MAKE THIS WHATEVER YOU WANT

    /*    CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(17.385044, 78.486671)).zoom(15.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.moveCamera(cameraUpdate); */

        mMap.getUiSettings().setRotateGesturesEnabled(true);


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

       /* CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(17.385044, 78.486671)).zoom(12).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition)); */
        mMap.setMyLocationEnabled(true); // false to disable

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (markerMap != null) {

                    int i = 0;
                    //  slidingUpPanelLayout.setPanelHeight(60);
                    try{
                    /*/   i = markerMap.get(marker);
                        House hh = filtered.get(i);
                        id.setText(hh.id);
                        price.setText(hh.price.toString());
                        type.setText(hh.type);
                        date.setText(hh.date);
                        toolbar.setTitle(hh.id); */
                        //   slidingUpPanelLayout.setPanelHeight(height/2);
                    }catch (Exception e){
                        e.printStackTrace();
                    }




                }
                return true;


            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                //mMap.clear();
                if(mMarker != null){
                    mMarker.remove();
                }



                if(skippedSecond) {
                    mMarker = mMap.addMarker(new MarkerOptions().position(point).title("Locality"));

                    //filterLocality(point);
                }



                // fetchAltitudeFromService task = new fetchAltitudeFromService();
                //    AsyncTaskCompat.executeParallel(task,null);

             /*   SharedPreferences locSave = mContext.getSharedPreferences("loc", 0);
                SharedPreferences.Editor editor = locSave.edit();
                editor.putLong("latitude",Double.doubleToLongBits(mylat));
                editor.putLong("longitude",Double.doubleToLongBits(mylng));
                editor.commit();



                Toast.makeText(mContext, "Getting altitude....", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

                alert.setTitle("Add to bookmarks?");
                alert.setMessage("Add this location to bookmarks by giving it a name.");

// Set an EditText view to get user input
                final EditText input = new EditText(mContext);
                alert.setView(input);

                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String locn = input.getText().toString();

                        //  AddPackage task = new AddPackage();

                        if(!locn.isEmpty()) {
                            //      task.locname = locn;
                            //      AsyncTaskCompat.executeParallel(task,null);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Name can't be empty - skipping", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alert.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show(); */

            }
        });
    }


    void updateMarket(MarkerOptions mymark){

        mMarker =  mMap.addMarker(mymark);

    }












    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }


    }

    /**
     * If connected get lat and long
     *
     */
    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            // Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        mLastLocation = location;

        // Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }



/*    protected void setUpNavView()
    {
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(selectedNavItemId);

        selectedTemp = selectedNavItemId;
    } */


    /**
     * Helper method to allow child classes to opt-out of having the
     * hamburger menu.
     * @return
     */







    public void curLocError(){
        // Snackbar snackbar = Snackbar
        //         .make(coordinatorLayout, "Couldn't retrieve current location...", Snackbar.LENGTH_SHORT);
        // snackbar.show();

        Toast.makeText(getApplicationContext(),"Couldn't retrieve current location...",Toast.LENGTH_SHORT).show();
    }


    public void showSnackBar(String s){
        //  Snackbar snackbar =
        //          make(coordinatorLayout, s, Snackbar.LENGTH_SHORT);
        //  snackbar.show();

        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }





    public class DatabaseHandler extends SQLiteOpenHelper {


        public DatabaseHandler() {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Creating Tables
        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_COORDINATES + "("
                    + KEY_ID + " TEXT," + KEY_LAT + " NUMBER,"
                    + KEY_LON + " NUMBER," + KEY_MEMBERS + " NUMBER," + KEY_PRICE + " NUMBER," + KEY_TYPE + " TEXT," + KEY_DATE + " TEXT" + ")";
            db.execSQL(CREATE_CONTACTS_TABLE);
        }

        // Upgrading database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COORDINATES);
            // Create tables again
            onCreate(db);
        }
        // Adding new contact
        void addCoordinates(House co) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_ID, co.id); // House code
            values.put(KEY_LAT, co.lat); // House Name
            values.put(KEY_LON, co.lon); // House sem
            values.put(KEY_PRICE, co.price); // House credits
            values.put(KEY_MEMBERS, co.members);
            values.put(KEY_TYPE, co.type);
            values.put(KEY_DATE, co.date);


            // Inserting Row
            db.insert(TABLE_COORDINATES, null, values);
            db.close(); // Closing database connection
        }

        public int  getSems(){

            int sems = -1;
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT COUNT(*) " + "FROM " + TABLE_COORDINATES;
            Cursor cursor = db.rawQuery(query, null);;
            if( cursor != null && cursor.moveToFirst() ){
                sems = Integer.parseInt(cursor.getString(0));
                cursor.close();
            }
            if(sems == -1 ){
                return  0;
            }
            return sems;
        }

        public List<House> getHouses(){

            List<House> coList = new ArrayList<House>();

            SQLiteDatabase db = this.getWritableDatabase();
            //      String query = "SELECT * " +  "FROM " + TABLE_COORDINATES + " WHERE (" + KEY_SEM + " = '" + sems + "')" ;
            //    Cursor cursor = db.query(TABLE_COORDINATES,null,KEY_SEM + "=?",new String[]{ sems },null,null,null);
            Cursor cursor = db.query(TABLE_COORDINATES,null,null,null,null,null,null);
            //    cursor.moveToFirst();
            while (cursor.moveToNext() && cursor != null ){
                House co = new House();
                co.id = cursor.getString(cursor.getColumnIndex(KEY_ID));
                co.lat = cursor.getDouble(cursor.getColumnIndex(KEY_LAT));
                co.lon = cursor.getDouble(cursor.getColumnIndex(KEY_LON));
                co.type = cursor.getString(cursor.getColumnIndex(KEY_TYPE));
                co.members = cursor.getDouble(cursor.getColumnIndex(KEY_MEMBERS));
                co.price = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE));
                co.date = cursor.getString(cursor.getColumnIndex(KEY_DATE));
                coList.add(co);
            }
            cursor.close();
            return coList;
        }

        public void clearAlready(){
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                db.execSQL("DELETE FROM " + TABLE_COORDINATES);
            }catch (SQLiteException e){
                e.printStackTrace();
            }

        }
    }



    @Override
    public void onBackPressed() {
        if(slidingUpPanelLayout.getPanelHeight() > h2){
            slidingUpPanelLayout.setPanelHeight(h2);
            return;
        }
        if(slidingUpPanelLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)){
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return;
        }
        super.onBackPressed();
    }


   /* public class NestedScrollableViewHelper extends ScrollableViewHelper {
        public int getScrollableViewScrollPosition(View scrollableView, boolean isSlidingUp) {
            if (scrollableView instanceof NestedScrollView) {
                if(isSlidingUp){
                    return scrollableView.getScrollY();
                } else {
                    NestedScrollView nsv = ((NestedScrollView) scrollableView);
                    View child = nsv.getChildAt(0);
                    return (child.getBottom() - (nsv.getHeight() + nsv.getScrollY()));
                }
            } else {
                return 0;
            }
        }
    } */



    private String getUrlFromLatLng(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }




    String getUrlFromPlace(String source, String destination){

        return "https://maps.googleapis.com/maps/api/directions/json?origin="
                + source + "&destination=" + destination + "&key=AIzaSyD21L0ioNBL_LSBtdacgymHRZ7XVcNeYWI";

    }


    public void setMaps(String source, String destination){
        //  String startAddress = null, endAddress = null;
        nav = new Navigator(mMap,source,destination);
        nav.findDirections(false);
    }



    public class NestedScrollableViewHelper extends ScrollableViewHelper {
        public int getScrollableViewScrollPosition(View scrollableView, boolean isSlidingUp) {
            if (scrollableView instanceof NestedScrollView) {
                if(isSlidingUp){
                    return scrollableView.getScrollY();
                } else {
                    NestedScrollView nsv = ((NestedScrollView) scrollableView);
                    View child = nsv.getChildAt(0);
                    return (child.getBottom() - (nsv.getHeight() + nsv.getScrollY()));
                }
            } else {
                return 0;
            }
        }
    }





    void showBreakerAlert(){

        breaker.setVisibility(View.VISIBLE);

    }

    void hideBreakerAlert(){

        breaker.setVisibility(View.GONE);

    }






}

