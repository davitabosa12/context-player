package br.ufc.great.contextplayer.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import br.ufc.great.contextplayer.model.PlaylistDAO;

public class CreatePlaylistDialog extends DialogFragment {

    String name, description;

    public CreatePlaylistDialog(){

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final EditText edtName = new EditText(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        edtName.setLayoutParams(lp);
        builder.setTitle("Create Playlist");
        builder.setView(edtName);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PlaylistDAO dao = new PlaylistDAO(getContext());
                name = edtName.getText().toString();
                if(dao.createBlankPlaylist(name) < 0){
                    Toast.makeText(getContext(), "Failed creating playlist", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getContext(), "Playlist created successfully", Toast.LENGTH_SHORT).show();
                }

            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        });

        return builder.create();
    }

    public static CreatePlaylistDialog newInstance() {
        CreatePlaylistDialog fragment = new CreatePlaylistDialog();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
