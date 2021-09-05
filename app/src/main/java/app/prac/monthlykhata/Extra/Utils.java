package app.prac.monthlykhata.Extra;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.snackbar.Snackbar;

import app.prac.monthlykhata.R;


public class Utils {


    public static Snackbar snackBarText(Context context, String message) {
        Activity activity = (Activity) context;
        View layout;
        Snackbar snackbar = Snackbar
                .make(activity.findViewById(android.R.id.content), message, 3000);
        layout = snackbar.getView();
        //setting background color
        layout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        TextView text = (TextView) layout.findViewById(com.google.android.material.R.id.snackbar_text);
        //setting font color
        text.setTextColor(context.getResources().getColor(R.color.white));
        Typeface font = null;
        //Setting font
        font =  ResourcesCompat.getFont(context, R.font.pacifico);
        text.setTypeface(font);
        return snackbar;

    }


}
