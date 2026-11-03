package ma.ensaf.calculator;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 pager;
    private final String[] titles = {"Standard", "Scientifique", "Devise"};
    private TabLayoutMediator mediator;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.ime());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
        pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2); // Keep the three small pages alive when swiping.
        pager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull @Override public Fragment createFragment(int position) {
                return position == 2 ? new CurrencyFragment()
                        : CalculatorFragment.newInstance(position == 1);
            }
            @Override public int getItemCount() { return titles.length; }
        });
        TabLayout tabs = findViewById(R.id.tabs);
        mediator = new TabLayoutMediator(tabs, pager,
                (tab, position) -> tab.setText(titles[position]));
        mediator.attach();
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override public void onPageSelected(int position) {
                setTitle(titles[position]);
                invalidateOptionsMenu();
            }
        });
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override public boolean onPrepareOptionsMenu(Menu menu) {
        int[] ids = {R.id.calc_standard, R.id.calc_scientifique, R.id.calc_devise};
        menu.findItem(ids[pager.getCurrentItem()]).setChecked(true);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.calc_standard) pager.setCurrentItem(0, true);
        else if (id == R.id.calc_scientifique) pager.setCurrentItem(1, true);
        else if (id == R.id.calc_devise) pager.setCurrentItem(2, true);
        else return super.onOptionsItemSelected(item);
        return true;
    }
    @Override protected void onDestroy() {
        if (mediator != null) mediator.detach();
        super.onDestroy();
    }
}
