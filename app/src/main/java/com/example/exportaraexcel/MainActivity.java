package com.example.exportaraexcel;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnExportar =(Button)findViewById(R.id.btnExportar);
        btnExportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHealper dbHelper = new DBHealper(getBaseContext());
                dbHelper.insertData();

                final Cursor cursor = dbHelper.getuser();

                File sd = Environment.getExternalStorageDirectory();
                String csvFile = "myData.xls";

                File directory = new File(sd.getAbsolutePath());
                //create directory if not exist
                if (!directory.isDirectory()) {
                    directory.mkdirs();
                }
                try {

                    //file path
                    File file = new File(directory, csvFile);
                    WorkbookSettings wbSettings = new WorkbookSettings();
                    wbSettings.setLocale(new Locale("en", "EN"));
                    WritableWorkbook workbook;
                    workbook = Workbook.createWorkbook(file, wbSettings);
                    WritableSheet sheet = workbook.createSheet("userList", 0);
                    // column and row
                    sheet.addCell(new Label(0, 0, "user_name"));
                    sheet.addCell(new Label(1, 0, "phone_number"));

                    if (cursor.moveToFirst()) {
                        do {
                            String name = cursor.getString(cursor.getColumnIndex("user_name"));
                            String phoneNumber = cursor.getString(cursor.getColumnIndex("phone_number"));

                            int i = cursor.getPosition() + 1;
                            sheet.addCell(new Label(0, i, name));
                            sheet.addCell(new Label(1, i, phoneNumber));
                        } while (cursor.moveToNext());
                    }
                    //closing cursor
                    cursor.close();
                    workbook.write();
                    workbook.close();
                    Toast.makeText(getApplication(),
                            "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



    }
}
