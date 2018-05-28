package uinsgd.mcampus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Class DBDataSource sebagai penghubung antara aplikasi dengan database yang dibuat oleh Class
 * DBHelper.java
 */
public class DBDataSource {
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] allColumns_jadwal =
            {DBHelper.COLUMN_ID_1,
            DBHelper.COLUMN_MATKUL,
            DBHelper.COLUMN_NAME,
            DBHelper.COLUMN_JADWAL,
            DBHelper.COLUMN_JAM,
            DBHelper.COLUMN_RUANGAN};
    static String[] allColumns_tugas = {DBHelper.COLUMN_ID_2,
            DBHelper.COLUMN_MATKUL,
            DBHelper.COLUMN_TUGAS,
            DBHelper.COLUMN_SPIN_POS,
            DBHelper.COLUMN_DEADLINE,
            DBHelper.COLUMN_DESKRIPSI};

    DBDataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    void close() {
        dbHelper.close();
    }

    long createTugas(String namaMatkul, String tipeTugas, int spin_pos, String deadline,
                     String deskripsi) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_MATKUL, namaMatkul);
        values.put(DBHelper.COLUMN_TUGAS, tipeTugas);
        values.put(DBHelper.COLUMN_SPIN_POS, spin_pos);
        values.put(DBHelper.COLUMN_DEADLINE, deadline);
        values.put(DBHelper.COLUMN_DESKRIPSI, deskripsi);

        return database.insert(DBHelper.TABLE_NAME_2, null, values);
    }

    ArrayList<Tugas> getAllTugas() {
        ArrayList<Tugas> daftarTugas = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.TABLE_NAME_2, allColumns_tugas, null, null, null,
                null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Tugas tugas = cursorToTugas(cursor);
            daftarTugas.add(tugas);
            cursor.moveToNext();
        }
        cursor.close();

        return daftarTugas;
    }

    Cursor fetchAllTugas() {
        return database.query(DBHelper.TABLE_NAME_2, allColumns_tugas, null, null, null, null,
                null);
    }

    Cursor fetchTugas(long idTugas) throws SQLException {
        Cursor cursor = database.query(true, DBHelper.TABLE_NAME_2, allColumns_tugas,
                DBHelper.COLUMN_ID_2 + "=" + idTugas, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    private Tugas cursorToTugas(Cursor cursor) {
        return new Tugas(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4), cursor.getString(5));
    }

    Tugas getTugas(long idTugas) {
        Cursor cursor = database.query(DBHelper.TABLE_NAME_2, allColumns_tugas,
                "id_tugas = " + idTugas, null, null, null, null);

        cursor.moveToFirst();

        Tugas tugas = cursorToTugas(cursor);

        cursor.close();

        return tugas;
    }

    boolean updateTugas(Tugas tugas) {
        String strFilter = DBHelper.COLUMN_ID_2 + "=" + tugas.getIdTugas();
        ContentValues args = new ContentValues();

        args.put(DBHelper.COLUMN_MATKUL, tugas.getNamaMatkul());
        args.put(DBHelper.COLUMN_TUGAS, tugas.getJenisTugas());
        args.put(DBHelper.COLUMN_DEADLINE, tugas.getDeadline());
        args.put(DBHelper.COLUMN_DESKRIPSI, tugas.getDeskripsi());

        return database.update(DBHelper.TABLE_NAME_2, args, strFilter, null) > 0;
    }

    boolean deleteTugas(long id) {
        return database.delete(DBHelper.TABLE_NAME_2, DBHelper.COLUMN_ID_2 + "=" + id, null) > 0;
    }
}
