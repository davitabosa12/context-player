package br.ufc.great.contextplayer.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.ufc.great.contextplayer.MusicScanner;
import br.ufc.great.contextplayer.R;
import br.ufc.great.contextplayer.model.Song;
import br.ufc.great.contextplayer.views.SongViewRecyclerAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MusicListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicListFragment extends Fragment {

    private MusicScanner scanner;
    private RecyclerView mRecyclerView;
    private SongViewRecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<Song> songs;


    private OnFragmentInteractionListener mListener;


    public MusicListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MusicListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicListFragment newInstance() {
        MusicListFragment fragment = new MusicListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context ctx = container.getContext();
        // Inflate the layout for this fragment
        songs = (ArrayList<Song>) new MusicScanner(ctx).scan();

        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_music_list, container, false);
        //configura recycler
        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(ctx);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SongViewRecyclerAdapter(songs);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

        return rootview;
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
}
