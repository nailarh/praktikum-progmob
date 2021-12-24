package com.nananana.grenie;

import android.net.Uri;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TampilTanamanActivity extends AppCompatActivity {

    TextView nama, deskripsi, temperatur, cara_merawat, jenis;
    ImageView gambar;
    Button button;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_tanaman);

        gambar = findViewById(R.id.gambar);
        nama = findViewById(R.id.nama);
        deskripsi = findViewById(R.id.deskripsi);
        temperatur = findViewById(R.id.temperatur);
        cara_merawat = findViewById(R.id.cara);
        jenis = findViewById(R.id.jenis);

        String gambarUriString;
        gambarUriString = getIntent().getExtras().getString("gambar");
        Uri gambarUri = Uri.parse(gambarUriString );
        gambar.setImageURI(gambarUri);
        nama.setText(getIntent().getExtras().getString("nama"));
        deskripsi.setText(getIntent().getExtras().getString("deskripsi"));
        temperatur.setText(getIntent().getExtras().getString("temperatur"));
        cara_merawat.setText(getIntent().getExtras().getString("cara_merawat"));
        jenis.setText(getIntent().getExtras().getString("jenis"));

        button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TampilTanamanActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "Application On Start", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "Beralih ke Detail Tanaman", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "Application On Resume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "Beralih ke Tambah Tanaman", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Data Sebelumnya", Toast.LENGTH_SHORT).show();
    }

    public void backButton(View view) { onBackPressed(); }
}