package com.splo2t.alchol.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.splo2t.alchol.CardRecyclerViewAdapter;
import com.splo2t.alchol.R;
import com.splo2t.alchol.model.CardViewItemDTO;
import com.splo2t.alchol.model.DrinkList;
import com.splo2t.alchol.model.ResultData;
import com.google.firebase.FirebaseApp;


import java.util.ArrayList;
import java.util.Arrays;

public class RecipeResultActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_result);

        Toast.makeText(this, getIntent().getStringExtra("Input"), Toast.LENGTH_SHORT).show();
        final String[] selectedData = getIntent().getStringExtra("Input").split(",");
        Log.d("AVC", Arrays.toString(selectedData));
        boolean[] checked = new boolean[DrinkList.data.length];

        for(int i = 0; i < checked.length; i++){
            checked[i] = true;
        }

        ArrayList<String> tempList = new ArrayList<>();

        final String[] unSelectedData;

        for( int i = 0; i < checked.length; i++){
            for(int j = 0; j <selectedData.length; j++){
                if(DrinkList.data[i].equals(selectedData[j])){
                    checked[i] = false;
                    break;
                }

            }
        }

        for(int i = 0; i < checked.length; i++){
            if(checked[i] == true){
                tempList.add(DrinkList.data[i]);
            }
        }

        unSelectedData = tempList.toArray(new String[tempList.size()]);
        if(unSelectedData.length > 0){
            Log.d("AVC", Arrays.toString(unSelectedData));
        }

        RecyclerView recyclerView = findViewById(R.id.result_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CardRecyclerViewAdapter(initialCardViewItem()));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Drink")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<CardViewItemDTO> test = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                int count = 0;
                                ResultData data = new ResultData();
                                data.setMenu(document.getId());
                                data.setSource(document.getData().get("Source").toString());
                                String[] tempArr = data.getSource().split(",");
                                for(String temp:tempArr){
                                    for(int i = 0; i < selectedData.length; i++){
                                        Log.d("selected_length", String.valueOf(selectedData.length));

                                        if(selectedData[i].equals(temp)){
                                            count++;
                                        }
                                        else if(selectedData[i].equals("")){
                                            count++;
                                        }
                                        if(count == selectedData.length){
                                            Log.d("Result", data.getMenu());
                                            document.getData();
                                            String sourceData = ((String)document.getData().get("Source")).replace(" ", "");
                                            String ratio = ((String)document.getData().get("Ratio")).replace(" ", "");
                                            String[] displayData = sourceData.split(",");
                                            String[] tempRatio = ratio.split(",");
                                            for(int j = 0; j < displayData.length; j++){
                                                displayData[j] += tempRatio[j];
                                            }
                                            test.add(new CardViewItemDTO(document.getId().toString(), Arrays.toString(displayData)));
                                            break;
                                        }
                                    }
                                    if(count == selectedData.length){
                                        break;
                                    }
                                }

                            }
                            CardRecyclerViewAdapter temp = (CardRecyclerViewAdapter) recyclerView.getAdapter();
                            temp.setData( test.toArray(new CardViewItemDTO[test.size()]));
                            temp.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Error getting documents : " + task.getException(), Toast.LENGTH_SHORT).show();
                            Log.d("Error", String.valueOf(task.getException()));
                        }
                    }
                });
    }

    CardViewItemDTO[] initialCardViewItem(){
        CardViewItemDTO[] returnCardViewItemDTO = new CardViewItemDTO[1];
        returnCardViewItemDTO[0] = new CardViewItemDTO("temp", "temp");
        return returnCardViewItemDTO;
    }
}
