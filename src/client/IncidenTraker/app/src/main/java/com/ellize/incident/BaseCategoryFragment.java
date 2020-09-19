package com.ellize.incident;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class BaseCategoryFragment extends Fragment {


    private static final String ARG_TITLE = "title";
    private static final String ARG_TYPE = "type";


    private String mTitle;
    private int mType;

    public BaseCategoryFragment() {
        // Required empty public constructor
    }

    public static BaseCategoryFragment newInstance(String title, int type) {
        BaseCategoryFragment fragment = new BaseCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
            mType = getArguments().getInt(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_base_category, container, false);
        ((TextView)v.findViewById(R.id.tv_title)).setText(mTitle);
        return v;
    }
}