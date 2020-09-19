package com.ellize.incident;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class InfoDialog extends DialogFragment {
    public static InfoDialog newInstance(User user){
        Bundle bundle = new Bundle();
        InfoDialog infoDialog = new InfoDialog();
        bundle.putString("fio",user.fio);
        bundle.putString("inn",user.company);
        infoDialog.setArguments(bundle);
        return infoDialog;
    }
    public interface DialogInfoListener{
        public void onDialogExitClicked();
    }
    DialogInfoListener mListener;
    boolean status = true;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.info_dialog,container,false);
        final TextView tv_name = v.findViewById(R.id.tv_name);
        TextView tv_info = v.findViewById(R.id.tv_user_info);
        TextView btn_exit = v.findViewById(R.id.tv_btn_exit);
        TextView btn_cancel = v.findViewById(R.id.tv_btn_cancel);
        Bundle args = getArguments();
        if(args==null){
            dismiss();
        }
        tv_info.setText(args.getString("inn"));
        tv_name.setText(args.getString("fio"));
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status){
                    tv_name.setText(R.string.exit_confirm);
                    status = false;
                } else {
                    mListener.onDialogExitClicked();
                    dismiss();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof DialogInfoListener){
            mListener = (DialogInfoListener) context;
        } else {
            throw new RuntimeException("must implement dialog listener");
        }
    }
}
