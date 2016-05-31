package br.com.tonapuc;

import android.app.Activity;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Criado por Jean em 25/05/2016.
 */
public class EnderecoActivity extends Activity {
    // Constantes com as coordenadas a PUC Minas em Betim
    private static final double latitudePUC = -19.955055;
    private static final double longitudePUC = -44.198153;
    private GoogleMap mMap;
    private LinearLayout layout;
    private double latitude, longitude;
    private TextView lblLatitude, lblLongitude, lblDistancia, lblStatus;
    private EditText editEndereco;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.endereco_layout);

        // Busca os elementos da página
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        lblLatitude = (TextView) findViewById(R.id.lbl_latitude);
        lblLongitude = (TextView) findViewById(R.id.lbl_longitude);
        lblStatus = (TextView) findViewById(R.id.lbl_status);
        lblDistancia = (TextView) findViewById(R.id.lbl_distancia);
        editEndereco = (EditText) findViewById(R.id.edit_endereco);
        layout = (LinearLayout) findViewById(R.id.layout_cabecalho);

        layout.setVisibility(View.GONE);
    }

    /**
     * Método para buscar a localização do dispositivo baseado no endereço informado
     */
    public void MeuLocal() {
        try {
            if (editEndereco.getText().toString().equals("")) {
                Toast.makeText(this, "Endereço não pode ser vazio, informe um endereço", Toast.LENGTH_SHORT).show();
                return;
            }
            Geocoder coder = new Geocoder(this);
            Address local;
            List<Address> enderecos = coder.getFromLocationName(editEndereco.getText().toString(), 5);

            if (enderecos == null) {
                Toast.makeText(this, "Endereço não encontrado, tente novamente", Toast.LENGTH_SHORT).show();
                return;
            } else {
                local = enderecos.get(0);
            }

            latitude = local.getLatitude();
            longitude = local.getLongitude();
            layout.setVisibility(View.VISIBLE);

            // Escreve as coordenadas na tela
            escreveCoordenadas(local.getLatitude(), local.getLongitude());

            // Monta os pontos no mapa
            desenhaPonto(local.getLatitude(), local.getLongitude());

        } catch (Exception e) {
            Toast.makeText(this, "Não foi possível localizar o endereço, tente novamente", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método para escrever as coordenadas e a distância na tela
     *
     * @param latitude  Latitude do local
     * @param longitude Longitude do local
     */
    public void escreveCoordenadas(double latitude, double longitude) {
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
     * Evento ao clicar no botão para localizar o endereço
     *
     * @param view Objeto
     */
    public void Localizar(View view) {
        try {
            MeuLocal();
        } catch (Exception e) {
            Toast.makeText(this, "Ocorreu um erro, tente novamente", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Evento ao clicar no botão para limpar a tela e esconder os dados
     *
     * @param view Objeto
     */
    public void LimparEndereco(View view) {
        editEndereco.setText("");
        layout.setVisibility(View.GONE);
    }

    /**
     * Método para criar os marcadores no mapa, na localização atual e na PUC
     *
     * @param latitude  Latitude do ponto informado no campo endereço
     * @param longitude Longitude do ponto informado no campo endereço
     */
    private void desenhaPonto(double latitude, double longitude) {
        mMap.clear();

        // Converte a localização atual e da PUC em um objeto para ser usado na API de mapas
        LatLng localAtual = new LatLng(latitude, longitude);
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
                .snippet(String.format("%s,%s", latitude, longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .position(localAtual));
    }
}
