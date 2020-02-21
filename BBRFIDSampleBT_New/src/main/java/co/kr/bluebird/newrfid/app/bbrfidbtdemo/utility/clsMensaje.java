package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;

public class clsMensaje {
    private Context loContext;

    public clsMensaje(Context toContext)
    {
        loContext = toContext;
    }

    /**
     *
     * @param viewGroup: Vista de Contenido
     * @param toActividadDestino: Intent para definir si se desea cambiar a otra actividad. null sino desea definir un Intent.
     */
    public void gMostrarMensajeOk( ViewGroup viewGroup, Intent toActividadDestino)
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
                    if(toActividadDestino !=null)
                    {
                        loContext.startActivity(toActividadDestino);
                    }
                }
            });
            alertDialog.show();
        }catch (Exception ex)
        {
            Toast.makeText(loContext, "No se pudo mostrar el Dialogo. Error en el metodo: gMostrarMensajeOk() "+ex.toString(), Toast.LENGTH_LONG).show();
        }
    }


    public void gMostrarMensajeAdvertencia(ViewGroup toVista, String tsMensaje)
    {
        try{
            //ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(loContext).inflate(R.layout.dialogo_alerta, toVista, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(loContext, R.style.myDialog));
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button btnOk = dialogView.findViewById(R.id.buttonCerrar);
            TextView poLabelTexto = dialogView.findViewById(R.id.txtMensaje);
            poLabelTexto.setText(tsMensaje);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }catch (Exception ex)
        {
            Toast.makeText(loContext, "No se pudo mostrar el Dialogo. Error en el metodo: gMostrarMensajeAdvertencia() "+ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void gMostrarMensajeInformacion(ViewGroup toVista, String tsMensaje)
    {
        try{
            //ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(loContext).inflate(R.layout.dialogo_informacion, toVista, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(loContext, R.style.myDialog));
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button btnOk = dialogView.findViewById(R.id.buttonCerrar);
            TextView poLabelTexto = dialogView.findViewById(R.id.txtMensaje);
            poLabelTexto.setText(tsMensaje);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }catch (Exception ex)
        {
            Toast.makeText(loContext, "No se pudo mostrar el Dialogo. Error en el metodo: gMostrarMensajeInformacion() "+ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void gMostrarMensajeConfirmacion(ViewGroup toVista, String... tsMensaje)
    {

    }

    public void gMostrarMensajeError(ViewGroup toVista, String tsMensaje)
    {
        try{
            //ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(loContext).inflate(R.layout.dialogo_error, toVista, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(loContext, R.style.myDialog));
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            Button btnOk = dialogView.findViewById(R.id.buttonCerrar);
            TextView poLabelTexto = dialogView.findViewById(R.id.txtMensaje);
            poLabelTexto.setText(tsMensaje);
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
}
