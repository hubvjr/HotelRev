package com.hotelrev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class BookingDetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView tv1;
    private TextView tv2;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        Intent i = getIntent();
        String key = i.getStringExtra("key");

        imageView = findViewById(R.id.imageView);
        tv1 = findViewById(R.id.detail1);
        tv2 = findViewById(R.id.detail2);
        btn = findViewById(R.id.btn);

        FirebaseDatabase database =
                FirebaseDatabase.getInstance(BuildConfig.FIREBASE_URL);
        DatabaseReference ref = database.getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Booking")
                .child(key);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BookingDto dto = snapshot.getValue(BookingDto.class);
                tv1.setText(key);
                tv2.setText("Number of Pax: " + dto.numOfPax + "\n"+
                        "Special Request: " + dto.specialRequest);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //initializing MultiFormatWriter for QR code
        MultiFormatWriter mWriter = new MultiFormatWriter();
        try {
            //BitMatrix class to encode entered text and set Width & Height
            BitMatrix mMatrix = mWriter.encode(key, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder mEncoder = new BarcodeEncoder();
            Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
            imageView.setImageBitmap(mBitmap);//Setting generated QR code to imageView
            // to hide the keyboard
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        btn.setOnClickListener(v -> {
            FirebaseDatabase data =
                    FirebaseDatabase.getInstance(BuildConfig.FIREBASE_URL);
            data.getReference().child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("Booking")
                    .child(key).removeValue();

            data = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_URL);
            data.getReference().child("book").child(key).removeValue();

            Toast.makeText(this, "Cancelling .... ", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);


            Toast.makeText(this, "Booking Cancelled", Toast.LENGTH_SHORT).show();
        });


    }
}