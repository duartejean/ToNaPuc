package br.com.tonapuc;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import android.widget.Toast;

public class LocalActivity extends Activity {
    // Constantes com as coordenadas a PUC Minas em Betim
    private static final double latitudePUC = -19.955055;
    private static final double longitudePUC = -44.198153;

    private TextView lblLatitude, lblLongitude, lblDistancia, lblStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_layout);

        lblLatitude = (TextView) findViewById(R.id.lbl_latitude);
        lblLongitude = (TextView) findViewById(R.id.lbl_longitude);
        lblStatus = (TextView) findViewById(R.id.lbl_status);
        lblDistancia = (TextView) findViewById(R.id.lbl_distancia);

        try {
            Localizar();
        } catch (Exception e) {

        } finally {
            Toast.makeText(this, "Ocorreu um erro, tente novamente", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método para buscar a localização do dispositivo
     */
    public void Localizar() {
        GPS Local = new GPS(this);

        // Busca os dados do local
        double latitude = Local.getLatitude();
        double longitude = Local.getLongitude();
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
        Toast.makeText(this, "" + distancia, Toast.LENGTH_SHORT).show();
        String[] arrayDistancia = Double.toString(distancia).split("\\.");

        if (distancia < 500) {
            return String.format("%s metros", arrayDistancia[0]);
        } else {
            return String.format("%s,%s Km", String.valueOf((int) (Double.valueOf(arrayDistancia[0]) / 1000)).split("\\.")[0], String.valueOf((int) (Double.valueOf(arrayDistancia[0]) % 1000)).split("\\.")[0]);
        }
    }
}
