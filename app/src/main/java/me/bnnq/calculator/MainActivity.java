package me.bnnq.calculator;

import static me.bnnq.calculator.Calculator.calculateExpression;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Locale;

class Theme {
    public static int themeVal;
}

public class MainActivity extends AppCompatActivity {

    //to-do:
    //SHAREDPRAFERENCES CLICKHISTORY!!!!!!!
    // JUnit

    enum MenuButton {
        DIGIT_ZERO,
        DIGIT,
        OPERATOR_DIVIDE,
        OPERATOR,
        OPEN_BRACKET,
        CLOSE_BRACKET,
        DOT
    }

    int openBracketsCounter = 0;
    int closeBracketsCounter = 0;
    Deque<MenuButton> buttonClickHistory = new ArrayDeque<>();

    TextView textVisualInput;
    TextView textResult;

    Button digit0;
    Button digit1;
    Button digit2;
    Button digit3;
    Button digit4;
    Button digit5;
    Button digit6;
    Button digit7;
    Button digit8;
    Button digit9;

    Button openBracket;
    Button closeBracket;

    Button divide;
    Button multiply;
    Button minus;
    Button plus;

    Button dot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("CalculatorData", MODE_PRIVATE);
        Theme.themeVal = sharedPreferences.getInt("theme", R.style.LightTheme);
        setTheme(Theme.themeVal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        digit0 = findViewById(R.id.buttonDigit0);
        digit1 = findViewById(R.id.buttonDigit1);
        digit2 = findViewById(R.id.buttonDigit2);
        digit3 = findViewById(R.id.buttonDigit3);
        digit4 = findViewById(R.id.buttonDigit4);
        digit5 = findViewById(R.id.buttonDigit5);
        digit6 = findViewById(R.id.buttonDigit6);
        digit7 = findViewById(R.id.buttonDigit7);
        digit8 = findViewById(R.id.buttonDigit8);
        digit9 = findViewById(R.id.buttonDigit9);
        openBracket = findViewById(R.id.buttonCalculatorOpenBracket);
        closeBracket = findViewById(R.id.buttonCalculatorCloseBracket);
        divide = findViewById(R.id.buttonCalculatorDivide);
        multiply = findViewById(R.id.buttonCalculatorMultiply);
        minus = findViewById(R.id.buttonCalculatorMinus);
        plus = findViewById(R.id.buttonCalculatorPlus);
        dot = findViewById(R.id.buttonCalculatorDot);

        textVisualInput = findViewById(R.id.textVisualInput);
        textVisualInput.setText(sharedPreferences.getString("visualInput", "0"));
        textResult = findViewById(R.id.textResult);
        buttonClickHistory.push(MenuButton.DIGIT_ZERO);
        updateMenu();
        updateResult();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getSharedPreferences("CalculatorData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("visualInput", textVisualInput.getText().toString());
        editor.putInt("theme", Theme.themeVal);
        editor.apply();
    }

    @Override
    protected void onResume() {
        SharedPreferences sharedPreferences = getSharedPreferences("CalculatorData", MODE_PRIVATE);
        textVisualInput.setText(sharedPreferences.getString("visualInput", "0"));
        buttonClickHistory.clear();
        String strTextVisualInput = textVisualInput.getText().toString();
        for (int i = 0; i < strTextVisualInput.length(); i++) {
            switch (strTextVisualInput.charAt(i)) {
                case '0':
                    buttonClickHistory.push(MenuButton.DIGIT_ZERO);
                    break;
                case '/':
                    buttonClickHistory.push(MenuButton.OPERATOR_DIVIDE);
                    break;
                case '*':
                case '+':
                case '-':
                    buttonClickHistory.push(MenuButton.OPERATOR);
                    break;
                case '(':
                    openBracketsCounter++;
                    buttonClickHistory.push(MenuButton.OPEN_BRACKET);
                    break;
                case ')':
                    closeBracketsCounter++;
                    buttonClickHistory.push(MenuButton.CLOSE_BRACKET);
                    break;
                case '.':
                    buttonClickHistory.push(MenuButton.DOT);
                    break;
                default:
                    buttonClickHistory.push(MenuButton.DIGIT);
            }
        }
        updateMenu();

        Theme.themeVal = sharedPreferences.getInt("theme", R.style.LightTheme);
        super.onResume();
    }

    private void updateResult() {
        DecimalFormat df = new DecimalFormat("#", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(8);
        textResult.setText(df.format(calculateExpression(textVisualInput.getText().toString())));
    }

    private boolean isDotAvailable() {
        String str = textVisualInput.getText().toString();
        boolean result = true;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '.') {
                result = false;
                while (++i < str.length()) {
                    if (str.charAt(i) == '+' || str.charAt(i) == '-' || str.charAt(i) == '*' || str.charAt(i) == '/') {
                        result = true;
                        break;
                    }
                }
                i--;
            }
        }
        return result;
    }

    private void updateMenu() {
        MenuButton lastMenuButton = buttonClickHistory.peek();
        TypedValue typedValue = new TypedValue();

        switch (lastMenuButton) {
            case DIGIT:
            case DIGIT_ZERO: {
                digit0.setEnabled(true);
                getTheme().resolveAttribute(R.attr.digitColor, typedValue, true);
                digit0.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit1.setEnabled(true);
                digit1.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit2.setEnabled(true);
                digit2.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit3.setEnabled(true);
                digit3.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit4.setEnabled(true);
                digit4.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit5.setEnabled(true);
                digit5.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit6.setEnabled(true);
                digit6.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit7.setEnabled(true);
                digit7.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit8.setEnabled(true);
                digit8.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit9.setEnabled(true);
                digit9.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));

                openBracket.setEnabled(false);
                openBracket.setTextColor(getColor(R.color.transparency_orange));
                if (openBracketsCounter > closeBracketsCounter && textVisualInput.getText().toString().length() > 1) {
                    closeBracket.setEnabled(true);
                    closeBracket.setTextColor(getColor(R.color.orange));
                }
                else {
                    closeBracket.setEnabled(false);
                    closeBracket.setTextColor(getColor(R.color.transparency_orange));
                }

                divide.setEnabled(true);
                divide.setTextColor(getColor(R.color.orange));
                multiply.setEnabled(true);
                multiply.setTextColor(getColor(R.color.orange));
                minus.setEnabled(true);
                minus.setTextColor(getColor(R.color.orange));
                plus.setEnabled(true);
                plus.setTextColor(getColor(R.color.orange));

                if (isDotAvailable())
                    dot.setEnabled(true);
                else {
                    dot.setEnabled(false);
                    getTheme().resolveAttribute(R.attr.digitTransparencyColor, typedValue, true);
                }
                dot.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));

                break;
            }
            case OPERATOR:
            case OPEN_BRACKET: {
                digit0.setEnabled(true);
                getTheme().resolveAttribute(R.attr.digitColor, typedValue, true);
                digit0.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit1.setEnabled(true);
                digit1.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit2.setEnabled(true);
                digit2.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit3.setEnabled(true);
                digit3.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit4.setEnabled(true);
                digit4.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit5.setEnabled(true);
                digit5.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit6.setEnabled(true);
                digit6.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit7.setEnabled(true);
                digit7.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit8.setEnabled(true);
                digit8.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit9.setEnabled(true);
                digit9.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));

                openBracket.setEnabled(true);
                openBracket.setTextColor(getColor(R.color.orange));
                closeBracket.setEnabled(false);
                closeBracket.setTextColor(getColor(R.color.transparency_orange));

                divide.setEnabled(false);
                divide.setTextColor(getColor(R.color.transparency_orange));
                multiply.setEnabled(false);
                multiply.setTextColor(getColor(R.color.transparency_orange));
                minus.setEnabled(false);
                minus.setTextColor(getColor(R.color.transparency_orange));
                plus.setEnabled(false);
                plus.setTextColor(getColor(R.color.transparency_orange));

                getTheme().resolveAttribute(R.attr.digitTransparencyColor, typedValue, true);
                dot.setEnabled(false);
                dot.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));

                break;
            }
            case OPERATOR_DIVIDE: {
                getTheme().resolveAttribute(R.attr.digitTransparencyColor, typedValue, true);
                digit0.setEnabled(false);
                digit0.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                getTheme().resolveAttribute(R.attr.digitColor, typedValue, true);
                digit1.setEnabled(true);
                digit1.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit2.setEnabled(true);
                digit2.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit3.setEnabled(true);
                digit3.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit4.setEnabled(true);
                digit4.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit5.setEnabled(true);
                digit5.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit6.setEnabled(true);
                digit6.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit7.setEnabled(true);
                digit7.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit8.setEnabled(true);
                digit8.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit9.setEnabled(true);
                digit9.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));


                openBracket.setEnabled(true);
                openBracket.setTextColor(getColor(R.color.orange));
                if (openBracketsCounter > closeBracketsCounter) {
                    closeBracket.setEnabled(true);
                    closeBracket.setTextColor(getColor(R.color.orange));
                }
                else {
                    closeBracket.setEnabled(false);
                    closeBracket.setTextColor(getColor(R.color.transparency_orange));
                }

                divide.setEnabled(false);
                divide.setTextColor(getColor(R.color.transparency_orange));
                multiply.setEnabled(false);
                multiply.setTextColor(getColor(R.color.transparency_orange));
                minus.setEnabled(false);
                minus.setTextColor(getColor(R.color.transparency_orange));
                plus.setEnabled(false);
                plus.setTextColor(getColor(R.color.transparency_orange));

                getTheme().resolveAttribute(R.attr.digitTransparencyColor, typedValue, true);
                dot.setEnabled(false);
                dot.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));

                break;
            }
            case CLOSE_BRACKET: {
                getTheme().resolveAttribute(R.attr.digitTransparencyColor, typedValue, true);
                digit0.setEnabled(false);
                digit0.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit1.setEnabled(false);
                digit1.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit2.setEnabled(false);
                digit2.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit3.setEnabled(false);
                digit3.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit4.setEnabled(false);
                digit4.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit5.setEnabled(false);
                digit5.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit6.setEnabled(false);
                digit6.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit7.setEnabled(false);
                digit7.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit8.setEnabled(false);
                digit8.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit9.setEnabled(false);
                digit9.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));

                openBracket.setEnabled(false);
                openBracket.setTextColor(getColor(R.color.transparency_orange));
                if (openBracketsCounter > closeBracketsCounter) {
                    closeBracket.setEnabled(true);
                    closeBracket.setTextColor(getColor(R.color.orange));
                }
                else {
                    closeBracket.setEnabled(false);
                    closeBracket.setTextColor(getColor(R.color.transparency_orange));
                }

                divide.setEnabled(true);
                divide.setTextColor(getColor(R.color.orange));
                multiply.setEnabled(true);
                multiply.setTextColor(getColor(R.color.orange));
                minus.setEnabled(true);
                minus.setTextColor(getColor(R.color.orange));
                plus.setEnabled(true);
                plus.setTextColor(getColor(R.color.orange));

                getTheme().resolveAttribute(R.attr.digitTransparencyColor, typedValue, true);
                dot.setEnabled(false);
                dot.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));

                break;
            }
            case DOT: {
                digit0.setEnabled(true);
                getTheme().resolveAttribute(R.attr.digitColor, typedValue, true);
                digit0.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit1.setEnabled(true);
                digit1.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit2.setEnabled(true);
                digit2.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit3.setEnabled(true);
                digit3.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit4.setEnabled(true);
                digit4.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit5.setEnabled(true);
                digit5.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit6.setEnabled(true);
                digit6.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit7.setEnabled(true);
                digit7.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit8.setEnabled(true);
                digit8.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));
                digit9.setEnabled(true);
                digit9.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));

                openBracket.setEnabled(false);
                openBracket.setTextColor(getColor(R.color.transparency_orange));
                closeBracket.setEnabled(false);
                closeBracket.setTextColor(getColor(R.color.transparency_orange));

                divide.setEnabled(false);
                divide.setTextColor(getColor(R.color.transparency_orange));
                multiply.setEnabled(false);
                multiply.setTextColor(getColor(R.color.transparency_orange));
                minus.setEnabled(false);
                minus.setTextColor(getColor(R.color.transparency_orange));
                plus.setEnabled(false);
                plus.setTextColor(getColor(R.color.transparency_orange));

                getTheme().resolveAttribute(R.attr.digitTransparencyColor, typedValue, true);
                dot.setEnabled(false);
                dot.setTextColor(ContextCompat.getColor(this, typedValue.resourceId));

                break;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void onButtonDigitClick(final int val) {

        if (textResult.getText().toString().length() > 15 || textVisualInput.getText().toString().length() >= 225)
            return;

        try {
            if (val < 0 || val > 9)
                throw new IncorrectUseException("Unexpected value" + val + " in onButtonDigitClick");
        }
        catch (IncorrectUseException e) {
            e.printStackTrace();
            System.exit(-10);
        }

        StringBuilder sb = new StringBuilder(textVisualInput.getText().toString());
        if (val == 0 && sb.length() == 1 && sb.charAt(0) == '0')
            return;
        else if (sb.length() == 1 && sb.charAt(0) == '0')
            sb.deleteCharAt(0);
        sb.append(val);

        textVisualInput.setText(sb.toString());

        updateResult();
        if (val == 0)
            buttonClickHistory.push(MenuButton.DIGIT_ZERO);
        else
            buttonClickHistory.push(MenuButton.DIGIT);
        updateMenu();
    }

    public void onButtonThemeSwitcherClick(View view) {
        Theme.themeVal = (Theme.themeVal == R.style.LightTheme ? R.style.DarkTheme : R.style.LightTheme);
        recreate();
    }

    public void onButtonDigit0Click(View view) {
        onButtonDigitClick(0);
    }

    public void onButtonDigit1Click(View view) {
        onButtonDigitClick(1);
    }

    public void onButtonDigit2Click(View view) {
        onButtonDigitClick(2);
    }

    public void onButtonDigit3Click(View view) {
        onButtonDigitClick(3);
    }

    public void onButtonDigit4Click(View view) {
        onButtonDigitClick(4);
    }

    public void onButtonDigit5Click(View view) {
        onButtonDigitClick(5);
    }

    public void onButtonDigit6Click(View view) {
        onButtonDigitClick(6);
    }

    public void onButtonDigit7Click(View view) {
        onButtonDigitClick(7);
    }

    public void onButtonDigit8Click(View view) {
        onButtonDigitClick(8);
    }

    public void onButtonDigit9Click(View view) {
        onButtonDigitClick(9);
    }

    @SuppressLint("SetTextI18n")
    public void onButtonCalculatorPlusClick(View view) {
        if (textResult.getText().toString().length() > 15 || textVisualInput.getText().toString().length() >= 225)
            return;
        textVisualInput.setText((textVisualInput.getText().toString()) + "+");
        updateResult();
        buttonClickHistory.push(MenuButton.OPERATOR);
        updateMenu();
    }

    @SuppressLint("SetTextI18n")
    public void onButtonCalculatorMinusClick(View view) {
        if (textResult.getText().toString().length() > 15 || textVisualInput.getText().toString().length() >= 225)
            return;
        textVisualInput.setText((textVisualInput.getText().toString()) + "-");
        updateResult();
        buttonClickHistory.push(MenuButton.OPERATOR);
        updateMenu();
    }

    @SuppressLint("SetTextI18n")
    public void onButtonCalculatorMultiplyClick(View view) {
        if (textResult.getText().toString().length() > 15 || textVisualInput.getText().toString().length() >= 225)
            return;
        textVisualInput.setText((textVisualInput.getText().toString()) + "*");
        updateResult();
        buttonClickHistory.push(MenuButton.OPERATOR);
        updateMenu();
    }

    @SuppressLint("SetTextI18n")
    public void onButtonCalculatorDivideClick(View view) {
        if (textResult.getText().toString().length() > 15 || textVisualInput.getText().toString().length() >= 225)
            return;
        textVisualInput.setText((textVisualInput.getText().toString()) + "/");
        updateResult();
        buttonClickHistory.push(MenuButton.OPERATOR_DIVIDE);
        updateMenu();
    }

    @SuppressLint("SetTextI18n")
    public void onButtonCalculatorDotClick(View view) {
        if (textResult.getText().toString().length() > 15 || textVisualInput.getText().toString().length() >= 225)
            return;
        textVisualInput.setText((textVisualInput.getText().toString()) + ".");
        updateResult();
        buttonClickHistory.push(MenuButton.DOT);
        updateMenu();
    }

    public void onButtonCalculatorClearClick(View view) {
        textVisualInput.setText("0");
        updateResult();
        buttonClickHistory.clear();
        openBracketsCounter = 0;
        closeBracketsCounter = 0;
        buttonClickHistory.push(MenuButton.DIGIT_ZERO);
        updateMenu();
    }

    @SuppressLint("SetTextI18n")
    public void onButtonCalculatorBackspaceClick(View view) {
        StringBuilder sb = new StringBuilder(textVisualInput.getText().toString());
        if (sb.length() == 1 && sb.charAt(0) == '0')
            return;
        else if (sb.length() == 1) {
            buttonClickHistory.clear();
            closeBracketsCounter = 0;
            openBracketsCounter = 0;
            buttonClickHistory.push(MenuButton.DIGIT_ZERO);
            sb = new StringBuilder("0");
            textVisualInput.setText(sb.toString());
            updateResult();
            updateMenu();
            return;
        }

        sb.deleteCharAt(sb.length() - 1);
        textVisualInput.setText(sb.toString());
        updateResult();
        MenuButton temp = buttonClickHistory.pop();
        if (temp == MenuButton.OPEN_BRACKET)
            openBracketsCounter--;
        else if (temp == MenuButton.CLOSE_BRACKET)
            closeBracketsCounter--;
        updateMenu();
    }

    @SuppressLint("SetTextI18n")
    public void onButtonCalculatorOpenBracketClick(View view) {
        if (textResult.getText().toString().length() > 15 || textVisualInput.getText().toString().length() >= 225)
            return;
        textVisualInput.setText((textVisualInput.getText().toString()) + "(");
        openBracketsCounter++;
        buttonClickHistory.push(MenuButton.OPEN_BRACKET);
        updateResult();
        updateMenu();
    }

    @SuppressLint("SetTextI18n")
    public void onButtonCalculatorCloseBracketClick(View view) {
        if (textResult.getText().toString().length() > 15 || textVisualInput.getText().toString().length() >= 225)
            return;
        textVisualInput.setText((textVisualInput.getText().toString()) + ")");
        closeBracketsCounter++;
        buttonClickHistory.push(MenuButton.CLOSE_BRACKET);
        updateResult();
        updateMenu();


    }

    public void onButtonCalculatorEqualClick(View view) {
        if (textResult.getText().toString().equals("∞") || textResult.getText().toString().equals("NaN"))
            textVisualInput.setText("0");
        else
            textVisualInput.setText(textResult.getText().toString());
        buttonClickHistory.clear();
        buttonClickHistory.push(MenuButton.DIGIT);
        openBracketsCounter = 0;
        closeBracketsCounter = 0;
        updateResult();
        updateMenu();
    }

    public void onTextResultClick(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", textResult.getText().toString());
        clipboard.setPrimaryClip(clip);

        Toast.makeText(getApplicationContext(), R.string.textNotificationAboutClipboard, Toast.LENGTH_LONG).show();
    }

}