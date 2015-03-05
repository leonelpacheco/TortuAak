package mx.edukweb.tortuaak;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;


public class Anidacion extends Fragment implements LocationProvider.LocationCallback{

    double currentLatitude, currentLongitude ;
    SQLControlador dbconeccion;
    ListView lista;
    String resp;

    private LocationProvider mLocationProvider;

    public static final String TAG = MapsActivity.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registro, container, false);

        final EditText huevos = (EditText) rootView.findViewById(R.id.txtHuevos);
        final EditText playa = (EditText) rootView.findViewById(R.id.txtPlaya);
        final TextView responsable = (TextView) rootView.findViewById(R.id.txtResponsable);
        lista = (ListView) rootView.findViewById(R.id.listAnidaciones);

        dbconeccion = new SQLControlador(getActivity());

       SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(getActivity());

        resp = sp.getString("username", "");
        responsable.setText(resp);
        Button btnRegistrar = (Button) rootView.findViewById(R.id.btnRegistrar);
/*
        //Obtener instancia del GameSpinner
        final Spinner playa = (Spinner) rootView.findViewById(R.id.spnPlaya);
        //ArrayAdapter para conectar el Spinner a nuestros recursos strings.xml
        ArrayAdapter<CharSequence> adapter;
//Asignas el origen de datos desde los recursos
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Playas,
                android.R.layout.simple_spinner_item);

//Asignas el layout a inflar para cada elemento
//al momento de desplegar la lista
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//Seteas el adaptador
        playa.setAdapter(adapter);
*/

        mLocationProvider = new LocationProvider(getActivity(), this);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbconeccion.abrirBaseDeDatos();
                Log.d(TAG, Double.toString(currentLatitude));
                Log.d(TAG, Double.toString(currentLongitude));
                String lat =Double.toString(currentLatitude);
                String longitud =Double.toString(currentLongitude);

                String huevo = huevos.getText().toString();

                dbconeccion.insertarDatos(resp, huevo, playa.getText().toString(), lat, longitud);

                Cursor cursor = dbconeccion.leerDatos();
                dbconeccion.cerrar();

                String[] from = new String[] {
                        DBhelper.REGISTRO_ID,
                        DBhelper.REGISTRO_RESPONSABLE
                       // DBhelper.REGISTRO_LAT,
                       // DBhelper.REGISTRO_LON
                };
                int[] to = new int[] {
                        R.id.miembro_id,
                        R.id.miembro_nombre
                       /*
                        R.id.txtHuevos,
                        R.id.txtLat,
                        R.id.txtLong
                        */

                };

                /*
                SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                       getActivity(), R.layout.formato_fila, cursor, from, to);

                adapter.notifyDataSetChanged();
                lista.setAdapter(adapter);
                */

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment fragment = null;
                fragment = new MapaTortugas();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();



            }
        });


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mLocationProvider.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mLocationProvider.disconnect();
    }


    @Override
    public void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        currentLatitude = location.getLatitude();
         currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
    }

}//Fin de clase