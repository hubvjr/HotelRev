package com.hotelrev;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;


import java.text.ParseException;
import java.util.ArrayList;

/**
 * The type Adapter image.
 */
public class AdapterText extends RecyclerView.Adapter<AdapterText.TextViewHold> {

    /**
     * The Image location.
     */
    ArrayList<TextCardHelper> imageLocation;

    final private ListItemClickListener OnCardClickListener;

    private int horizon;

    /**
     * Instantiates a new Adapter image.
     *
     * @param imageLocation the image location
     * @param listener      the listener
     * @param horizon       the horizon
     */
    public AdapterText(ArrayList<TextCardHelper> imageLocation, ListItemClickListener listener, int horizon) {
        this.imageLocation = imageLocation;
        OnCardClickListener = listener;
        this.horizon = horizon;
    }


    @Override
    public TextViewHold onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image_card, parent, false);

        return new TextViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TextViewHold holder, int position) {
        TextCardHelper imageCardHelper = imageLocation.get(position);
        holder.title.setText(imageCardHelper.getId());
        holder.startDate.setText(imageCardHelper.getLine1());
        holder.endDate.setText(imageCardHelper.getLine2());

    }

    @Override
    public int getItemCount() {
        return imageLocation.size();
    }

    /**
     * The interface List item click listener.
     */
    public interface ListItemClickListener {
        /**
         * On card list click.
         *
         * @param index the index
         */
        void onCardListClick(int index) throws ParseException;
    }

    /**
     * The type view hold.
     */
    public class TextViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView title;
        private final TextView startDate;
        private final TextView endDate;


        /**
         * Instantiates a new view hold.
         *
         * @param itemView the item view
         */
        public TextViewHold(@NonNull @NotNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvTitle);
            startDate = itemView.findViewById(R.id.tvStartDate);
            endDate = itemView.findViewById(R.id.tvEndDate);
            itemView.setOnClickListener(this);

        }

        /**
         * card view click
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            try {
                OnCardClickListener.onCardListClick(clickedPosition);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

}

