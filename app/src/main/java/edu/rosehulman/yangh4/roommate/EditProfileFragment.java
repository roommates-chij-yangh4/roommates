package edu.rosehulman.yangh4.roommate;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class EditProfileFragment extends Fragment {

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final DatabaseReference mMemberRef = FirebaseDatabase.getInstance().getReference().child("members");
        final View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        final TextView firstname = (TextView) view.findViewById(R.id.edit_first_name);
        final TextView lastname = (TextView) view.findViewById(R.id.edit_last_name);
        final TextView cellphone = (TextView) view.findViewById(R.id.edit_cellphone);
        final TextView email = (TextView) view.findViewById(R.id.edit_email);
        final TextView contactinfo = (TextView) view.findViewById(R.id.edit_description);
        mMemberRef.child(MainActivity.user.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final People user = dataSnapshot.getValue(People.class);
                Button button = (Button) view.findViewById(R.id.edit_button);
                firstname.setText(user.getFirst_name());
                lastname.setText(user.getLast_name());
                cellphone.setText(user.getPhone());
                email.setText(user.getEmail());
                contactinfo.setText(user.getExtracontactinfo());
                ((MainActivity) getActivity()).setActionBarTitle("Edit Profile");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.setFirst_name(firstname.getText().toString());
                        user.setLast_name(lastname.getText().toString());
                        user.setEmail(email.getText().toString());
                        user.setPhone(cellphone.getText().toString());
                        user.setExtracontactinfo(contactinfo.getText().toString());
                        ((TextView) MainActivity.mNavigationView.findViewById(R.id.user_name_text)).setText(user.getName());
                        ((TextView) MainActivity.mNavigationView.findViewById(R.id.user_email_text)).setText(user.getEmail());
                        Snackbar snackbar = Snackbar.make(view, "Changes Saved", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        mMemberRef.child(user.getKey()).setValue(user);
                        if (!user.getGroupkeys().keySet().isEmpty() && !user.getItemkeys().keySet().isEmpty()) {
                            for (String groupkey : user.getGroupkeys().keySet()) {
                                DatabaseReference temp = FirebaseDatabase.getInstance().getReference().child("groups/" + groupkey);
                                for (String itemkey : user.getItemkeys().keySet()) {
                                    temp.child("items/" + itemkey + "/Username").setValue(user.getName());
                                }
                            }
                        }
                        MainActivity.user = user;
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void showPasswordWarningDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("WARNING!");
        builder.setMessage("New Password DO NOT MATCH");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }

    private void showWrongPasswordWarningDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("WARNING!");
        builder.setMessage("Wrong Password");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}
