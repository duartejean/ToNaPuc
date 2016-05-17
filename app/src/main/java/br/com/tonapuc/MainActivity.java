package br.com.tonapuc;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    // Constantes com as coordenadas a PUC Minas em Betim
    private static final double latitudePUC = -19.955055;
    private static final double longitudePUC = -44.198153;

    private TextView lblLatitude, lblLongitude, lblDistancia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblLatitude = (TextView) findViewById(R.id.lbl_latitude);
        lblLongitude = (TextView) findViewById(R.id.lbl_longitude);
        lblDistancia = (TextView) findViewById(R.id.lbl_distancia);
    }

    /**
     * Método para buscar a localização do dispositivo
     *
     * @param view - Componente que invocou o método
     */
    public void Localizar(View view) {
        GPSTracker Local = new GPSTracker(this);
        double latitude = Local.getLatitude();
        double longitude = Local.getLongitude();

        lblLatitude.setText(String.format("%s", latitude));
        lblLongitude.setText(String.format("%s", longitude));

        double distancia = DistanciaEntrePontos(latitude, longitude);
        if (distancia > 500) {
            lblDistancia.setText(String.format("Você está longe da PUC Minas em Betim, a %s de distância.", EscreveDistancia(distancia)));
        } else {
            lblDistancia.setText("Você está dentro do ambiente da PUC Minas em Betim!");
        }
    }

    /**
     * Método que recebe as coordenadas da latitude/longitude e calcula a distância até as coordenadas da PUC Minas em Betim
     *
     * @param latitude  - Coordenadas da latitude
     * @param longitude - Coordenadas da longitude
     * @return double - Distância entre os pontos
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
     * @param distancia - Distância em metros
     * @return String com a distância formatada
     */
    private String EscreveDistancia(double distancia) {
        if (distancia < 1000) {
            return String.format("%sm", distancia);
        } else {
            int KM = (int) distancia / 1000;
            double M = distancia - KM;
            return String.format("%s.%s Km", KM, M);
        }
    }
}
