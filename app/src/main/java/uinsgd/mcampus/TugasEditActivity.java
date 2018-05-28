package uinsgd.mcampus;

import android.app.*;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TugasEditActivity extends AppCompatActivity {
    private Button datetime_input;
    private Calendar calendar;
    private EditText editDeskripsi;
    private DBDataSource dataSource;
    private Spinner spinJenisTugas;
    //private Spinner spinMatkul;
    private EditText editMatkul;
    private DatePickerDialog.OnDateSetListener date;
    private TimePickerDialog.OnTimeSetListener time;
    private Long idTugas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tugas_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        datetime_input = (Button) findViewById(R.id.deadline_button);
        calendar = Calendar.getInstance(new Locale("id"));
        editDeskripsi = (EditText) findViewById(R.id.deskripsi_tugas);
        spinJenisTugas = (Spinner) findViewById(R.id.tipetugas_spinner);
        editMatkul = (EditText) findViewById(R.id.matkul_spinner);
        //spinMatkul = (Spinner) findViewById(R.id.matkul_spinner);
        dataSource = new DBDataSource(this);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idTugas = savedInstanceState != null
                ? savedInstanceState.getLong(DBDataSource.allColumns_tugas[0])
                : null;

        updateLabel();

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                updateLabel();
            }
        };
        datetime_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(TugasEditActivity.this, time,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                        .show();
                new DatePickerDialog(TugasEditActivity.this, date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
        setIdTugasFromIntent();
        populateFields();
    }

    private void updateLabel() {
        String format = "EEEE, dd MMM yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, new Locale("id"));
        datetime_input.setText(simpleDateFormat.format(calendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add:
                String namaMatkul;
                String jenisTugas;
                int spin_pos;
                String deadline;
                String deskripsi;

                if (!editMatkul.getText().toString().equals("")) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                            Locale.getDefault());
//                    namaMatkul = spinMatkul.getSelectedItem().toString();
                    namaMatkul = editMatkul.getText().toString();
                    jenisTugas = spinJenisTugas.getSelectedItem().toString();
                    spin_pos = spinJenisTugas.getSelectedItemPosition();
                    deadline = simpleDateFormat.format(calendar.getTime());
                    deskripsi = editDeskripsi.getText().toString();

                    if (idTugas == null) {
                        long idx = dataSource.createTugas(namaMatkul, jenisTugas, spin_pos,
                                deadline, deskripsi);

                        if (idx > 0) {
                            idTugas = idx;
                        }
                    } else {
                        dataSource.updateTugas(new Tugas(idTugas, namaMatkul, jenisTugas, spin_pos,
                                deadline, deskripsi));
                    }

                    new ReminderManager(this).setReminder(idTugas, calendar);

                    Toast.makeText(this, "Pengingat telah disimpan.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(this, "Mata Kuliah tidak boleh kosong.", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            case R.id.action_cancel:
                finish();
                break;
            default:
                break;
        }

        return true;
    }

    private void setIdTugasFromIntent() {
        if (idTugas == null) {
            Bundle extras = getIntent().getExtras();
            idTugas = extras != null
                    ? extras.getLong(DBDataSource.allColumns_tugas[0])
                    : null;
        }
    }

    private void populateFields() {
        if (idTugas != null) {
            Cursor reminder = dataSource.fetchTugas(idTugas);
            startManagingCursor(reminder);
            editMatkul.setText(reminder.getString(reminder
                    .getColumnIndexOrThrow(DBDataSource.allColumns_tugas[1])));
            spinJenisTugas.setSelection(reminder.getInt(reminder
                    .getColumnIndexOrThrow(DBDataSource.allColumns_tugas[3])));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd MMM yyyy HH:mm",
                    new Locale("id"));
            Date date;
            try {
                String datetime = reminder.getString(reminder
                        .getColumnIndexOrThrow(DBDataSource.allColumns_tugas[4]));
                date = simpleDateFormat.parse(datetime);
                calendar.setTime(date);
            } catch (ParseException e) {
                Log.e("ReminderEditActivity", e.getMessage(), e);
            }
            updateLabel();
        }
    }
}
