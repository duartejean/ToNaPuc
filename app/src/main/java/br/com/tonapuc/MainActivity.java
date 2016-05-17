package br.com.tonapuc;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView lblLatitude, lblLongitude, lblDistancia;
    private static final double latitudePUC = -19.955055;
    private static final double longitudePUC = -44.198153;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblLatitude = (TextView) findViewById(R.id.lbl_latitude);
        lblLongitude = (TextView) findViewById(R.id.lbl_longitude);
        lblDistancia = (TextView) findViewById(R.id.lbl_distancia);
    }

    public void Localizar(View view) {
        GPSTracker Local = new GPSTracker(this);
        lblLatitude.setText(String.format("%s", Local.getLatitude()));
        lblLongitude.setText(String.format("%s", Local.getLongitude()));

        double distancia = DistanciaEntrePontos();
        if (distancia > 500) {
            lblDistancia.setText(String.format("Você está longe da PUC Minas em Betim, a %sm de distância.", distancia));
        } else {
            lblDistancia.setText(String.format("Você está dentro do ambiente da PUC Minas em Betim!"));
        }
    }

    private double DistanciaEntrePontos() {
        GPSTracker Local = new GPSTracker(this);

        Location locationA = new Location("PUC Minas");
        locationA.setLatitude(latitudePUC);
        locationA.setLongitude(longitudePUC);

        Location locationB = new Location("Smartphone");
        locationB.setLatitude(Local.getLatitude());
        locationB.setLongitude(Local.getLongitude());

        return locationA.distanceTo(locationB);
    }
}
