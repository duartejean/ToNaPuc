package br.com.tonapuc;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Criado por Jean Duarte em 16/05/2016.
 * Baseado no website: http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/
 */
public class GPSTracker extends Service implements LocationListener {
    private final Context mContext;

    // Status do GPS
    boolean isGPSEnabled = false;

    boolean canGetLocation = false;

    Location location; // Localização
    double latitude; // Latitude
    double longitude; // Longitude

    // Distância mínima para realizar atualizações
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 metros

    // Tempo mínimo para realizar atualizações
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minuto

    // Declarando Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    /**
     * Inicialização do objeto Localização, invocado no construtor da classe
     *
     * @return Objeto com as coordenadas
     */
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // Busca o status do GPS
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (isGPSEnabled) {
                this.canGetLocation = true;
                // Se o GPS estiver ativo, busca a latitude/longitude com base nos serviços de GPS
                if (location == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
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

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    /**
     * Função para retornar a latitude
     *
     * @return double Coordenadas da Latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    /**
     * Função para retornar a longitude
     *
     * @return double Coordenadas da Longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }
}
