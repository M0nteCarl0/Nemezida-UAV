package com.ellize.incident;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriesFragment extends Fragment {
/**
 *  вкладка на главной странице - Инциденты разлива нефти.
 * 2 вкладка на главной странице - Инциденты вскрытия грунта
 * 3 вкладка на главной странице - Инциденты складирования материалов
 * 4 вкладка на главной странице - Работа спец. техники
 */


    public CategoriesFragment() {
        // Required empty public constructor
    }


    public static CategoriesFragment newInstance() {
        CategoriesFragment fragment = new CategoriesFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(bar!=null) bar.show();
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_categories, container, false);
        LinearLayout ll_catsContainer = view.findViewById(R.id.ll_actegories_container);
        String[] cats = getResources().getStringArray(R.array.categories);
        final MainViewModel mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        for(int i = 0 ;i < cats.length; ++i){
            View item = getLayoutInflater().inflate(R.layout.category_item,view,false);
            Button btn = item.findViewById(R.id.btn_category);
            final int finalI = i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewModel.setCurrentFragment(finalI);
                }
            });
            btn.setText(cats[i]);
            ll_catsContainer.addView(item);
        }
        return view;
    }
}