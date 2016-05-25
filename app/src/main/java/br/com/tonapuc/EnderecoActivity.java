package br.com.tonapuc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Criado por Jean em 25/05/2016.
 */
public class EnderecoActivity extends Activity implements LocationListener {
    public GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.endereco_layout);

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        LocationManager locationManager;

        // Get the LocationManager object from the System Service LOCATION_SERVICE
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Create a criteria object needed to retrieve the provider
        Criteria criteria = new Criteria();

        // Get the name of the best available provider
        String provider = locationManager.getBestProvider(criteria, true);

        // We can use the provider immediately to get the last known location
        Location location = locationManager.getLastKnownLocation(provider);

        // request that the provider send this activity GPS updates every 20 seconds
        locationManager.requestLocationUpdates(provider, 20000, 0, this);
    }

    public void Localizar(View view) {
        try {

        } catch (Exception e) {

        } finally {
            Toast.makeText(this, "Ocorreu um erro, tente novamente", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mMap != null) {
            drawMarker(location);
        }
    }

    private void drawMarker(Location location) {
        mMap.clear();

        //  convert the location object to a LatLng object that can be used by the map API
        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

        // zoom to the current location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 16));

        // add a marker to the map indicating our current position
        mMap.addMarker(new MarkerOptions()
                .position(currentPosition)
                .snippet("Lat:" + location.getLatitude() + "Lng:" + location.getLongitude()));
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
