package uinsgd.mcampus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LihatTugasActivity extends AppCompatActivity {
    TextView namaMatkul;
    TextView jenisTugas;
    TextView deadline;
    TextView deskripsi;
    Calendar calendar;
    Bundle bundle;
    DBDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_tugas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        namaMatkul = (TextView) findViewById(R.id.matkul_text);
        jenisTugas = (TextView) findViewById(R.id.jenis_tugas_text);
        deadline = (TextView) findViewById(R.id.deadline_text);
        deskripsi = (TextView) findViewById(R.id.deskripsi_text);
        dataSource = new DBDataSource(getApplicationContext());
        calendar = Calendar.getInstance(new Locale("id"));

        bundle = this.getIntent().getExtras();
        //noinspection ConstantConditions
        String[] datetime = bundle.getString("deadline").split(" ");
        String[] tanggal = datetime[0].split("-");
        String[] jam = datetime[1].split(":");
        int[] dated = new int[tanggal.length];
        int[] timed = new int[jam.length];
        for (int i = 0; i < dated.length; i++) {
            dated[i] = Integer.parseInt(tanggal[i]);
        }
        for (int i = 0; i < timed.length; i++) {
            timed[i] = Integer.parseInt(jam[i]);
        }
        calendar.set(dated[0], dated[1], dated[2], timed[0], timed[1]);
        namaMatkul.setText(bundle.getString("namaMatkul"));
        jenisTugas.setText(bundle.getString("jenisTugas"));
        deadline.setText(new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm", new Locale("id"))
                .format(calendar.getTime()));
        deskripsi.setText(bundle.getString("deskripsi"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.popup_edit:
                Intent intent = new Intent(getApplicationContext(), TugasEditActivity.class);
                Bundle b = new Bundle();

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                b.putLong("idTugas", bundle.getLong("idTugas"));

                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.popup_delete:
                 dataSource.deleteTugas(bundle.getLong("idTugas"));
                 finish();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
