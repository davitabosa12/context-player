package br.ufc.great.contextplayer.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;

import br.ufc.great.contextplayer.EditarPlaylistActivity;
import br.ufc.great.contextplayer.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link} interface
 * to handle interaction events.
 * Use the {@link MainScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainScreenFragment extends Fragment implements View.OnLongClickListener, View.OnClickListener {


    private OnFragmentInteractionListener mListener;
    private Button btnTreino, btnChuvoso, btnNuvens, btnCarro, btnEnsolarado;
    private TableRow trRecomendacao;
    private static String TAG = "MainScreenFragment";

    public MainScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainScreenFragment newInstance() {
        MainScreenFragment fragment = new MainScreenFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_main_screen, container, false);
       //find buttons
       btnTreino = rootview.findViewById(R.id.btn_treino);
       btnCarro = rootview.findViewById(R.id.btn_no_carro);
       btnChuvoso = rootview.findViewById(R.id.btn_chuvoso);
       btnEnsolarado = rootview.findViewById(R.id.btn_ensolarado);
       btnNuvens = rootview.findViewById(R.id.btn_nuvens);
       trRecomendacao = rootview.findViewById(R.id.tr_playlist_recomendada);
       setUpButtonListeners();
       return rootview;
    }

    private void setUpButtonListeners() {
        btnTreino.setOnLongClickListener(this);
//        btnCarro.setOnLongClickListener(this);
//        btnChuvoso.setOnLongClickListener(this);
//        btnEnsolarado.setOnLongClickListener(this);
//        btnNuvens.setOnLongClickListener(this);
//
//        btnTreino.setOnClickListener(this);
//        btnCarro.setOnClickListener(this);
//        btnChuvoso.setOnClickListener(this);
//        btnEnsolarado.setOnClickListener(this);
//        btnNuvens.setOnClickListener(this);
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onLongClick(View view) {
        Intent i = new Intent(getContext(), EditarPlaylistActivity.class);
        switch(view.getId()){
            case R.id.btn_chuvoso:
                i.putExtra("playlist", "chuvoso");
                startActivity(i);
                break;
            case R.id.btn_ensolarado:
                i.putExtra("playlist", "ensolarado");
                startActivity(i);
                break;
            case R.id.btn_no_carro:
                i.putExtra("playlist", "no_carro");
                startActivity(i);
                break;
            case R.id.btn_nuvens:
                i.putExtra("playlist", "nuvens");
                startActivity(i);
                break;
            case R.id.btn_treino:
                i.putExtra("playlist", "treino");
                startActivity(i);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if(mListener == null){
            Log.e(TAG, "onClick: activity doesn't implement OnFragmentInteractionListener ", new NullPointerException());
        }
       mListener.onFragmentInteraction(this, view);

    }
}
