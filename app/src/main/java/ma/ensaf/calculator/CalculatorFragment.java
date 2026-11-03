package ma.ensaf.calculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CalculatorFragment extends Fragment {
    private CalculatorEngine engine = new CalculatorEngine();
    private TextView display, history;

    public static CalculatorFragment newInstance(boolean scientific) {
        CalculatorFragment fragment = new CalculatorFragment();
        Bundle args = new Bundle();
        args.putBoolean("scientific", scientific);
        fragment.setArguments(args);
        return fragment;
    }
    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            CalculatorEngine restored = (CalculatorEngine) savedInstanceState.getSerializable("engine");
            if (restored != null) engine = restored;
        }
    }
    @Nullable @Override public View onCreateView(@NonNull LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle state) {
        boolean scientific = requireArguments().getBoolean("scientific");
        return inflater.inflate(scientific ? R.layout.fragment_scientifique
                : R.layout.fragment_standard, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle state) {
        display = view.findViewById(R.id.display);
        history = view.findViewById(R.id.history);
        bindButtons(view);
        refresh();
        Button angle = view.findViewById(R.id.angleMode);
        if (angle != null) angle.setText(engine.degrees ? "DEG" : "RAD");
    }
    private void bindButtons(View view) {
        if (view instanceof Button) {
            view.setOnClickListener(button -> {
                String key = String.valueOf(button.getTag());
                engine.press(key);
                if (key.equals("DEG")) ((Button) button).setText(engine.degrees ? "DEG" : "RAD");
                refresh();
            });
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) bindButtons(group.getChildAt(i));
        }
    }
    private void refresh() {
        display.setText(engine.display());
        history.setText(engine.history());
    }
    @Override public void onSaveInstanceState(@NonNull Bundle out) {
        super.onSaveInstanceState(out);
        out.putSerializable("engine", engine);
    }
    @Override public void onDestroyView() {
        display = null;
        history = null;
        super.onDestroyView();
    }
}
