package com.aquarius.vnumap.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.NumberPicker;

import com.aquarius.vnumap.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class SettingActivity extends AppCompatActivity {
    private static final String[] MENU_STRINGS = {"Số điểm đánh dấu", "Màu sắc các điểm đánh dấu", "Màu sắc điểm đánh dấu được chọn"};
    private static final CharSequence[] MENU_COLOR_STRING = {"Đỏ", "Xanh nước biển", "Xanh lá cây", "Vàng", "Tím", "Da cam"};
    public static final float[] MENU_COLOR_FLOAT = {BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_BLUE,
                                                    BitmapDescriptorFactory.HUE_GREEN, BitmapDescriptorFactory.HUE_YELLOW,
                                                    BitmapDescriptorFactory.HUE_VIOLET, BitmapDescriptorFactory.HUE_ORANGE};
    public static final String KEY_PREFERENCES = "VNUMAP123";
    public static final String KEY_NUMBER_MARKER = "numberofmarker";
    public static final String KEY_COLOR = "colorofmarker";
    public static final String KEY_COLOR_CHOOSE = "colorofmarkerchoose";

    private SharedPreferences setting = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setting = getSharedPreferences(KEY_PREFERENCES, 0);
        final SharedPreferences.Editor editor = setting.edit();

        Toolbar toolbar = (Toolbar)findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Cài đặt");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, MapsActivity.class);
                finish();
                startActivity(intent);
            }
        });

        ListView listView = (ListView)findViewById(R.id.setting_list);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MENU_STRINGS));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0 :
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingActivity.this);
                        final NumberPicker numberPicker = new NumberPicker(SettingActivity.this);
                        numberPicker.setMaxValue(10);
                        numberPicker.setMinValue(1);
                        numberPicker.setValue(setting.getInt(KEY_NUMBER_MARKER, 10));
                        FrameLayout frameLayout = new FrameLayout(SettingActivity.this);
                        frameLayout.addView(numberPicker, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
                        alertDialog.setView(frameLayout);
                        alertDialog.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editor.putInt(KEY_NUMBER_MARKER, numberPicker.getValue());
                                editor.commit();
                            }
                        });
                        alertDialog.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alertDialog.setTitle(MENU_STRINGS[0]);
                        alertDialog.show();
                        break;
                    case 1 :
                        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                        builder.setTitle(MENU_STRINGS[1]);
                        builder.setSingleChoiceItems(MENU_COLOR_STRING, setting.getInt(KEY_COLOR, 0), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editor.putInt(KEY_COLOR, i);
                                editor.commit();
                            }
                        });
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();

                        break;
                    case 2 :
                        final AlertDialog.Builder builder3 = new AlertDialog.Builder(SettingActivity.this);
                        builder3.setTitle(MENU_STRINGS[2]);
                        builder3.setSingleChoiceItems(MENU_COLOR_STRING, setting.getInt(KEY_COLOR_CHOOSE, 3), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editor.putInt(KEY_COLOR_CHOOSE, i);
                                editor.commit();

                            }
                        });
                        builder3.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder3.show();
                        break;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
