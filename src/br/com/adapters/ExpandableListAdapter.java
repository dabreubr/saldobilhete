package br.com.adapters;

import java.util.List;
import java.util.Map;

import br.com.database.DatabaseHandler;
import br.com.entities.Transacao;
import br.com.saldobilhete.HistoricoListActivity;
import br.com.saldobilhete.R;
 
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class ExpandableListAdapter extends BaseExpandableListAdapter {
 
    private Activity context;
    private Map<String, List<Transacao>> transactionsCollections;
    private List<String> dates;
 
    public ExpandableListAdapter(Activity context, List<String> dates,
            Map<String, List<Transacao>> transactionsCollections) {
        this.context = context;
        this.transactionsCollections = transactionsCollections;
        this.dates = dates;
    }
 
    public Object getChild(int groupPosition, int childPosition) {
        return transactionsCollections.get(dates.get(groupPosition)).get(childPosition);
    }
 
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    public View getChildView(final int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        final Transacao transaction = (Transacao) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();
 
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.historico_list_chield, null);
        }
 
        TextView item = (TextView) convertView.findViewById(R.id.operacao);
 
        ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new OnClickListener() {
 
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Deseja remover?");
                builder.setCancelable(false);
                builder.setPositiveButton("Sim",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                List<Transacao> child =
                                		transactionsCollections.get(dates.get(groupPosition));
                                DatabaseHandler db = new DatabaseHandler(context);
                                db.deleteTransaction(child.get(childPosition));
                                child.remove(childPosition);
                                notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("Não",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
 
        item.setText(transaction.toListString());
        return convertView;
    }
 
    public int getChildrenCount(int groupPosition) {
        return transactionsCollections.get(dates.get(groupPosition)).size();
    }
 
    public Object getGroup(int groupPosition) {
        return dates.get(groupPosition);
    }
 
    public int getGroupCount() {
        return dates.size();
    }
 
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        String transactionName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.historico_list_group,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.data);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(transactionName);
        return convertView;
    }
 
    public boolean hasStableIds() {
        return true;
    }
 
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}