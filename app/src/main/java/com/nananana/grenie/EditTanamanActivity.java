package com.nananana.grenie;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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

public class EditTanamanActivity extends AppCompatActivity {
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
    TextView tv_nama, tv_deskripsi, tv_temperatur, tv_cara_merawat, tv_jenis;
    ImageView tv_gambar;

    final int kodeGallery = 100, kodeKamera = 99;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tanaman);

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

    private void getData(){
        Cursor cursor = helper.oneData(id);
        if(cursor.moveToFirst()){
            String gambarUriString;
            gambarUriString = cursor.getString(cursor.getColumnIndex(DBHelper.row_gambar));
            Uri gambar = Uri.parse(gambarUriString );
            String nama = cursor.getString(cursor.getColumnIndex(DBHelper.row_nama));
            String deskripsi = cursor.getString(cursor.getColumnIndex(DBHelper.row_deskripsi));
            String temperatur = cursor.getString(cursor.getColumnIndex(DBHelper.row_temperatur));
            String cara = cursor.getString(cursor.getColumnIndex(DBHelper.row_cara));
            String jenis = cursor.getString(cursor.getColumnIndex(DBHelper.row_jenis));

            tv_gambar.setImageURI(gambar);
            tv_nama.setText(nama);
            tv_deskripsi.setText(deskripsi);
            tv_temperatur.setText(temperatur);
            tv_cara_merawat.setText(cara);
            tv_jenis.setText(jenis);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    public void bt_submit(View view) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_edit:
                String sgambar = imageUri.toString().trim();
                String snama = nama.getText().toString().trim();
                String sdeskripsi = deskripsi.getText().toString().trim();
                String stemperatur = txtSeekBar.getText().toString().trim();
                String scara = cara_merawat.getText().toString().trim();
                String sjenis = jenistanaman.trim();

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
                    helper.updateData(values, id);
                    Toast.makeText(EditTanamanActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                }

                switch (item.getItemId()) {
                    case R.id.delete_edit:
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditTanamanActivity.this);
                        builder.setMessage("Data ini akan dihapus.");
                        builder.setCancelable(true);
                        builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                helper.deleteData(id);
                                Toast.makeText(EditTanamanActivity.this, "Data Terhapus", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    public void backButton(View view) { onBackPressed(); }
}