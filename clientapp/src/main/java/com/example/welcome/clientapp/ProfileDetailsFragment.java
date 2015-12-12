package com.example.welcome.clientapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.neovisionaries.ws.client.WebSocketException;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by welcome on 11/28/2015.
 */
public class ProfileDetailsFragment extends Fragment {

    private CustomerDetails mCnt;
    private SocketsApp mSockets;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCnt = CustomerDetails.getInstance();
        try {
            mSockets = SocketsApp.getInstance(getActivity());
        } catch (IOException | WebSocketException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_profile, container, false);

        final EditText etName = (EditText) v.findViewById(R.id.editText);
        final EditText etPhone = (EditText) v.findViewById(R.id.editText2);
        final EditText etGuardian = (EditText) v.findViewById(R.id.editText4);
        Spinner mySpinner = (Spinner) v.findViewById(R.id.spinner);


        etName.setText(mCnt.getCuName());
        etGuardian.setText(mCnt.getGuardian());
        etPhone.setText(mCnt.getPhoneNumber());

        mySpinner.setSelection(((ArrayAdapter) mySpinner.getAdapter()).getPosition(mCnt.getPickup()));
        final Button btEdit = (Button) v.findViewById(R.id.button);

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Yet to be done, call us for any changes", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}
