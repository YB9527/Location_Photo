package com.xp.zjd.init;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xp.R;


/**
 * Created by Administrator on 2020/3/31.
 */

public class InitFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_init, container, false);

        init();
        return view;
    }


    private void init() {

    }

}
