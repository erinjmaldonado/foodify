package com.example.foodify.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;

import com.example.foodify.MainActivity;
import com.example.foodify.R;
import com.firebase.ui.auth.AuthUI;

public class ProfileFragment extends Fragment{
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button signOut = (Button) view.findViewById(R.id.bt_logout);
        signOut.setOnClickListener(v->{
            signOut();
        });
        return view;
    }

    public void signOut(){

        AuthUI.getInstance()
                .signOut(getActivity())
                .addOnCompleteListener(task -> {
                    // do something here
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                });
    }
}
