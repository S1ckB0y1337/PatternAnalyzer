//Nikolaos Katsiopis
//icsd13076
package com.buftas.patternanalyzer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

//This class provides an easy way to display alert dialogs to inform the user for various tasks
public class InfoDialog extends AppCompatDialogFragment {

    private String title, message;
    //Overriding the onCreateDialog to build our custom dialog with the provided message and title
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     //Do Nothing
                    }
                });
        return builder.create();
    }

    //Set Methods
    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
