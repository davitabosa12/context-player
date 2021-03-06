package br.ufc.great.contextplayer.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import br.ufc.great.contextplayer.EditarPlaylistActivity;
import br.ufc.great.contextplayer.Main2Activity;
import br.ufc.great.contextplayer.R;
import br.ufc.great.contextplayer.model.Playlist;
import br.ufc.great.contextplayer.model.PlaylistConfidence;
import br.ufc.great.contextplayer.model.PlaylistContexts;
import br.ufc.great.contextplayer.model.PlaylistDAO;
import br.ufc.great.contextplayer.views.PlaylistBigCard;
import smd.ufc.br.easycontext.CurrentContext;
import smd.ufc.br.easycontext.Snapshot;

import static br.ufc.great.contextplayer.Main2Activity.PLAY_ACTION;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SelectPlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectPlaylistFragment extends Fragment implements Snapshot.OnContextUpdate, View.OnClickListener, View.OnLongClickListener {

    public static final String ACTION_UPDATE = "update_recalculate";
    private static final String  TAG = "SelectPlaylistFragment";

    private OnFragmentInteractionListener mListener;
    private Snapshot snapshot;
    private List<Playlist> playlists;
    private List<PlaylistConfidence> playlistConfidences;
    private ScrollView mainLayout;
    private PlaylistBigCard mMostRecommended, mSecondRecommended, mThirdRecommended;
    private LinearLayout mFourthOnwards, mPreloader;
    private ConstraintLayout mNoPlaylists;
    private CurrentContext mLastContext;

    private BroadcastReceiver updateRecalculate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            recalculateAndUpdate();
        }
    };

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
        //get list of playlists
        PlaylistDAO playlistDAO = new PlaylistDAO(getContext());

        //TODO: this should be threaded
        playlists = playlistDAO.getAllPlaylists();
        playlistConfidences = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




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

        mMostRecommended.setOnLongClickListener(this);
        mSecondRecommended.setOnLongClickListener(this);
        mThirdRecommended.setOnLongClickListener(this);

        snapshot = Snapshot.getInstance(getContext());
        snapshot.setCallback(this);
        snapshot.updateContext(Snapshot.WEATHER, Snapshot.TIME_INTERVAL, Snapshot.DETECTED_ACTIVITY);
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.getPackageName()
                    + " must implement OnFragmentInteractionListener");
        }

        getContext().registerReceiver(updateRecalculate, new IntentFilter(ACTION_UPDATE));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        getContext().unregisterReceiver(updateRecalculate);
    }

    @Override
    public void onContextUpdate(CurrentContext currentContext) {
        mLastContext = currentContext;
        recalculateAndUpdate();
    }

    private void recalculateAndUpdate(){
        playlistConfidences.clear();
        playlists = new PlaylistDAO(getContext()).getAllPlaylists();
        for(Playlist playlist : playlists){
            PlaylistContexts contexts = playlist.getDefinitions();
            float confidence;
            if (contexts == null) {
                confidence = 0.0f;
            }else{
                confidence = contexts.calculateConfidence(mLastContext);
            }
            Log.d(TAG, "recalculateAndUpdate: playlist " + playlist.getName() + " with confidence = " + confidence);
            playlistConfidences.add(new PlaylistConfidence(playlist, confidence));
        }
        //sort it descending (reverse order)
        Collections.sort(playlistConfidences, Collections.<PlaylistConfidence>reverseOrder());
        playlists = new ArrayList<>();
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
            }else {
                mSecondRecommended.setVisibility(View.GONE);
            }
            if(playlists.size() >= 3){

                mThirdRecommended.setPlaylist(playlistConfidences.get(2).getPlaylist());
            } else {
                mThirdRecommended.setVisibility(View.GONE);
            }
            if(playlists.size() >= 4){
                Log.d(TAG, "updateUI: playlist size: " + playlists.size());
                mFourthOnwards.removeAllViews();
                for(int i = 3; i < playlists.size(); i++){
                    Button b = new Button(getContext());
                    final Playlist p = playlists.get(i);
                    b.setLayoutParams(
                            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                    b.setText(p.getName());
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //play this playlist
                            Intent i = new Intent(PLAY_ACTION);
                            i.putExtra("playlist_id", (long) p.getId());
                            getContext().sendBroadcast(i);
                            Toast.makeText(getContext(),"Playing " + p.getName(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    b.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            Toast.makeText(getContext(),"Editing " + p.getName(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), EditarPlaylistActivity.class);
                            intent.putExtra("playlist_id", p.getId());
                            startActivity(intent);
                            return true;
                        }
                    });
                    mFourthOnwards.addView(b);
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
                {
                    Intent i = new Intent(PLAY_ACTION);
                    i.putExtra("playlist_id", (long) playlists.get(0).getId());
                    getContext().sendBroadcast(i);
                    Toast.makeText(getContext(), "Playing " + playlists.get(0).getName(), Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.card_second:
            {
                if(playlists.size() < 2) break;
                Intent i = new Intent(PLAY_ACTION);
                i.putExtra("playlist_id", (long) playlists.get(1).getId());
                getContext().sendBroadcast(i);
                Toast.makeText(getContext(), "Playing " + playlists.get(1).getName(), Toast.LENGTH_SHORT).show();
                break;

            }
            case R.id.card_third:
            {
                if(playlists.size() < 3) break;
                Intent i = new Intent(PLAY_ACTION);
                i.putExtra("playlist_id", (long) playlists.get(2).getId());
                getContext().sendBroadcast(i);
                Toast.makeText(getContext(), "Playing " + playlists.get(2).getName(), Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @Override
    public boolean onLongClick(View view) {
        Intent i = new Intent(getContext(), EditarPlaylistActivity.class);
        switch (view.getId()){
            case R.id.card_first:
                Toast.makeText(getContext(), "Editing " + playlists.get(0).getName(), Toast.LENGTH_SHORT).show();
                i.putExtra("playlist_id", playlists.get(0).getId());
                break;
            case R.id.card_second:
                if(playlists.size() < 2) break;
                Toast.makeText(getContext(), "Editing " + playlists.get(1).getName(), Toast.LENGTH_SHORT).show();

                i.putExtra("playlist_id", playlists.get(1).getId());
                break;
            case R.id.card_third:
                if(playlists.size() < 3) break;
                Toast.makeText(getContext(), "Editing " + playlists.get(2).getName(), Toast.LENGTH_SHORT).show();
                i.putExtra("playlist_id", playlists.get(2).getId());

                break;
        }
        startActivity(i);
        return true;
    }

}
