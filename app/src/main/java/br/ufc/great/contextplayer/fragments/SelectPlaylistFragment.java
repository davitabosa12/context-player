package br.ufc.great.contextplayer.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.ufc.great.contextplayer.R;
import br.ufc.great.contextplayer.model.Playlist;
import br.ufc.great.contextplayer.model.PlaylistConfidence;
import br.ufc.great.contextplayer.model.PlaylistContexts;
import br.ufc.great.contextplayer.model.PlaylistDAO;
import br.ufc.great.contextplayer.views.PlaylistBigCard;
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
public class SelectPlaylistFragment extends Fragment implements Snapshot.OnContextUpdate, View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    private Snapshot snapshot;
    private List<Playlist> playlists;
    private List<PlaylistConfidence> playlistConfidences;
    private ScrollView mainLayout;
    private PlaylistBigCard mMostRecommended, mSecondRecommended, mThirdRecommended;
    private LinearLayout mFourthOnwards, mPreloader;
    private ConstraintLayout mNoPlaylists;

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

        //get list of playlists
        PlaylistDAO playlistDAO = new PlaylistDAO(getContext());

        //TODO: this should be threaded
        playlists = playlistDAO.getAllPlaylists();
        playlistConfidences = new ArrayList<>();


        snapshot = Snapshot.getInstance(getContext());
        snapshot.setCallback(this);
        snapshot.updateContext(Snapshot.WEATHER, Snapshot.TIME_INTERVAL, Snapshot.DETECTED_ACTIVITY);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_playlist_recommendation, container, false);
        mMostRecommended = rootView.findViewById(R.id.card_first);
        mSecondRecommended = rootView.findViewById(R.id.card_second);
        mThirdRecommended = rootView.findViewById(R.id.card_third);
        mFourthOnwards = rootView.findViewById(R.id.ll_dynamic);
        mainLayout = rootView.findViewById(R.id.scroll_view);
        mNoPlaylists = rootView.findViewById(R.id.layout_no_playlists);
        mPreloader = rootView.findViewById(R.id.ll_preload);
        mainLayout.setVisibility(View.GONE);
        mNoPlaylists.setVisibility(View.GONE);
        mPreloader.setVisibility(View.VISIBLE);


        //onclick listeners
        mMostRecommended.setOnClickListener(this);
        mSecondRecommended.setOnClickListener(this);
        mThirdRecommended.setOnClickListener(this);


        return rootView;
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
        //get playlist confidence
        for(Playlist playlist : playlists){
            PlaylistContexts contexts = playlist.getDefinitions();
            float confidence = contexts.calculateConfidence(currentContext);
            playlistConfidences.add(new PlaylistConfidence(playlist, confidence));
        }
        //sort it descending (reverse order)
        Collections.sort(playlistConfidences, Collections.<PlaylistConfidence>reverseOrder());
        playlists.clear();
        for(PlaylistConfidence confidence : playlistConfidences){
            playlists.add(confidence.getPlaylist());
        }
        updateUI();
    }

    private void updateUI() {
        if(playlists.isEmpty()){
            //no playlists on this device
            mainLayout.setVisibility(View.GONE);
            mNoPlaylists.setVisibility(View.VISIBLE);
        } else {
            mNoPlaylists.setVisibility(View.GONE);
            mPreloader.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);

            //set most recommended
            mMostRecommended.setPlaylist(playlists.get(0));
            mMostRecommended.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            //always check if there is more than 3 playlists, to recommend second and third places.
            if(playlists.size() >= 2){

                mSecondRecommended.setPlaylist(playlistConfidences.get(1).getPlaylist());
            }
            if(playlists.size() >= 3){

                mThirdRecommended.setPlaylist(playlistConfidences.get(2).getPlaylist());
            }
            if(playlists.size() >= 4){
                for(int i = 3; i < playlists.size(); i++){
                    Button b = new Button(getContext());
                    Playlist p = playlists.get(i);
                    b.setLayoutParams(
                            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                    b.setText(p.getName());
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //play this playlist
                        }
                    });
                }
            }

        }


    }

    @Override
    public void onClick(View view) {
        if(playlists.isEmpty()) return;

        //TODO: Broadcasts to main activity, to play the playlist
        //maybe pass an intent with the playlistId to play?
        switch (view.getId()){
            case R.id.card_first:
                Toast.makeText(getContext(), "Playing " + playlists.get(0).getName(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.card_second:
                if(playlists.size() < 2) break;
                Toast.makeText(getContext(), "Playing " + playlists.get(1).getName(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.card_third:
                if(playlists.size() < 3) break;
                Toast.makeText(getContext(), "Playing " + playlists.get(2).getName(), Toast.LENGTH_SHORT).show();
                break;
        }
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
