package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;

public class clsMensaje {
    private Context loContext;

    public clsMensaje(Context toContext)
    {
        loContext = toContext;
    }

    public void gMostrarMensajeOk( ViewGroup viewGroup)
    {
        try{
            //ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(loContext).inflate(R.layout.dialogo_ok, viewGroup, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(loContext, R.style.myDialog));
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button btnOk = dialogView.findViewById(R.id.buttonOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }catch (Exception ex)
        {
            Toast.makeText(loContext, "No se pudo mostrar el Dialogo. Error en el metodo: gMostrarMensajeOk() "+ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void gMostrarMensaheAviso()
    {

    }

    public void gMostrarMensajeError()
    {

    }
}
