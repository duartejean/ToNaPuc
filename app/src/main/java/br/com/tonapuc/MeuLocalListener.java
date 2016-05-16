package br.com.tonapuc;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

/***
 * Criado por Jean Duarte em 16/05/2016.
 */
public class MeuLocalListener implements LocationListener {
    public static double latitude;
    public static double longitude;

    @Override
    public void onLocationChanged(Location loc) {
        loc.getLatitude();
        loc.getLongitude();
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        //print "Currently GPS is Disabled";
        Toast.makeText(null, "O GPS está desabilitado, verifique!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(null, "O GPS está habilitado, verifique!", Toast.LENGTH_SHORT).show();
        //print "GPS got Enabled";
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
