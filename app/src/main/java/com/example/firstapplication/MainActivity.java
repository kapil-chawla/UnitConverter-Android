package com.example.firstapplication;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Spinner unitType;
    private Spinner conversionType;
    private EditText inputField;
    private TextView resultView;
    private double multiplyFactor;
    private final Map<String , String> conversionMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();

        unitType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String unitType = (String)parent.getItemAtPosition(position);
                ArrayAdapter<CharSequence> conversionAdapter = ArrayAdapter.createFromResource(MainActivity.this,
                        getConversionResourceByID(unitType.toLowerCase(),"array") , android.R.layout.simple_list_item_checked);
                conversionType.setAdapter(conversionAdapter);

                populateConversionMap(conversionMap, getResourceArrayByID(unitType.toLowerCase()),
                        getResourceArrayByID(unitType.toLowerCase()+"Factor"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        conversionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inputField.setText("");
                resultView.setText("");
                String conversionType = (String)parent.getItemAtPosition(position);
                String mulFac = conversionMap.get(conversionType);
                if(mulFac!=null && !("").equals(mulFac))
                    multiplyFactor = Double.parseDouble(mulFac);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        inputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null && !s.toString().equals("")){
                    double d = Double.parseDouble(s.toString());
                    double result = d * multiplyFactor;
                    resultView.setText(getResourceStringByID("result")  + result);
                }else{
                    resultView.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void populateConversionMap(Map<String, String> conversionMap, String[] keys, String[] values) {
        inputField.setText("");
        resultView.setText("");
        conversionMap.clear();
        for(int i=0;i<keys.length;i++){
            conversionMap.put(keys[i],values[i]);
        }
    }

    private void initComponents(){
        unitType = findViewById(R.id.unitType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.unitTypes, android.R.layout.simple_list_item_activated_1);
        unitType.setAdapter(adapter);

        conversionType = findViewById(R.id.conversionType);

        inputField = findViewById(R.id.inputField);
        resultView = findViewById(R.id.resultView);
    }

    public int getConversionResourceByID(String name, String resType) {
        Resources res = MainActivity.this.getResources();
        return res.getIdentifier(name, resType, this.getPackageName());
    }
    public String getResourceStringByID(String name) {
        Resources res = MainActivity.this.getResources();
        return res.getString(res.getIdentifier(name, "string", this.getPackageName()));
    }
    public String[] getResourceArrayByID(String name) {
        Resources res = MainActivity.this.getResources();
        return res.getStringArray(res.getIdentifier(name, "array", this.getPackageName()));
    }
}