package br.ufc.great.contextplayer.util;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.ufc.great.contextplayer.EditarPlaylistActivity;
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
        Intent editarPlaylist = new Intent(this, EditarPlaylistActivity.class);
        switch (view.getId()){
            case R.id.btn_nuvens:
                editarPlaylist.putExtra("playlist", "nuvens");
                startActivity(editarPlaylist);
                break;
            case R.id.btn_ensolarado:
                editarPlaylist.putExtra("playlist", "nuvens");
                startActivity(editarPlaylist);
                break;
            case R.id.btn_chuvoso:
                editarPlaylist.putExtra("playlist", "nuvens");
                startActivity(editarPlaylist);
                break;
            case R.id.btn_treino:
                editarPlaylist.putExtra("playlist", "nuvens");
                startActivity(editarPlaylist);
                break;
            case R.id.btn_no_carro:
                editarPlaylist.putExtra("playlist", "nuvens");
                startActivity(editarPlaylist);
                break;
            default:
                return;
        }

    }
}
