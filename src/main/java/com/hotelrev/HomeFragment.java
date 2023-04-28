package com.hotelrev;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", /*Locale.getDefault()*/Locale.ENGLISH);

    private TextView tv;
    private Button btnUpdate;

    private EditText numOfPax;
    private EditText specialRequest;

    private ImageView imageView;

    private String type = "";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tv = getView().findViewById(R.id.textView);
        btnUpdate = getView().findViewById(R.id.btnBook);

        numOfPax = getView().findViewById(R.id.edNum);
        specialRequest = getView().findViewById(R.id.edSpecial);

        imageView = getView().findViewById(R.id.imageView);

        Spinner spinner = getView().findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        setSubtitle(dateFormat.format(new Date()));
        canBook();
        final CompactCalendarView compactCalendarView = getView().findViewById(R.id.compactcalendar_view);
        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary
        // Use constants provided by Java Calendar class
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.setLocale(TimeZone.getDefault(), /*Locale.getDefault()*/Locale.ENGLISH);
        compactCalendarView.setShouldDrawDaysHeader(true);

        // define a listener to receive callbacks when certain events happen.
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                setSubtitle(dateFormat.format(dateClicked));
                canBook();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                setSubtitle(dateFormat.format(firstDayOfNewMonth));
                canBook();
            }
        });

        btnUpdate.setOnClickListener(v -> {
            if (numOfPax.getText().toString().length() == 0) {
                numOfPax.setError("");
                return;
            }
            if (specialRequest.getText().toString().length() == 0) {
                specialRequest.setError("");
                return;
            }
            FirebaseDatabase database =
                    FirebaseDatabase.getInstance(BuildConfig.FIREBASE_URL);
            DatabaseReference ref = database.getReference();
            DatabaseReference usersRef = ref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Booking").child(tv.getText().toString() + " " + type);

            BookingDto dto = new BookingDto();
            dto.numOfPax = Integer.parseInt(numOfPax.getText().toString());
            dto.specialRequest = specialRequest.getText().toString();
            dto.roomType = type;

            usersRef.setValue(dto);

            DatabaseReference ref2 = database.getReference();
            DatabaseReference usersRef2 = ref2.child("book").child(tv.getText().toString() + " " + type);
            usersRef2.setValue(true);
            Toast.makeText(getContext(), "BOOK SUCCESS", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        });
    }

    public void setSubtitle(String subtitle) {
        tv.setText(subtitle);
    }

    private void canBook() {

        FirebaseDatabase database =
                FirebaseDatabase.getInstance(BuildConfig.FIREBASE_URL);
        DatabaseReference ref = database.getReference();
        DatabaseReference usersRef = ref.child("book").child(tv.getText().toString() + " " + type);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Boolean.class) != null) {
                    setSubtitle("Fully Booked");
                    btnUpdate.setClickable(false);
                } else {
                    btnUpdate.setClickable(true);
                }
                System.out.println(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        type = (String) parent.getItemAtPosition(pos);
        setSubtitle(dateFormat.format(new Date()));
        canBook();
        if(pos == 0){
            imageView.setImageResource(R.drawable.singlebed);
        }else if(pos == 1){
            imageView.setImageResource(R.drawable.doublebed);
        }else if(pos == 2){
            imageView.setImageResource(R.drawable.deluxebed);
        }else if(pos == 3){
            imageView.setImageResource(R.drawable.familybed);
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
    }
}