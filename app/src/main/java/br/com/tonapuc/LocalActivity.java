package br.com.tonapuc;

import android.app.Activity;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocalActivity extends Activity implements LocationListener {
    // Constantes com as coordenadas a PUC Minas em Betim
    private static final double latitudePUC = -19.955055;
    private static final double longitudePUC = -44.198153;

    private TextView lblLatitude, lblLongitude, lblDistancia, lblStatus;
    double latitude, longitude;
    public GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cabecalho_layout);

        // Busca os elementos da página
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        lblLatitude = (TextView) findViewById(R.id.lbl_latitude);
        lblLongitude = (TextView) findViewById(R.id.lbl_longitude);
        lblStatus = (TextView) findViewById(R.id.lbl_status);
        lblDistancia = (TextView) findViewById(R.id.lbl_distancia);

        try {
            MeuLocal();
        } catch (Exception e) {
            Toast.makeText(this, "Ocorreu um erro, tente novamente", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método para buscar a localização do dispositivo baseado no GPS
     */
    public void MeuLocal() {
        LocationManager locationManager;

        // Cria um LocationManager para pegar o LOCATION_SERVICE do sistema
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Pega o nome do melhor provider
        String provider = locationManager.getBestProvider(new Criteria(), true);

        // Usa o provider para descobrir a localização
        Location location = locationManager.getLastKnownLocation(provider);

        // Busca a atividade do GPS a cada minuto
        locationManager.requestLocationUpdates(provider, 60000, 0, this);

        // Escreve as coordenadas na tela
        escreveCoordenadas(location);

        // Monta os pontos no mapa
        desenhaPonto(location);
    }

    /**
     * Método para escrever as coordenadas e a distância na tela
     *
     * @param location Objeto com a localização
     */
    public void escreveCoordenadas(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        double distancia = DistanciaEntrePontos(latitude, longitude);

        // Preenche as informações nos componentes
        lblLatitude.setText(String.format("%s", latitude));
        lblLongitude.setText(String.format("%s", longitude));
        lblDistancia.setText(String.format("%s", EscreveDistancia(distancia)));

        // Status de acordo com a distância
        if (distancia > 500) {
            lblStatus.setText(String.format("%s", "Você não está na PUC Minas em Betim!"));
        } else {
            lblStatus.setText(String.format("%s", "Você está na PUC Minas em Betim, seja muito bem-vindo(a)!"));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        MeuLocal();
        Log.d("Mudou de lugar", String.format("%s - %s", latitude, longitude));
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

    /**
     * Método que recebe as coordenadas da latitude/longitude e calcula a distância até as coordenadas da PUC Minas em Betim
     *
     * @param latitude  Coordenadas da latitude
     * @param longitude Coordenadas da longitude
     * @return double Distância entre os pontos em metros
     */
    private double DistanciaEntrePontos(double latitude, double longitude) {
        Location locationA = new Location("PUC Minas");
        locationA.setLatitude(latitudePUC);
        locationA.setLongitude(longitudePUC);

        Location locationB = new Location("Smartphone");
        locationB.setLatitude(latitude);
        locationB.setLongitude(longitude);

        return locationB.distanceTo(locationA);
    }

    /**
     * Método para escrever distância em KM ou Metros
     *
     * @param distancia Distância em metros
     * @return String com a distância formatada em KM ou Metros
     */
    private String EscreveDistancia(double distancia) {
        String[] arrayDistancia = Double.toString(distancia).split("\\.");

        if (distancia < 500) {
            return String.format("%s metros", arrayDistancia[0]);
        } else {
            return String.format("%s,%s Km", String.valueOf((int) (Double.valueOf(arrayDistancia[0]) / 1000)).split("\\.")[0], String.valueOf((int) (Double.valueOf(arrayDistancia[0]) % 1000)).split("\\.")[0]);
        }
    }

    /**
     * Método para criar os marcadores no mapa, na localização atual e na PUC
     *
     * @param location Objeto com os dados do local atual
     */
    private void desenhaPonto(Location location) {
        mMap.clear();

        // Converte a localização atual e da PUC em um objeto para ser usado na API de mapas
        LatLng localAtual = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng localPUC = new LatLng(latitudePUC, longitudePUC);

        // Define o zoom no local atual
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(localAtual, 14));

        // Adiciona um marcador no local da PUC Betim
        mMap.addMarker(new MarkerOptions()
                .title("PUC")
                .snippet(String.format("%s,%s", latitudePUC, longitudePUC))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .position(localPUC));

        // Adiciona um círculo cobrindo o raio de 500 metros da PUC em Betim
        mMap.addCircle(new CircleOptions()
                .center(localPUC)
                .radius(500)
                .strokeColor(Color.argb(90, 153, 153, 255))
                .fillColor(Color.argb(70, 153, 153, 255)));

        // Adiciona um marcador no local atual do dispositivo
        mMap.addMarker(new MarkerOptions()
                .title("Meu Local")
                .snippet(String.format("%s,%s", location.getLatitude(), location.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .position(localAtual));
    }
}
