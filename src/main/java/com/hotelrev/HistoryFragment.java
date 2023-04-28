package com.hotelrev;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import lombok.SneakyThrows;

public class HistoryFragment extends Fragment {

    private RecyclerView listRecycler;
    private RecyclerView.Adapter adap;
    private ArrayList<TextCardHelper> cardList;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", /*Locale.getDefault()*/Locale.ENGLISH);


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Firebase.setAndroidContext(view.getContext());
        initRecycler();
    }

    private void initRecycler() {

        Firebase databaseTrending =
                new Firebase(BuildConfig.FIREBASE_URL);

        listRecycler = this.getView().findViewById(R.id.listRecyclerBooked);
        listRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        listRecycler.setLayoutManager(layoutManager);

        cardList = new ArrayList<>();

        // get the user enrolled event and add to card to display
        databaseTrending.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Booking").orderByKey().addChildEventListener(new ChildEventListener() {
            @SneakyThrows
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                BookingDto dto = snapshot.getValue(BookingDto.class);
                String s = "";
                if (dto.complete) {
                    s = "Completed\n";
                }
                String numOfPax = s + "\nNumber of Pax: " + dto.numOfPax;
                String specialRequest = "Special Request: \n" + dto.specialRequest;
                cardList.add(new TextCardHelper(snapshot.getKey(), numOfPax, specialRequest));
                adap = new AdapterText(cardList, index -> {
                    Intent i = new Intent(getContext(), BookingDetailsActivity.class);
                    i.putExtra("key",cardList.get(index).getId());
                    startActivity(i);

                }, 1);
                listRecycler.setAdapter(adap);


            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });


    }
}

