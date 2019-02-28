package br.ufc.great.contextplayer;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import br.ufc.great.contextplayer.fragments.MainScreenFragment;
import br.ufc.great.contextplayer.fragments.MusicListFragment;
import br.ufc.great.contextplayer.fragments.SelectPlaylistFragment;

public class MainPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
        Fragment musicList = MusicListFragment.newInstance();
        Fragment mainScreen = MainScreenFragment.newInstance();
        Fragment playlistSelect = SelectPlaylistFragment.newInstance();
        mFragments.add(musicList);
        mFragments.add(mainScreen);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "Músicas";
            case 1:
                return "Playlists";
            default:
                return "";
        }
    }

    @Override
    public Fragment getItem(int i) {

        return mFragments.get(i);

    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
