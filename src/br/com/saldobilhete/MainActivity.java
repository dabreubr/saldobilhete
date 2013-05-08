package br.com.saldobilhete;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private static final int DIALOG_INSERIR_SALDO = 1;
	private static final int DIALOG_OUTROS_VALORES = 2;
	
	private Float saldoBilhete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        saldoBilhete = Float.valueOf(0);
        
        Button btnInserirSaldo = (Button) findViewById(R.id.btnInserirSaldo);
        Button btnCPTMMetro = (Button) findViewById(R.id.btnCPTMMetro);
        Button btnOutrosValores = (Button) findViewById(R.id.btnOutrosValores);
       
        btnInserirSaldo.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Dialog inserirSaldo = showTextEntryDialog(DIALOG_INSERIR_SALDO);
            	inserirSaldo.show();
            }
        });
        
        btnCPTMMetro.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	cptmMetro();
            }
        });
        
        btnOutrosValores.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Dialog outrosValores = showTextEntryDialog(DIALOG_OUTROS_VALORES);
            	outrosValores.show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    protected Dialog showTextEntryDialog(int dialog) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);

        switch (dialog) {
    	case DIALOG_INSERIR_SALDO:
	        return new AlertDialog.Builder(this)
	            .setIcon(R.drawable.alert_dialog_icon)
	            .setTitle(R.string.inserirSaldo)
	            .setView(textEntryView)
	            .setPositiveButton(R.string.alert_dialog_confirmar, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	EditText edtValor = (EditText) textEntryView.findViewById(R.id.edtValor);
	                	saldoBilhete = saldoBilhete + Float.valueOf(edtValor.getText().toString());
	                	atualizaTela();
	                }
	            })
	            .setNegativeButton(R.string.alert_dialog_cancelar, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	
	                    dialog.cancel();
	                }
	            })
	            .create();
    	case DIALOG_OUTROS_VALORES:
	        return new AlertDialog.Builder(this)
	            .setIcon(R.drawable.alert_dialog_icon)
	            .setTitle(R.string.outrosValores)
	            .setView(textEntryView)
	            .setPositiveButton(R.string.alert_dialog_confirmar, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	                	EditText edtValor = (EditText) textEntryView.findViewById(R.id.edtValor);
	                	saldoBilhete = saldoBilhete - Float.valueOf(edtValor.getText().toString());
	                	atualizaTela();
	                }
	            })
	            .setNegativeButton(R.string.alert_dialog_cancelar, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int whichButton) {
	
	                    dialog.cancel();
	                }
	            })
	            .create();	       
    	}
    	return null;
    }
    
    private void atualizaTela() {
        TextView txtValorSaldoBilhete = (TextView) findViewById(R.id.txtValorSaldoBilhete);
        txtValorSaldoBilhete.setText(saldoBilhete.toString());
    }
    
	private void cptmMetro() {
		Float valorCPTMMetro = Float.valueOf(3);
		
		saldoBilhete = saldoBilhete - valorCPTMMetro;
		atualizaTela();
		
	}
    
}
