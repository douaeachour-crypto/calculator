package ma.ensaf.calculator;

import java.io.Serializable;
import java.math.BigDecimal;

// Immediate calculation, left to right, as in the original app.
public class CalculatorEngine implements Serializable {
    private static final long serialVersionUID = 1L;
    private String input = "0", operator = "";
    private double accumulator;
    private boolean fresh = true, error = false;
    public boolean degrees = true;

    public String display() { return error ? "Erreur" : input; }
    public String history() { return operator.isEmpty() ? "" : format(accumulator) + " " + operator; }
    public static String format(double value) {
        if (!Double.isFinite(value)) throw new ArithmeticException("Résultat invalide");
        return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
    }
    private void clear() {
        input = "0"; operator = ""; accumulator = 0; fresh = true; error = false;
    }
    public void press(String key) {
        if (key.equals("C")) { clear(); return; }
        if (key.equals("DEG")) { degrees = !degrees; return; }
        if (error) {
            clear();
            if (!(key.matches("[0-9]") || key.equals(".") || key.equals("π") || key.equals("e"))) return;
        }
        try {
            if (key.matches("[0-9]") || key.equals(".")) {
                if (fresh) { input = "0"; fresh = false; }
                if (key.equals(".")) {
                    if (!input.contains(".")) input += ".";
                } else {
                    input = input.equals("0") ? key : input.equals("-0") ? "-" + key : input + key;
                }
            } else if (key.equals("⌫")) {
                // A pending operator with no second operand can be removed.
                if (fresh && !operator.isEmpty()) { operator = ""; input = format(accumulator); }
                else {
                    input = input.length() > 1 ? input.substring(0, input.length() - 1) : "0";
                    if (input.equals("-")) input = "0";
                }
                fresh = false;
            } else if (key.equals("π") || key.equals("e")) {
                input = format(key.equals("π") ? Math.PI : Math.E); fresh = false;
            } else if (key.equals("±")) {
                if (fresh && !operator.isEmpty()) input = "0";
                input = input.startsWith("-") ? input.substring(1) : "-" + input;
                fresh = false;
            } else if (key.equals("=")) {
                if (!operator.isEmpty() && !fresh) {
                    input = format(calculate(accumulator, Double.parseDouble(input), operator));
                    operator = ""; fresh = true;
                }
            } else if (key.equals("+") || key.equals("−") || key.equals("×")
                    || key.equals("÷") || key.equals("xʸ")) {
                if (!operator.isEmpty() && !fresh) {
                    input = format(calculate(accumulator, Double.parseDouble(input), operator));
                }
                accumulator = Double.parseDouble(input);
                operator = key; fresh = true;
            } else {
                if (fresh && !operator.isEmpty()) return;
                double x = Double.parseDouble(input), value;
                double angle = degrees ? Math.toRadians(x) : x;
                switch (key) {
                    case "%": value = x / 100; break;
                    case "√": value = Math.sqrt(x); break;
                    case "x²": value = x * x; break;
                    case "1/x": value = 1 / x; break;
                    case "log": value = Math.log10(x); break;
                    case "ln": value = Math.log(x); break;
                    case "sin": value = Math.sin(angle); break;
                    case "cos": value = Math.cos(angle); break;
                    case "tan":
                        if (Math.abs(Math.cos(angle)) < 1e-12) throw new ArithmeticException();
                        value = Math.tan(angle); break;
                    default: return;
                }
                input = format(value);
                fresh = operator.isEmpty();
            }
        } catch (ArithmeticException | NumberFormatException exception) {
            clear(); error = true;
        }
    }
    private double calculate(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "−": return a - b;
            case "×": return a * b;
            case "÷": if (b == 0) throw new ArithmeticException(); return a / b;
            case "xʸ": return Math.pow(a, b);
            default: throw new IllegalArgumentException(op);
        }
    }
}
