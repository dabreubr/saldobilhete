package br.com.saldobilhete;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.database.DatabaseHandler;
import br.com.entities.Cartao;
import br.com.entities.Transacao;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private static final int DIALOG_INSERIR_SALDO = 1;
	private static final int DIALOG_OUTROS_VALORES = 2;
	private Cartao cartao;
	private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        db = new DatabaseHandler(this);
        cartao = db.getCartao(1);
        
        Button btnInserirSaldo = (Button) findViewById(R.id.btnInserirSaldo);
        Button btnCPTMMetro = (Button) findViewById(R.id.btnCPTMMetro);
        Button btnOutrosValores = (Button) findViewById(R.id.btnOutrosValores);
        Button btnHistorico = (Button) findViewById(R.id.btnHistorico);
        
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
        
        btnHistorico.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Intent it = new Intent(MainActivity.this, HistoricoListActivity.class);
            	startActivity(it);
            }
        });
        
        atualizaTela();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
         
        switch (item.getItemId())
        {
        case R.id.action_settings:
            return true;
        case R.id.limparDados:
        	db.limparDados();
        	cartao = db.getCartao(1);
        	atualizaTela();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
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
	                	inserirSaldo(Float.valueOf(edtValor.getText().toString()));
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
	                	cartao.setSaldo(cartao.getSaldo() - Float.valueOf(edtValor.getText().toString()));
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
        txtValorSaldoBilhete.setText(cartao.getSaldo().toString());
    }
    
	@SuppressLint("SimpleDateFormat") 
	private void cptmMetro() {
		Float valorCPTMMetro = Float.valueOf(3);
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = df.format(Calendar.getInstance().getTime());
		
		Transacao transacao = new Transacao();
		transacao.setData(formattedDate);
		transacao.setTipo(2);
		transacao.setValor(valorCPTMMetro);
		transacao.setCartao(cartao);
		transacao.setDebitoCredito("D");
		
		db.addTransacao(transacao);
		
		cartao.setSaldo(cartao.getSaldo() - valorCPTMMetro);
		db.updateSaldo(cartao);
		atualizaTela();
		
	}
	
	@SuppressLint("SimpleDateFormat") 
	private void inserirSaldo(float valor) {
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		String formattedDate = df.format(Calendar.getInstance().getTime());
		
		Transacao transacao = new Transacao();
		transacao.setData(formattedDate);
		transacao.setTipo(1);
		transacao.setValor(valor);
		transacao.setCartao(cartao);
		transacao.setDebitoCredito("C");
		
		db.addTransacao(transacao);
		
		cartao.setSaldo(cartao.getSaldo() + valor);
		db.updateSaldo(cartao);
		atualizaTela();
		
	}
   
}
