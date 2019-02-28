package br.ufc.great.contextplayer.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.ufc.great.contextplayer.R;
import br.ufc.great.contextplayer.model.Playlist;
import smd.ufc.br.easycontext.CurrentContext;
import smd.ufc.br.easycontext.Snapshot;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectPlaylistFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectPlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectPlaylistFragment extends Fragment implements Snapshot.OnContextUpdate {
    private OnFragmentInteractionListener mListener;
    private Snapshot snapshot;
    private List<Playlist> playlists;

    public SelectPlaylistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SelectPlaylistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectPlaylistFragment newInstance() {
        SelectPlaylistFragment fragment = new SelectPlaylistFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        snapshot = Snapshot.getInstance(getContext());
        snapshot.setCallback(this);
        snapshot.updateContext(Snapshot.WEATHER, Snapshot.TIME_INTERVAL, Snapshot.DETECTED_ACTIVITY);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_playlist, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onContextUpdate(CurrentContext currentContext) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
