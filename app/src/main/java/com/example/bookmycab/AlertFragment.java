package com.example.bookmycab;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AlertFragment extends Fragment {

    EditText mobileno,message;
    Button sendsms;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void sendsms(View v)
    {

        //refernce the edittext
        mobileno=(EditText) v.findViewById(R.id.editphone);
        message=(EditText) v.findViewById(R.id.edittaxinumber);

        message.setText("Hii", TextView.BufferType.valueOf(message.getText().toString()));
        //get the phone the number and the message

        String number=mobileno.getText().toString();
        String msg=message.getText().toString();
        //use the sms manager to send message
        SmsManager sm=SmsManager.getDefault();
        sm.sendTextMessage(number, null, msg, null, null);
        Toast.makeText(getActivity(),"Messege sent",Toast.LENGTH_LONG).show();


    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        mobileno = getActivity().findViewById(R.id.editphone);
//        message = getActivity().findViewById(R.id.edittaxinumber);
//        sendsms = (Button) getActivity().findViewById(R.id.button);
//
//        sendsms.setOnClickListener(v -> {
//            String no=mobileno.getText().toString();
//            String msg=message.getText().toString();
//
//            Intent intent = new Intent(getContext(), AlertFragment.class);
//            PendingIntent pi=PendingIntent.getActivity(getContext(), 0, intent,0);
//
//            SmsManager sms=SmsManager.getDefault();
//            sms.sendTextMessage(no, null, msg, pi,null);
//
//            Toast.makeText(getContext(), "Message Sent successfully!",
//                    Toast.LENGTH_LONG).show();
//        });
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_alert, container, false);
        return v;
    }


}