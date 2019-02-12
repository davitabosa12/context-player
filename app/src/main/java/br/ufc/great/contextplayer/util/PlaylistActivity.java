package br.ufc.great.contextplayer.util;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.ufc.great.contextplayer.R;

public class PlaylistActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnNuvens, btnEnsolarado, btnChuvoso, btnTreino, btnNoCarro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        setTitle("Editar playlists");

        //pegar botoes
        btnNuvens = findViewById(R.id.btn_nuvens);
        btnEnsolarado = findViewById(R.id.btn_ensolarado);
        btnChuvoso = findViewById(R.id.btn_chuvoso);
        btnTreino= findViewById(R.id.btn_treino);
        btnNoCarro= findViewById(R.id.btn_no_carro);
    }

    @Override
    public void onClick(View view) {
        Intent editarPlaylist = new Intent(this, )
        switch (view.getId()){
            case R.id.btn_nuvens:
                break;
            case R.id.btn_ensolarado:
                break;
            case R.id.btn_chuvoso:
                break;
            case R.id.btn_treino:
                break;
            case R.id.btn_no_carro:
                break;
            default:
                return;
        }

    }
}
