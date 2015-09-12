package com.tripad.tetanggaku.slidingtab.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tripad.tetanggaku.R;

/**
 * Created by Edwin on 15/02/2015.
 */
public class TabCentral extends Fragment {

    static boolean isdebug = true;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_central,container,false);
        return v;
    }

}