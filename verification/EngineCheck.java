import ma.ensaf.calculator.CalculatorEngine;
import java.io.*;
public class EngineCheck {
    static int count;
    static CalculatorEngine run(String... keys) {
        CalculatorEngine engine = new CalculatorEngine();
        for (String key : keys) engine.press(key);
        return engine;
    }
    static void expect(String expected, String... keys) {
        String actual = run(keys).display();
        if (!expected.equals(actual)) throw new AssertionError(expected + " != " + actual);
        count++;
    }
    public static void main(String[] args) throws Exception {
        expect("5", "2", "+", "3", "=");
        expect("-3", "2", "−", "5", "=");
        expect("20", "2", "+", "3", "×", "4", "=");
        expect("2.5", "5", "÷", "2", "=");
        expect("Erreur", "5", "÷", "0", "=");
        expect("7", "5", "÷", "0", "=", "7");
        expect("1.5", "1", ".", ".", "5");
        expect("8", "2", "xʸ", "3", "=");
        expect("3", "9", "√");
        expect("Erreur", "9", "±", "√");
        expect("2", "1", "0", "0", "log");
        expect("Erreur", "0", "ln");
        expect("Erreur", "9", "0", "tan");
        expect("0.5", "5", "0", "%");
        expect("8", "2", "+", "3", "=", "+", "3", "=");
        expect("5", "9", "√", "+", "2", "=");
        expect("5", "2", "+", "9", "√", "=");
        expect("-3", "2", "+", "±", "5", "=");
        expect("4", "1", "2", "⌫", "+", "3", "=");
        expect("6", "2", "+", "×", "3", "=");
        expect("5", "2", "+", "⌫", "+", "3", "=");
        CalculatorEngine e = run("DEG", "π", "sin");
        if (Math.abs(Double.parseDouble(e.display())) > 1e-12) throw new AssertionError("Radians");
        count++;
        e = run("1", "2", "+");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        new ObjectOutputStream(bytes).writeObject(e);
        e = (CalculatorEngine) new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray())).readObject();
        e.press("3"); e.press("=");
        if (!"15".equals(e.display())) throw new AssertionError("Restoration");
        count++;
        System.out.println("PASS: " + count + " calculator checks");
    }
}
