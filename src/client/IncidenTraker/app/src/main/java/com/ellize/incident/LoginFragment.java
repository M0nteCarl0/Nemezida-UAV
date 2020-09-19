package com.ellize.incident;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.ellize.incident.databinding.LoginFragmentBinding;


public class LoginFragment extends Fragment {

    private MainViewModel mViewModel;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("fragment","login fragment");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        LoginFragmentBinding binding = DataBindingUtil.inflate(inflater,R.layout.login_fragment,container,false);
        binding.setViewmodel(mViewModel);
        final TextView tv_error = binding.tvErrorAuth;
        mViewModel.isEmailPswError.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) tv_error.setVisibility(View.INVISIBLE);
                else {
                    tv_error.setText(R.string.wrong_login_psw);
                    tv_error.setVisibility(View.VISIBLE);
                }
            }
        });
        mViewModel.isConnectionError.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) tv_error.setVisibility(View.INVISIBLE);
                else {
                    tv_error.setText(R.string.wrong_no_connection);
                    tv_error.setVisibility(View.VISIBLE);
                }
            }
        });
        mViewModel.isNoInternet.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) tv_error.setVisibility(View.INVISIBLE);
                else {
                    tv_error.setText(R.string.wrong_need_internet);
                    tv_error.setVisibility(View.VISIBLE);
                }
            }
        });

        TextView tv_link = binding.tvForgetpsw;
        tv_link.setMovementMethod(LinkMovementMethod.getInstance());
       // binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        //getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS|WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(bar!=null) bar.hide();
        super.onResume();
    }

    @Override
    public void onPause() {

        super.onPause();
        //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS|WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
