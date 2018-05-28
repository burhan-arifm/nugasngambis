package uinsgd.mcampus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

public class TugasActivity extends AppCompatActivity{
    private RecyclerView recycler_tugas;
    private List<Tugas> tugas;
    private DBDataSource dataSource;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tugas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recycler_tugas = (RecyclerView) findViewById(R.id.recycler_tugas);
        recycler_tugas.setHasFixedSize(true);
        recycler_tugas.setLayoutManager(new LinearLayoutManager(this));

        dataSource = new DBDataSource(this);
        dataSource.open();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_tugas);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        setData();
                        setAdapter();
                    }
                }, 1500);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_new_task = new Intent(getApplicationContext(), TugasEditActivity.class);
                startActivity(intent_new_task);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.about) {
            startActivity(new Intent(getApplicationContext(), TentangActivity.class));
        }

        return true;
    }

    void setData() {
        tugas = dataSource.getAllTugas();
    }

    void setAdapter() {
        TugasAdapter tugasAdapter = new TugasAdapter(getApplicationContext(), tugas);
        recycler_tugas.setAdapter(tugasAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        setData();
        setAdapter();
    }
}
