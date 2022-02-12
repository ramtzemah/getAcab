package com.example.getacab;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

public class Fragment_List extends Fragment {

    private RecyclerView main_LST_drives;
    private AppCompatActivity activity;
//    private CallBack_List callBackList;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

//    public void setCallBackList(CallBack_List callBackList) {
//        this.callBackList = callBackList;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        main_LST_drives = view.findViewById(R.id.main_LST_drives);
        return view;

    }

    public RecyclerView getMain_LST_drives() {
        if(main_LST_drives == null){
            return null;
        }else {
            return main_LST_drives;
        }
    }

}