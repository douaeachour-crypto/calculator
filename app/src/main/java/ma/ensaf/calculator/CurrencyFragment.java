package ma.ensaf.calculator;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.math.BigDecimal;

public class CurrencyFragment extends Fragment {
    public CurrencyFragment() { super(R.layout.fragment_devise); }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle state) {
        Spinner from = view.findViewById(R.id.fromCurrency);
        Spinner to = view.findViewById(R.id.toCurrency);
        EditText amount = view.findViewById(R.id.amount);
        EditText rate = view.findViewById(R.id.rate);
        TextView label = view.findViewById(R.id.rateLabel);
        TextView result = view.findViewById(R.id.conversionResult);
        String[] currencies = {"MAD", "EUR", "USD", "GBP", "CAD"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                R.layout.currency_item, currencies);
        adapter.setDropDownViewResource(R.layout.currency_item);
        from.setAdapter(adapter);
        to.setAdapter(adapter);
        if (state != null) {
            from.setSelection(state.getInt("from", 1));
            to.setSelection(state.getInt("to", 0));
        } else { from.setSelection(1); to.setSelection(0); }
        // Capture the pair: a new pair must never silently reuse the old rate.
        final String[] lastPair = {from.getSelectedItem() + "/" + to.getSelectedItem()};
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View child, int position, long id) {
                String source = from.getSelectedItem().toString();
                String target = to.getSelectedItem().toString();
                String pair = source + "/" + target;
                if (!lastPair[0].equals(pair)) { rate.setText(""); result.setText(""); }
                lastPair[0] = pair;
                label.setText("Taux : 1 " + source + " = … " + target);
                rate.setEnabled(!source.equals(target));
                if (source.equals(target)) rate.setText("1");
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { }
        };
        from.setOnItemSelectedListener(listener);
        to.setOnItemSelectedListener(listener);
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { result.setText(""); }
            @Override public void afterTextChanged(Editable s) { }
        };
        amount.addTextChangedListener(watcher);
        rate.addTextChangedListener(watcher);
        view.findViewById(R.id.convert).setOnClickListener(button -> {
            try {
                BigDecimal value = new BigDecimal(amount.getText().toString().trim().replace(',', '.'));
                BigDecimal multiplier = from.getSelectedItem().equals(to.getSelectedItem())
                        ? BigDecimal.ONE : new BigDecimal(rate.getText().toString().trim().replace(',', '.'));
                if (value.signum() < 0 || multiplier.signum() <= 0) throw new NumberFormatException();
                result.setText(value.multiply(multiplier).stripTrailingZeros().toPlainString()
                        + " " + to.getSelectedItem());
            } catch (NumberFormatException e) {
                result.setText("Saisissez un montant ≥ 0 et un taux > 0.");
            }
        });
    }
    @Override public void onSaveInstanceState(@NonNull Bundle out) {
        super.onSaveInstanceState(out);
        View view = getView();
        if (view != null) {
            out.putInt("from", ((Spinner) view.findViewById(R.id.fromCurrency)).getSelectedItemPosition());
            out.putInt("to", ((Spinner) view.findViewById(R.id.toCurrency)).getSelectedItemPosition());
        }
    }
}
