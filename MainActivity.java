package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etResult;
    private String input = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etResult = findViewById(R.id.etResult);

        // Number buttons
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btn4 = findViewById(R.id.btn4);
        Button btn5 = findViewById(R.id.btn5);
        Button btn6 = findViewById(R.id.btn6);
        Button btn7 = findViewById(R.id.btn7);
        Button btn8 = findViewById(R.id.btn8);
        Button btn9 = findViewById(R.id.btn9);
        Button btn0 = findViewById(R.id.btn0);

        // Operation buttons
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnSubtract = findViewById(R.id.btnSubtract);
        Button btnMultiply = findViewById(R.id.btnMultiply);
        Button btnDivide = findViewById(R.id.btnDivide);
        Button btnEqual = findViewById(R.id.btnEqual);
        Button btnClear = findViewById(R.id.btnClear);

        // Set click listeners for number buttons
        View.OnClickListener numberClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                input += clickedButton.getText().toString();
                etResult.setText(input);
            }
        };

        btn1.setOnClickListener(numberClickListener);
        btn2.setOnClickListener(numberClickListener);
        btn3.setOnClickListener(numberClickListener);
        btn4.setOnClickListener(numberClickListener);
        btn5.setOnClickListener(numberClickListener);
        btn6.setOnClickListener(numberClickListener);
        btn7.setOnClickListener(numberClickListener);
        btn8.setOnClickListener(numberClickListener);
        btn9.setOnClickListener(numberClickListener);
        btn0.setOnClickListener(numberClickListener);

        // Set click listeners for operation buttons
        btnAdd.setOnClickListener(v -> appendOperation("+"));
        btnSubtract.setOnClickListener(v -> appendOperation("-"));
        btnMultiply.setOnClickListener(v -> appendOperation("*"));
        btnDivide.setOnClickListener(v -> appendOperation("/"));

        // Equal button
        btnEqual.setOnClickListener(v -> calculateResult());

        // Clear button
        btnClear.setOnClickListener(v -> {
            input = "";
            etResult.setText("");
        });
    }

    // Method to append operation symbols to the input
    private void appendOperation(String operator) {
        input += operator;
        etResult.setText(input);
    }

    // Method to calculate the result
    private void calculateResult() {
        try {
            // Evaluate the mathematical expression using a simple eval method (Note: This should be used carefully, and in production, a safer approach is recommended)
            double result = evaluateExpression(input);
            etResult.setText(String.valueOf(result));
            input = String.valueOf(result); // Save result for further operations
        } catch (Exception e) {
            etResult.setText("Error");
            input = "";
        }
    }

    // Simple method to evaluate basic mathematical expressions (for demonstration purposes only)
    private double evaluateExpression(String expression) throws Exception {
        // In a real app, consider using a safer and more robust evaluation library
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() throws Exception {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new Exception("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() throws Exception {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm(); // Addition
                    else if (eat('-')) x -= parseTerm(); // Subtraction
                    else return x;
                }
            }

            double parseTerm() throws Exception {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor(); // Multiplication
                    else if (eat('/')) x /= parseFactor(); // Division
                    else return x;
                }
            }

            double parseFactor() throws Exception {
                if (eat('+')) return parseFactor(); // Unary plus
                if (eat('-')) return -parseFactor(); // Unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // Parenthesis
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // Numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else {
                    throw new Exception("Unexpected: " + (char) ch);
                }
                return x;
            }
        }.parse();
    }
}
