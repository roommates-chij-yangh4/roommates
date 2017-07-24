package edu.rosehulman.yangh4.roommate;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindPassword_Fragment extends Fragment {


    public FindPassword_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_password_page, container, false);
        TextView EmailInput = ((TextView) view.findViewById(R.id.email_input));
        Button sendPWButton = ((Button) view.findViewById(R.id.send_password_button));
        sendPWButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add something here to send an email.
            }
        });
        return view;
    }

}
