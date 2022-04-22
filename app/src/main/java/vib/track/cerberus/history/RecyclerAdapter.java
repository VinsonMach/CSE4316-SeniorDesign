package vib.track.cerberus.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vib.track.cerberus.R;
import vib.track.cerberus.data.HistoryData;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{

    private Context c;
    private List<HistoryData> historyDataList;

    public RecyclerAdapter(Context c, List<HistoryData> historyDataList) {
        this.c = c;
        this.historyDataList = historyDataList;
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(c).inflate(R.layout.recycler_view, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {

        holder.name.setText("" + historyDataList.get(position).getSensorName());
        holder.time.setText("" + historyDataList.get(position).getEventTime());
        holder.date.setText("" + historyDataList.get(position).getEventDate());

    }

    @Override
    public int getItemCount() {
        return historyDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView time;
        TextView date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.dataSensorName);
            time = (TextView)itemView.findViewById(R.id.dataEventTime);
            date = (TextView)itemView.findViewById(R.id.dataEventDate);

        }
    }
}