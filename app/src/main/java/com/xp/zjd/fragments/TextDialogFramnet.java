package com.xp.zjd.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xp.R;
import com.xp.common.AndroidTool;
import com.xp.zjd.photo.PhotosFragment;

import java.util.List;

public class TextDialogFramnet extends DialogFragment {

    int mNum;

    static TextDialogFramnet newInstance(int num)
    {
        TextDialogFramnet textdia=new TextDialogFramnet();
        Bundle bundel=new Bundle();
        bundel.putInt("name", num);
        textdia.setArguments(bundel);
        return textdia;
    }
    public void onCreate(Bundle saveInstanced)
    {
        super.onCreate(saveInstanced);
        mNum=getArguments().getInt("name");
        int style=DialogFragment.STYLE_NO_TITLE,theme=0;
        switch((mNum-1)%6)
        {
            case 1:
                style=DialogFragment.STYLE_NO_TITLE;
                break;
            case 2:
                style=DialogFragment.STYLE_NO_FRAME;
            case 3:
                style = DialogFragment.STYLE_NO_INPUT;
                break;
            case 4:
                style = DialogFragment.STYLE_NORMAL;
                break;
            case 5:
                style = DialogFragment.STYLE_NORMAL;
                break;
            case 6:
                style = DialogFragment.STYLE_NO_TITLE;
                break;
            case 7:
                style = DialogFragment.STYLE_NO_FRAME;
                break;
            case 8:
                style = DialogFragment.STYLE_NORMAL;
                break;
        }
        switch((mNum-1)%6)
        {
            case 4:
                theme=android.R.style.Theme_Holo; break;
            case 5:
                theme=android.R.style.Theme_Holo_Light_Dialog;
                break;
            case 6: theme = android.R.style.Theme_Holo_Light; break;
            case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
            case 8: theme = android.R.style.Theme_Holo_Light; break;
        }
        setStyle(style,theme);
    }
    public View onCreateView(LayoutInflater inflater,ViewGroup contaniner,Bundle savedInstance)
    {
        View v=inflater.inflate(R.layout.fragment_test, contaniner,false);


        PhotosFragment photosFragment = new PhotosFragment();
        List<Fragment> list =  getFragmentManager().getFragments();

        Fragment fr = list.get(1);
        FragmentManager fragmentManager =fr.getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.layer1111, photosFragment);

        transaction.addToBackStack(null);
        transaction.commit();

        //AndroidTool.replaceFrameLayout(R.id.layer1111,photosFragment);
        return v;

    }
}
