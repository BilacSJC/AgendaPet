package com.converter.cambio.app_petshop.Controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.converter.cambio.app_petshop.Activitys.Cliente.CadastroClienteActivity;
import com.converter.cambio.app_petshop.Activitys.Cliente.LoginClienteActivity;
import com.converter.cambio.app_petshop.R;

public class MetodosPadraoController {

    public void  alertDialog(Context context, String strTitle, String strMsg){
        new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert)
                .setTitle(strTitle)
                .setMessage(strMsg)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    } }).show();
    }

    public void  alertToast(Context context, String strMsg){
        Toast toast = Toast.makeText(context, strMsg, Toast.LENGTH_LONG);
        toast.show();
    }
}
