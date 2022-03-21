package vib.track.cerberus.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import vib.track.cerberus.data.History;
import vib.track.cerberus.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {

    private OnClickAction onClickAction;
    List<History> historyList = new ArrayList<>();

    public interface OnClickAction {
        void onClickActionMethod(int position);
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item_card, parent,false);
        return new HistoryHolder(itemView);
    }

    public static class HistoryHolder extends RecyclerView.ViewHolder {
        final private TextView eventDate, eventTime;
        final private CardView vCardView;
        final private ImageView typeImg;

        public HistoryHolder(View itemView) {
            super(itemView);
            vCardView = itemView.findViewById(R.id.box_item);
            typeImg = itemView.findViewById(R.id.mode_image);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventTime = itemView.findViewById(R.id.eventTime);
        }

        public CardView getvCardView() {
            return vCardView;
        }
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, final int position) {
        History historyObj = historyList.get(position);

        // converting Date object to String object
        DateFormat dateFormat = new SimpleDateFormat("mm-dd-yyyy");
        String strDate = dateFormat.format(historyObj.getEventDate());

        // converting Time object to String object
        Time pureTime = historyObj.getEventTime();
        String strTime = pureTime.toString();

        // need to find a way to set the image
        // holder.typeImg.setImage(); ??
        // displaying appropriate vector image
        String eType = historyObj.getEventType();
        if( eType.equalsIgnoreCase("door") )
        {
            holder.typeImg.setImageResource(R.drawable.ic_baseline_sensor_door_24);
            //for testing purposes, may comment out this next line
            System.out.println(eType);
        }
        else if( eType.equalsIgnoreCase("window") )
        {
            holder.typeImg.setImageResource(R.drawable.ic_baseline_sensor_window_24);
            //for testing purposes, may comment out this next line
            System.out.println(eType);
        }
        else if( eType.equalsIgnoreCase("doorbell") )
        {
            holder.typeImg.setImageResource(R.drawable.ic_baseline_ring_24);
            //for testing purposes, may comment out this next line
            System.out.println(eType);
        }
        else
        {
            holder.typeImg.setImageResource(R.drawable.ic_baseline_not_interested_24);
            //for testing purposes, may comment out this next line
            System.out.println(eType);
        }

        //Displaying objects in designated boxes of the history card
        holder.eventDate.setText(strDate);
        holder.eventTime.setText(strTime);

        holder.getvCardView().setOnClickListener(view -> onClickAction.onClickActionMethod(position));
    }

    @Override
    public int getItemCount(){ return historyList.size(); }

    public HistoryAdapter(List<History> Histories){ historyList = Histories; }
    public void setOnClickAction(OnClickAction onClickAction) { this.onClickAction = onClickAction; }

}

