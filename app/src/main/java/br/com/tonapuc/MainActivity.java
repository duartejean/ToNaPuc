package br.com.tonapuc;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    // Constantes com as coordenadas a PUC Minas em Betim
    private static final double latitudePUC = -19.955055;
    private static final double longitudePUC = -44.198153;

    private TextView lblLatitude, lblLongitude, lblDistancia, lblStatus;
    private LinearLayout linearLatitude, linearLongitude, linearDistancia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblLatitude = (TextView) findViewById(R.id.lbl_latitude);
        lblLongitude = (TextView) findViewById(R.id.lbl_longitude);
        lblStatus = (TextView) findViewById(R.id.lbl_status);
        lblDistancia = (TextView) findViewById(R.id.lbl_distancia);

        linearLatitude = (LinearLayout) findViewById(R.id.linear_latitude);
        linearLongitude = (LinearLayout) findViewById(R.id.linear_longitude);
        linearDistancia = (LinearLayout) findViewById(R.id.linear_distancia);
    }

    /**
     * Método para buscar a localização do dispositivo
     *
     * @param view - Componente que invocou o método
     */
    public void Localizar(View view) {
        GPSTracker Local = new GPSTracker(this);

        // Busca os dados do local
        double latitude = Local.getLatitude();
        double longitude = Local.getLongitude();
        double distancia = DistanciaEntrePontos(latitude, longitude);

        // Preenche as informações nos componentes
        lblLatitude.setText(String.format("%s", latitude));
        lblLongitude.setText(String.format("%s", longitude));
        lblDistancia.setText(String.format("%s", EscreveDistancia(distancia)));

        // Exibe os layouts com as informações
        linearLatitude.setVisibility(View.VISIBLE);
        linearLongitude.setVisibility(View.VISIBLE);
        linearDistancia.setVisibility(View.VISIBLE);

        // Status de acordo com a distância
        if (distancia > 500) {
            lblStatus.setText(String.format("%s", "Você não está na PUC Minas em Betim!"));
        } else {
            lblStatus.setText(String.format("%s", "Você está na PUC Minas em Betim, seja muito bem-vindo(a)!"));
        }
    }

    /**
     * Método que recebe as coordenadas da latitude/longitude e calcula a distância até as coordenadas da PUC Minas em Betim
     *
     * @param latitude  - Coordenadas da latitude
     * @param longitude - Coordenadas da longitude
     * @return double - Distância entre os pontos em metros
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
     * @return String com a distância formatada em KM ou Metros
     */
    private String EscreveDistancia(double distancia) {
        if (distancia < 500) {
            return String.format("%s metros", (int) distancia);
        } else {
            double KM = (int) (distancia / 1000);
            //double M = distancia - KM;
            return String.format("%s Km", KM);
        }
    }
}
