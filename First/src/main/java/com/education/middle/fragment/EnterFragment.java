package com.education.middle.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.education.middle.R;

public class EnterFragment extends Fragment {
    private OnEnterListener listener;

    private AppCompatEditText edit;
    private AppCompatButton bOk;

    public EnterFragment() {
    }

    public static EnterFragment newInstance() {
        EnterFragment fragment = new EnterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enter, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle data){
        String url = "https://arm-software.github.io/opengl-es-sdk-for-android/proceduralGeometry-title.png";
        edit = view.findViewById(R.id.edURI);
        edit.setText(url);
        bOk = view.findViewById(R.id.bOk);
        bOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) listener.onOk(edit.getText().toString());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEnterListener) {
            listener = (OnEnterListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnEnterListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnEnterListener {
        void onOk(String url);
    }
}
