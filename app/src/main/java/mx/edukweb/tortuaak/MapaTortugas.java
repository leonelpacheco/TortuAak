package mx.edukweb.tortuaak;

import android.app.Activity;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



/**
 * Created by Leonel on 27/02/2015.
 */
public class MapaTortugas extends Fragment implements LocationProvider.LocationCallback {


    public static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private LocationProvider mLocationProvider;

    SQLControlador dbconeccion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);

        mLocationProvider = new LocationProvider(getActivity(), this);
        dbconeccion = new SQLControlador(getActivity());
        setUpMapIfNeeded();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mLocationProvider.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mLocationProvider.disconnect();
    }

    private void setUpMapIfNeeded() {

        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.

            FragmentManager myFM = getActivity().getSupportFragmentManager();

            final SupportMapFragment myMAPF;

            myMAPF = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mMap = myMAPF.getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }

       /*
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapa))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }


        */
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        dbconeccion.abrirBaseDeDatos();
        Cursor c = dbconeccion.leerDatos();
        dbconeccion.cerrar();

        LatLng latLng2;
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String codigo= c.getString(0);
                String nombre = c.getString(1);
                latLng2 = new LatLng(Double.parseDouble(c.getString(3)), Double.parseDouble(c.getString(4)));
               // mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(c.getString(3)), Double.parseDouble(c.getString(4)))).title(c.getString(2)));
                MarkerOptions options = new MarkerOptions()
                        .position(latLng2)
                        .snippet("Nido #: " + c.getString(0) +"\n Fecha: "+ c.getString(5))
                        .icon (BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher))
                        .flat(true)
                        .title("Cantidad de huevos: "+c.getString(2) );
                mMap.addMarker(options);

            } while(c.moveToNext());
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng2)      // Sets the center of the map to LatLng (refer to previous snippet)
                    .zoom(12)                   // Sets the zoom
                    //.bearing(90)                // Sets the orientation of the camera to east
                    //.tilt(10)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

       /*
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .snippet ("Nido #:")
                .icon (BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher))
                .flat(true)
                .title("Tipo de Tortuga!");
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        */
    }
}//Fin de clase
