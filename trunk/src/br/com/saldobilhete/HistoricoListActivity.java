package br.com.saldobilhete;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.adapters.ExpandableListAdapter;
import br.com.database.DatabaseHandler;
import br.com.entities.Transacao;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

public class HistoricoListActivity extends Activity {

	private DatabaseHandler db;
	List<String> groupList;
    List<Transacao> childList;
    Map<String, List<Transacao>> operacoesCollection;
    ExpandableListView expListView;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.historico_list);
		
		db = new DatabaseHandler(this);
		
		createGroupList();
		 
        createCollection();
 
        expListView = (ExpandableListView) findViewById(R.id.listaHistorico);
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(
                this, groupList, operacoesCollection);
        expListView.setAdapter(expListAdapter);
        for(int i=0; i<expListAdapter.getGroupCount(); i++)
        	expListView.expandGroup(i);
        setGroupIndicatorToRight();
 
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                final Transacao selected = (Transacao) expListAdapter.getChild(
                        groupPosition, childPosition);
                Toast.makeText(getBaseContext(), selected.toListString(), Toast.LENGTH_SHORT)
                        .show();
 
                return true;
            }
        });
    }
 
    private void createGroupList() {
        groupList = db.getAllDateTransactions();
    }
 
    private void createCollection() {
        operacoesCollection = new LinkedHashMap<String, List<Transacao>>();
        
        for (String date : groupList) {
			ArrayList<Transacao> chields = new ArrayList<Transacao>();
            for (Transacao transacao : db.getTransactionsByDate(date))
            	chields.add(transacao);
			loadChild(chields);
 
            operacoesCollection.put(date, childList);
        }
    }
 
    private void loadChild(ArrayList<Transacao> operacoes) {
        childList = new ArrayList<Transacao>();
        for (Transacao operacao : operacoes)
            childList.add(operacao);
    }
 
    private void setGroupIndicatorToRight() {
        /* Get the screen width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
 
        expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }
 
    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }
 
}
