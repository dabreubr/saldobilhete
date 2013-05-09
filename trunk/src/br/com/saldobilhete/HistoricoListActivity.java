package br.com.saldobilhete;

import br.com.database.DatabaseHandler;
import br.com.entities.Transacao;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class HistoricoListActivity extends ListActivity {

	private DatabaseHandler db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new DatabaseHandler(this);
		ArrayAdapter<Transacao> arrayAdapter = new ArrayAdapter<Transacao>(this, android.R.layout.simple_list_item_1, db
				.getAllTransactions());
		setListAdapter(arrayAdapter);
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Object o = this.getListAdapter().getItem(position);
		String item = o.toString();
		Toast.makeText(this, item, Toast.LENGTH_SHORT).show();
	}
	
	

}
