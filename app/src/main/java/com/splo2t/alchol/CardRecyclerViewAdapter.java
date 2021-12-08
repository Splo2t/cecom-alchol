package com.splo2t.alchol;



import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.splo2t.alchol.model.CardViewItemDTO;
import com.splo2t.alchol.model.ResultData;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class CardRecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<CardViewItemDTO> cardViewItemDTOs = new ArrayList<>();
    Context context;
    final static int dbVersion = 2;
    public CardRecyclerViewAdapter(CardViewItemDTO[] cardViewItemDTO) {
        for(int i = 0; i < cardViewItemDTO.length; i++){
            cardViewItemDTOs.add(cardViewItemDTO[i]);
        }
    }


    public void setData(CardViewItemDTO[] temp){
        ArrayList<CardViewItemDTO> returnData = new ArrayList<>();
        for(int i = 0; i < temp.length; i++){
            returnData.add(temp[i]);
        }
        this.cardViewItemDTOs = returnData;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cardview, viewGroup, false);

        return new RowCell(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
      //  ((RowCell)viewHolder).imageView.setImageResource(cardViewItemDTOs.get(position).imageView);
        ((RowCell)viewHolder).title.setText(cardViewItemDTOs.get(position).title);
        ((RowCell)viewHolder).subtitle.setText(cardViewItemDTOs.get(position).subtitle);

    }

    @Override
    public int getItemCount() {
        return cardViewItemDTOs.size();
    }

    public class RowCell extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ImageView itemManageBtn;
        public TextView title;
        public TextView subtitle;
        public TextView price;
        public RowCell(View view){
            super(view);
            imageView = (ImageView) view.findViewById(R.id.cardView_image);
            title = (TextView) view.findViewById(R.id.cardView_title);
            subtitle = (TextView) view.findViewById(R.id.cardView_subtitle);
        }
    }


}