package com.nananana.grenie;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TambahTanamanActivity extends AppCompatActivity {
    int textSize = 0;
    String jenistanaman = "";
    EditText nama, deskripsi, cara_merawat;
    TextView txtSeekBar;
    SeekBar seekBar;
    RadioGroup jenis;
    CheckBox check;
    ImageView img1;
    Button btn1;
    DBHelper helper;
    long id;

    final int kodeGallery = 100, kodeKamera = 99;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_tanaman);

        helper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        nama = findViewById(R.id.et_nama);
        deskripsi = findViewById(R.id.et_deskripsi);
        cara_merawat = findViewById(R.id.et_caramerawat);
        txtSeekBar = findViewById(R.id.seekBarValue);
        seekBar = findViewById(R.id.seekBar);
        check = findViewById(R.id.checkbox);
        img1 = (ImageView) findViewById(R.id.gambar1);
        btn1 = (Button) findViewById(R.id.btn4);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentGallery, kodeGallery);
            }
        });

        txtSeekBar.setText(Integer.toString(textSize));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                txtSeekBar.setText(Integer.toString(textSize));
            }
        });

        jenis = findViewById(R.id.jenis);
        jenis.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.hias:
                        jenistanaman = "Hias";
                        break;
                    case R.id.herbal:
                        jenistanaman = "Herbal";
                        break;
                    case R.id.bunga:
                        jenistanaman = "Bunga";
                        break;
                    case R.id.buah:
                        jenistanaman = "Buah";
                        break;
                    case R.id.sayur:
                        jenistanaman = "Sayur";
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == kodeGallery && resultCode == RESULT_OK) {
            imageUri = data.getData();
            img1.setImageURI(imageUri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    public void bt_submit(View view) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_add:
                String sgambar = imageUri.toString().trim();
                String snama = nama.getText().toString().trim();
                String sdeskripsi = deskripsi.getText().toString().trim();
                String stemperatur = txtSeekBar.getText().toString().trim();
                String scara = cara_merawat.getText().toString().trim();
                String sjenis = jenis.toString().trim();

                ContentValues values = new ContentValues();
                values.put(DBHelper.row_gambar, sgambar);
                values.put(DBHelper.row_nama, snama);
                values.put(DBHelper.row_deskripsi, sdeskripsi);
                values.put(DBHelper.row_temperatur, stemperatur);
                values.put(DBHelper.row_cara, scara);
                values.put(DBHelper.row_jenis, sjenis);

                if (nama.getText().toString().isEmpty() || deskripsi.getText().toString().isEmpty() ||
                        cara_merawat.getText().toString().isEmpty() || jenistanaman == "") {
                    Toast.makeText(getApplicationContext(), "Data belum lengkap", Toast.LENGTH_SHORT).show();
                } else if (!check.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Mohon konfirmasi kembali apakah data sudah benar", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            this);
                    alertDialogBuilder.setTitle("Data Tanaman");
                    alertDialogBuilder
                            .setMessage("Nama Tanaman: " + nama.getText().toString() +
                                    "\nDeskripsi Tanaman: " + deskripsi.getText().toString() +
                                    "\nTemperature yang Diperlukan: " + txtSeekBar.getText().toString() +
                                    "\nCara Merawat: " + cara_merawat.getText().toString() +
                                    "\nJenis Tanaman: " + jenistanaman)
                            .setIcon(R.drawable.file)
                            .setCancelable(false)
                            .setPositiveButton("DETAIL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(TambahTanamanActivity.this, TampilTanamanActivity.class);
                                    intent.putExtra("gambar", imageUri.toString());
                                    intent.putExtra("nama", nama.getText().toString());
                                    intent.putExtra("deskripsi", deskripsi.getText().toString());
                                    intent.putExtra("temperatur", txtSeekBar.getText().toString());
                                    intent.putExtra("cara_merawat", cara_merawat.getText().toString());
                                    intent.putExtra("jenis", jenistanaman);
                                    startActivity(intent);
                                    Toast.makeText(TambahTanamanActivity.this, "Beralih ke Detail Tanaman", Toast.LENGTH_SHORT).show();
                                }
                            });
                    helper.insertData(values);
                    Toast.makeText(TambahTanamanActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void backButton(View view) { onBackPressed(); }
}