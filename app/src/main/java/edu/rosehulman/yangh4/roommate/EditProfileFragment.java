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
import android.widget.ImageView;
import android.widget.TextView;


public class EditProfileFragment extends Fragment {

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final User user = MainActivity.user;
        final View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        final TextView firstname = (TextView) view.findViewById(R.id.edit_first_name);
        final TextView lastname = (TextView) view.findViewById(R.id.edit_last_name);
        final TextView cellphone = (TextView) view.findViewById(R.id.edit_cellphone);
        final TextView email = (TextView) view.findViewById(R.id.edit_email);
        final TextView contactinfo = (TextView) view.findViewById(R.id.edit_description);
        final TextView pw = (TextView) view.findViewById(R.id.old_password);
        final TextView newpw = (TextView) view.findViewById(R.id.edit_password);
        final TextView newpwc = (TextView) view.findViewById(R.id.edit_password_confirm);
        final ImageView img = (ImageView) view.findViewById(R.id.edit_photo);
        Button button = (Button) view.findViewById(R.id.edit_button);
        firstname.setText(user.getFirst_name());
        lastname.setText(user.getLast_name());
        cellphone.setText(user.getPhone());
        email.setText(user.getEmail());
        contactinfo.setText(user.getExtracontactinfo());
        img.setImageBitmap(user.getPhoto());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user.getPassword().equals(pw.getText().toString())) {
                    showWrongPasswordWarningDialog();
                    return;
                }
                if (!newpw.getText().toString().equals(newpwc.getText().toString())) {
                    showPasswordWarningDialog();
                    return;
                }
                user.setFirst_name(firstname.getText().toString());
                user.setLast_name(lastname.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPhone(cellphone.getText().toString());
                user.setPhoto(drawableToBitmap(img.getDrawable()));
                user.setExtracontactinfo(contactinfo.getText().toString());
                if (newpw.getText().toString().length() != 0) {
                    user.setPassword(newpw.getText().toString());
                }
                Snackbar snackbar = Snackbar.make(view, "Changes Saved", Snackbar.LENGTH_SHORT);
                snackbar.show();
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
