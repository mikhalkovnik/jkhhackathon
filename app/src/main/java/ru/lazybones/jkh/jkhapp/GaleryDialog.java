package ru.lazybones.jkh.jkhapp;

import android.content.DialogInterface;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;

import com.squareup.picasso.Picasso;

public class GaleryDialog extends DialogFragment implements OnClickListener {

    final String LOG_TAG = "myLogs";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Title!");
        View v = inflater.inflate(R.layout.galerydialog, null);
        ImageView btndelete = v.findViewById(R.id.btndel);

            btndelete.setVisibility(View.VISIBLE);
            btndelete.setOnClickListener(this);



        v.findViewById(R.id.btncancel).setOnClickListener(this);
        ImageView pic = (ImageView) v.findViewById(R.id.picbig);
        Picasso.get().load(Current.url)
                .error(R.drawable.ic_menu_camera)
                .placeholder(R.drawable.ic_menu_camera)
                .resize(600, 600)
                .centerCrop()
                .into(pic);

        return v;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btncancel:
                // кнопка Cancel
                dismiss();
                break;
            case R.id.btndel:
                // кнопка delete
                AddneworderActivity.deletegalery();


                break;
        }
        Log.d(LOG_TAG, "Dialog 1: " + v.getId());
        dismiss();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog 1: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog 1: onCancel");
    }
}
