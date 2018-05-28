package uinsgd.mcampus;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Class DBHelper digunakan ketika akan membuat database internal aplikasi yang berbasis SQLite.
 * class ini membuat dua tabel yang berfungsi untuk menyinpan data-data yang diperlukan oleh
 * aplikasi, dalam hal ini yaitu jadwal perkuliahan dan pengingat tugas.
 */
class DBHelper extends SQLiteOpenHelper {
    static final String TABLE_NAME_1 = "jadwal_kuliah";
    static final String COLUMN_ID_1 = "kode_matkul";
    static final String TABLE_NAME_2 = "tugas";
    static final String COLUMN_ID_2 = "id_tugas";
    static final String COLUMN_MATKUL = "nama_matkul";
    static final String COLUMN_NAME = "nama_dosen";
    static final String COLUMN_JADWAL = "hari_kuliah";
    static final String COLUMN_JAM = "jam_kuliah";
    static final String COLUMN_RUANGAN = "ruangan";
    static final String COLUMN_TUGAS = "jenis_tugas";
    static final String COLUMN_SPIN_POS = "spin_pos";
    static final String COLUMN_DEADLINE = "deadline_tugas";
    static final String COLUMN_DESKRIPSI = "deskripsi_tugas";
    private static final String db_name = "apps_database.db";
    private static final int db_version = 1;
    private static final String table1_create = "create table "
            + TABLE_NAME_1 + " ("
            + COLUMN_ID_1 + " varchar(10) primary key, "
            + COLUMN_MATKUL + " varchar(25) not null, "
            + COLUMN_NAME + " varchar(40) not null, "
            + COLUMN_JADWAL + " varchar(10) not null, "
            + COLUMN_JAM + " time not null, "
            + COLUMN_RUANGAN + " varchar(10));";
    private static final String table2_create = "create table "
            + TABLE_NAME_2 + " ("
            + COLUMN_ID_2 + " integer primary key autoincrement, "
            + COLUMN_MATKUL + " varchar(25) not null, "
            + COLUMN_TUGAS + " varchar(10) not null, "
            + COLUMN_SPIN_POS + " integer not null, "
            + COLUMN_DEADLINE + " text not null, "
            + COLUMN_DESKRIPSI + " text, "
            + "foreign key(" + COLUMN_MATKUL + ") references " + TABLE_NAME_1 + "(" + COLUMN_MATKUL + "));";

    DBHelper(Context context) {
        super(context, db_name, null, db_version);
    }

    /**
     * method untuk membuat database
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(table1_create);
        db.execSQL(table2_create);
    }

    /**
     * method untuk upgrade database
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(), "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data.");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        onCreate(db);
    }
}
