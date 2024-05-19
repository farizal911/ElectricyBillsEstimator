package com.example.electricitybillestimator;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils; // For empty string check
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUnits, editTextRebate;
    private TextView textViewResult;
    private Button buttonCalculate, buttonExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextUnits = findViewById(R.id.editTextUnits);
        editTextRebate = findViewById(R.id.editTextRebate);
        textViewResult = findViewById(R.id.textViewResult);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonExit = findViewById(R.id.buttonExit);

        // Set hint text for edit texts (optional)
        editTextUnits.setHint("Enter Units Consumed");
        editTextRebate.setHint("Enter Rebate Percentage");

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate input fields (combined logic)
                if (isEmpty(editTextUnits) || isEmpty(editTextRebate)) {
                    // Show error message
                    Toast.makeText(MainActivity.this, "Please enter both electricity units and rebate percentage.", Toast.LENGTH_SHORT).show();

                    return;
                }

                try {
                    calculateBill();
                } catch (NumberFormatException e) {
                    // Handle non-numeric input
                    Toast.makeText(MainActivity.this, "Please enter only numbers for units and rebate.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Exit the app
                finish();
            }
        });
    }

    private boolean isEmpty(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString());
    }

    private void calculateBill() {
        int units = Integer.parseInt(editTextUnits.getText().toString());
        double rebate = Double.parseDouble(editTextRebate.getText().toString());

        double totalCharges = 0;

        if (units <= 200) {
            totalCharges = units * 0.218;
        } else if (units <= 300) {
            totalCharges = 200 * 0.218 + (units - 200) * 0.334;
        } else if (units <= 600) {
            totalCharges = 200 * 0.218 + 100 * 0.334 + (units - 300) * 0.516;
        } else {
            totalCharges = 200 * 0.218 + 100 * 0.334 + 300 * 0.516 + (units - 600) * 0.546;
        }

        totalCharges = totalCharges - (totalCharges * (rebate / 100));
        textViewResult.setText(String.format("Total Charges: RM %.2f", totalCharges));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
