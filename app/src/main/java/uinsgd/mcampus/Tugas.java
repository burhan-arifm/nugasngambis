package uinsgd.mcampus;

class Tugas {
    private long idTugas;
    private String namaMatkul;
    private String jenisTugas;
    private int spin_pos;
    private String deadline;
    private String deskripsi;

    Tugas(long idTugas, String namaMatkul, String jenisTugas, int spin_pos, String deadline, String deskripsi) {
        this.idTugas = idTugas;
        this.namaMatkul = namaMatkul;
        this.jenisTugas = jenisTugas;
        this.spin_pos = spin_pos;
        this.deadline = deadline;
        this.deskripsi = deskripsi;
    }

    long getIdTugas() {
        return idTugas;
    }

    String getNamaMatkul() {
        return namaMatkul;
    }

    String getJenisTugas() {
        return jenisTugas;
    }

    String getDeadline() {
        return deadline;
    }

    String getDeskripsi() {
        return deskripsi;
    }

    int getSpin_pos() {
        return spin_pos;
    }
}
