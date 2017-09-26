package ch.hsr.mge.gadgeothek.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ch.hsr.mge.gadgeothek.R;
import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;

public class GadgeothekActivity extends AppCompatActivity {

    private static final String ACTIVE_TAB = GadgeothekActivity.class.getCanonicalName() + ".ACTIVE_TAB";
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gadgeothek);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        // TODO ViewPager erstellen und Fragments angeben

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gadgeothek, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(ACTIVE_TAB, viewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                LibraryService.logout(new Callback<Boolean>() {
                    @Override
                    public void onCompletion(Boolean input) {
                        SharedPreferences preferences = getSharedPreferences(AbstractAuthenticationActivity.PREFERENCES, MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        // disable auto-login for the next time
                        editor.putBoolean(AbstractAuthenticationActivity.LAST_AUTOLOGIN_FAILED, true);
                        editor.apply();
                        Intent intent = new Intent(GadgeothekActivity.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);
                        finish();
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}
