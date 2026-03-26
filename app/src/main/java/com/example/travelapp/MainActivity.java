package com.example.travelapp;

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

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Creating the private variables to access the values and perform actions on it
    private Spinner spinnerCategory, spinnerSource, spinnerDestination;
    private EditText editTextValue;
    private Button buttonConvert;
    private TextView textViewResult;

    // Creating arrays and adding respective values so that the same is used dynamically going forwards in case of its respective category
    private final String[] currencyUnits = {"USD", "AUD", "EUR", "JPY", "GBP"};
    private final String[] fuelUnits = {"mpg", "km/L"};
    private final String[] liquidUnits = {"Gallon (US)", "Liters"};
    private final String[] temperatureUnits = {"Celsius", "Fahrenheit", "Kelvin"};
    private final String[] distanceUnits = {"Nautical Mile", "Kilometers"};

    // Entry point of the app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // calling the parent's class so that app does not crash
        super.onCreate(savedInstanceState);
        // connecting the file to the XML file
        setContentView(R.layout.activity_main);

        // Connecting/accessing the variables from the XML file to Java file
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSource = findViewById(R.id.spinnerSource);
        spinnerDestination = findViewById(R.id.spinnerDestination);
        editTextValue = findViewById(R.id.editTextValue);
        buttonConvert = findViewById(R.id.buttonConvert);
        textViewResult = findViewById(R.id.textViewResult);

        // Calling the function described below so that the necessary options are updated
        updateUnitSpinners(spinnerCategory.getSelectedItem().toString());

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = spinnerCategory.getSelectedItem().toString();
                updateUnitSpinners(selectedCategory);
                textViewResult.setText(getString(R.string.default_result));
                editTextValue.setError(null);
            }

            // when nothing is selected, nothing happens, however this case never happens
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // This piece of code triggers the conversion when we click on the Convert button
        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performConversion();
            }
        });
    }

    // In this part, we are updating the From and To based on the Category we have selected.
    private void updateUnitSpinners(String category) {
        String[] units;

        switch (category) {
            case "Currency":
                units = currencyUnits;
                break;
            case "Fuel Efficiency":
                units = fuelUnits;
                break;
            case "Liquid Volume":
                units = liquidUnits;
                break;
            case "Temperature":
                units = temperatureUnits;
                break;
            case "Distance":
                units = distanceUnits;
                break;
            default:
                units = currencyUnits;
                break;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSource.setAdapter(adapter);
        spinnerDestination.setAdapter(adapter);
    }

    // This code performs the conversion based on the options selected in the Select Category.
    private void performConversion() {
        String inputText = editTextValue.getText().toString().trim();

        // Edge cases as mentioned in the assignment. In case the value is empty and if we are
        // trying to convert then the screen shows a toast with the message.
        if (inputText.isEmpty()) {
            editTextValue.setError("Please enter a value");
            Toast.makeText(this, "Input cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Converting the value entered to a double value so that we can perform calculations.
        // Also, try catch block is added in case we enter alphabets or special characters, then
        // the screen shows a toast to enter a valid input.
        double inputValue;
        try {
            inputValue = Double.parseDouble(inputText);
        } catch (NumberFormatException e) {
            editTextValue.setError("Please enter a valid number");
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            return;
        }

        String category = spinnerCategory.getSelectedItem().toString();
        String source = spinnerSource.getSelectedItem().toString();
        String destination = spinnerDestination.getSelectedItem().toString();

        // Identity conversion handling
        if (source.equals(destination)) {
            Toast.makeText(this, "Source and destination are the same", Toast.LENGTH_SHORT).show();
            textViewResult.setText(String.format(Locale.getDefault(),
                    "Converted Value: %.4f %s", inputValue, destination));
            return;
        }

        // Edge case. The value of temperature cannot be negative.
        if (inputValue < 0 && !category.equals("Temperature")) {
            editTextValue.setError("Negative values are not allowed for " + category);
            Toast.makeText(this, "Negative values are not valid for " + category, Toast.LENGTH_SHORT).show();
            return;
        }

        // Edge case. The unit Kelvin cannot be negative.
        if (category.equals("Temperature")) {
            if (source.equals("Kelvin") && inputValue < 0) {
                editTextValue.setError("Kelvin cannot be negative");
                Toast.makeText(this, "Kelvin cannot be negative", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        double result;

        switch (category) {
            case "Currency":
                result = convertCurrency(inputValue, source, destination);
                break;
            case "Fuel Efficiency":
                result = convertFuel(inputValue, source, destination);
                break;
            case "Liquid Volume":
                result = convertLiquid(inputValue, source, destination);
                break;
            case "Temperature":
                result = convertTemperature(inputValue, source, destination);
                break;
            case "Distance":
                result = convertDistance(inputValue, source, destination);
                break;
            default:
                result = inputValue;
                break;
        }

        textViewResult.setText(String.format(Locale.getDefault(),
                "Converted Value: %.4f %s", result, destination));
    }

    // Converting the currency below based on source and destination
    private double convertCurrency(double value, String source, String destination) {
        double usdValue;

        switch (source) {
            case "USD":
                usdValue = value;
                break;
            case "AUD":
                usdValue = value / 1.55;
                break;
            case "EUR":
                usdValue = value / 0.92;
                break;
            case "JPY":
                usdValue = value / 148.50;
                break;
            case "GBP":
                usdValue = value / 0.78;
                break;
            default:
                usdValue = value;
                break;
        }

        switch (destination) {
            case "USD":
                return usdValue;
            case "AUD":
                return usdValue * 1.55;
            case "EUR":
                return usdValue * 0.92;
            case "JPY":
                return usdValue * 148.50;
            case "GBP":
                return usdValue * 0.78;
            default:
                return usdValue;
        }
    }

    // Below is the logic for converting one unit of the fuel to another
    private double convertFuel(double value, String source, String destination) {
        if (source.equals("mpg") && destination.equals("km/L")) {
            return value * 0.425;
        } else if (source.equals("km/L") && destination.equals("mpg")) {
            return value / 0.425;
        }
        return value;
    }

    // Below is the logic for liquid conversion from one unit to another
    private double convertLiquid(double value, String source, String destination) {
        if (source.equals("Gallon (US)") && destination.equals("Liters")) {
            return value * 3.785;
        } else if (source.equals("Liters") && destination.equals("Gallon (US)")) {
            return value / 3.785;
        }
        return value;
    }

    // Below is the logic for temperature conversion
    private double convertTemperature(double value, String source, String destination) {
        if (source.equals("Celsius") && destination.equals("Fahrenheit")) {
            return (value * 1.8) + 32;
        } else if (source.equals("Fahrenheit") && destination.equals("Celsius")) {
            return (value - 32) / 1.8;
        } else if (source.equals("Celsius") && destination.equals("Kelvin")) {
            return value + 273.15;
        } else if (source.equals("Kelvin") && destination.equals("Celsius")) {
            return value - 273.15;
        } else if (source.equals("Fahrenheit") && destination.equals("Kelvin")) {
            return ((value - 32) / 1.8) + 273.15;
        } else if (source.equals("Kelvin") && destination.equals("Fahrenheit")) {
            return ((value - 273.15) * 1.8) + 32;
        }
        return value;
    }

    // Adding the logic here for distance conversion
    private double convertDistance(double value, String source, String destination) {
        if (source.equals("Nautical Mile") && destination.equals("Kilometers")) {
            return value * 1.852;
        } else if (source.equals("Kilometers") && destination.equals("Nautical Mile")) {
            return value / 1.852;
        }
        return value;
    }
}