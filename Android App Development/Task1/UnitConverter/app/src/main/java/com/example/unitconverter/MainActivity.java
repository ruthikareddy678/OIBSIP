package com.example.unitconverter;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TEMPERATURE = "Temperature";

    // category -> (unit name -> factor to the category's base unit)
    private final Map<String, Map<String, Double>> categories = new LinkedHashMap<>();

    private Spinner spinnerCategory, spinnerFrom, spinnerTo;
    private EditText etValue;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildUnitData();

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        etValue = findViewById(R.id.etValue);
        tvResult = findViewById(R.id.tvResult);
        Button btnConvert = findViewById(R.id.btnConvert);

        spinnerCategory.setAdapter(makeAdapter(new ArrayList<>(categories.keySet())));

        // Switching category resets both unit dropdowns and clears the result
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadUnitsFor((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        btnConvert.setOnClickListener(v -> convert());
    }

    private void buildUnitData() {
        Map<String, Double> length = new LinkedHashMap<>(); // base: metre
        length.put("Millimetre", 0.001);
        length.put("Centimetre", 0.01);
        length.put("Metre", 1.0);
        length.put("Kilometre", 1000.0);
        length.put("Inch", 0.0254);
        length.put("Foot", 0.3048);
        length.put("Yard", 0.9144);
        length.put("Mile", 1609.344);
        categories.put("Length", length);

        Map<String, Double> weight = new LinkedHashMap<>(); // base: kilogram
        weight.put("Milligram", 0.000001);
        weight.put("Gram", 0.001);
        weight.put("Kilogram", 1.0);
        weight.put("Tonne", 1000.0);
        weight.put("Ounce", 0.028349523125);
        weight.put("Pound", 0.45359237);
        categories.put("Weight", weight);

        Map<String, Double> volume = new LinkedHashMap<>(); // base: litre
        volume.put("Millilitre", 0.001);
        volume.put("Litre", 1.0);
        volume.put("US Fluid Ounce", 0.0295735295625);
        volume.put("US Cup", 0.2365882365);
        volume.put("US Gallon", 3.785411784);
        categories.put("Volume", volume);

        // Temperature is not a simple factor; handled separately in convertTemperature()
        Map<String, Double> temp = new LinkedHashMap<>();
        temp.put("Celsius", 0.0);
        temp.put("Fahrenheit", 0.0);
        temp.put("Kelvin", 0.0);
        categories.put(TEMPERATURE, temp);
    }

    private ArrayAdapter<String> makeAdapter(List<String> items) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void loadUnitsFor(String category) {
        List<String> units = new ArrayList<>(categories.get(category).keySet());
        spinnerFrom.setAdapter(makeAdapter(units));
        spinnerTo.setAdapter(makeAdapter(units));
        if (units.size() > 1) {
            spinnerTo.setSelection(1); // default target differs from source
        }
        tvResult.setText("");
    }

    private void convert() {
        String input = etValue.getText().toString().trim();

        if (input.isEmpty()) {
            Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show();
            return;
        }

        double value;
        try {
            value = Double.parseDouble(input);
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        String category = (String) spinnerCategory.getSelectedItem();
        String from = (String) spinnerFrom.getSelectedItem();
        String to = (String) spinnerTo.getSelectedItem();

        double result;
        if (TEMPERATURE.equals(category)) {
            result = convertTemperature(value, from, to);
        } else {
            Map<String, Double> units = categories.get(category);
            result = value * units.get(from) / units.get(to);
        }

        tvResult.setText(format(value) + " " + from + " = " + format(result) + " " + to);
    }

    private double convertTemperature(double value, String from, String to) {
        double celsius;
        switch (from) {
            case "Fahrenheit": celsius = (value - 32) * 5.0 / 9.0; break;
            case "Kelvin":     celsius = value - 273.15; break;
            default:           celsius = value;
        }
        switch (to) {
            case "Fahrenheit": return celsius * 9.0 / 5.0 + 32;
            case "Kelvin":     return celsius + 273.15;
            default:           return celsius;
        }
    }

    private String format(double number) {
        if (number == 0) return "0";
        return new BigDecimal(number)
                .round(new MathContext(8))
                .stripTrailingZeros()
                .toPlainString();
    }
}
