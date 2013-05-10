package br.com.saldobilhete;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.adapters.ExpandableListAdapter;
import br.com.database.DatabaseHandler;
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
    List<String> childList;
    Map<String, List<String>> operacoesCollection;
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
 
        setGroupIndicatorToRight();
 
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                final String selected = (String) expListAdapter.getChild(
                        groupPosition, childPosition);
                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG)
                        .show();
 
                return true;
            }
        });
    }
 
    private void createGroupList() {
        groupList = new ArrayList<String>();
        groupList.add("2013-05-09");
        groupList.add("2013-05-10");
    }
 
    private void createCollection() {
        operacoesCollection = new LinkedHashMap<String, List<String>>();
 
        for (String data : groupList) {
            if (data.equals("2013-05-09")) { 
                ArrayList<String> chields = new ArrayList<String>();
                chields.add("Crédito R$ 100,00");
                chields.add("Débito CPTM/Metro R$ 3,00"); 
                chields.add("Débito CPTM/Metro R$ 3,00");
            	loadChild(chields);
            }
            else if (data.equals("2013-05-10")) {
                ArrayList<String> chields = new ArrayList<String>(); 
                chields.add("Débito CPTM/Metro R$ 3,00");
                loadChild(chields);
            }
 
            operacoesCollection.put(data, childList);
        }
    }
 
    private void loadChild(ArrayList<String> operacoes) {
        childList = new ArrayList<String>();
        for (String model : operacoes)
            childList.add(model);
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
