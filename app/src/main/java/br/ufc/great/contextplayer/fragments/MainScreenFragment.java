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
import android.widget.TextView;

import br.ufc.great.contextplayer.EditarPlaylistActivity;
import br.ufc.great.contextplayer.R;
import smd.ufc.br.easycontext.CurrentContext;
import smd.ufc.br.easycontext.Snapshot;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link} interface
 * to handle interaction events.
 * Use the {@link MainScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainScreenFragment extends Fragment implements Snapshot.OnContextUpdate {


    private static String TAG = "MainScreenFragment";
    Snapshot snapshot;
    private TextView txvContext;

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
        snapshot = Snapshot.getInstance(getContext());
        snapshot.setCallback(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_main_screen, container, false);
       txvContext = rootview.findViewById(R.id.txv_context);
       snapshot.updateContext(Snapshot.ALL_PROVIDERS);
       return rootview;
    }


    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {

        super.onInflate(context, attrs, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onContextUpdate(CurrentContext currentContext) {
        txvContext.setText(currentContext.toString());
    }
}
