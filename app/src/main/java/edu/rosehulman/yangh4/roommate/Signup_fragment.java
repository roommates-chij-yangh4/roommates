package edu.rosehulman.yangh4.roommate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Signup_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Signup_fragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Callback mCallback;

    public Signup_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_page, container, false);
        Button button = (Button) view.findViewById(R.id.signup_signup_page_button);
        final TextView firstname = (TextView) view.findViewById(R.id.first_name_input);
        final TextView lastname = (TextView) view.findViewById(R.id.last_name_input);
        final TextView email = (TextView) view.findViewById(R.id.email_input);
        final TextView password = (TextView) view.findViewById(R.id.password_input);
        final TextView passwordconfirm = (TextView) view.findViewById(R.id.confirm_password_input);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!password.getText().toString().equals(passwordconfirm.getText().toString())) {
                    showPasswordWarningDialog();
                    return;
                }
                mCallback.Signup(firstname.getText(), lastname.getText(), email.getText(), password.getText());
            }
        });
        return view;
    }

    private void showPasswordWarningDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("WARNING!");
        builder.setMessage("Password DO NOT MATCH");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Signup_fragment.Callback) {
            mCallback = (Signup_fragment.Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public interface Callback {
        void Signup(CharSequence FirstName, CharSequence LastName, CharSequence Email, CharSequence Password);
    }
}
