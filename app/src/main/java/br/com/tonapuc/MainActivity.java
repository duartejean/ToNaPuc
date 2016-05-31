package br.com.tonapuc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Evento do botão Buscar pelo Local
     *
     * @param view View
     */
    public void buscarPeloLocal(View view) {
        Intent intent = new Intent(MainActivity.this, LocalActivity.class);
        startActivity(intent);
    }

    /**
     * Evento do botão Buscar pelo endereço
     *
     * @param view - View
     */
    public void buscarPeloEndereco(View view) {
        Intent intent = new Intent(MainActivity.this, EnderecoActivity.class);
        startActivity(intent);
    }
}