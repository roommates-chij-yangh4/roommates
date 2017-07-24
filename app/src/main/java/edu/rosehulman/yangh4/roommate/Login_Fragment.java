package edu.rosehulman.yangh4.roommate;


import android.content.Context;
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
public class Login_Fragment extends Fragment {


    private Callback mCallback;

    public Login_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_page, container, false);
        Button button = (Button) view.findViewById(R.id.login_loginpage_button);
        final TextView email = (TextView) view.findViewById(R.id.email_input);
        final TextView password = (TextView) view.findViewById(R.id.password_input);
        final TextView forgetpassword = (TextView) view.findViewById(R.id.forget_password);
        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.findpassword();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check the password with firebase here
                mCallback.Login(email.getText(), password.getText());
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        super.onAttach(context);
        if (context instanceof Login_Fragment.Callback) {
            mCallback = (Login_Fragment.Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface Callback {

        void Login(CharSequence email, CharSequence password);

        void findpassword();
    }

}
