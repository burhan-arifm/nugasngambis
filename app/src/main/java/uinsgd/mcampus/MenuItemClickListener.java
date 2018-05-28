package uinsgd.mcampus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

class MenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
    private int position;
    private List<Tugas> tugasList;
    private DBDataSource dataSource;
    public Context context;

    MenuItemClickListener(int position, Context context, List<Tugas> tugasList) {
        dataSource = new DBDataSource(context);
        dataSource.open();
        this.position = position;
        this.tugasList = tugasList;
        this.context = context;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.popup_edit:
                switchToEdit(tugasList.get(position).getIdTugas());
                break;
            case R.id.popup_delete:
                dataSource.deleteTugas(tugasList.get(position).getIdTugas());
                Toast.makeText(context, "Tugas pada posisi ke-" + (position + 1 ) + " berhasil dihapus.", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return false;
    }

    private void switchToEdit(long idTugas) {
        Tugas tugas = dataSource.getTugas(idTugas);
        Intent intent = new Intent(context, TugasEditActivity.class);
        Bundle bundle = new Bundle();

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle.putLong("idTugas", idTugas);
        bundle.putString("namaMatkul", tugas.getNamaMatkul());
        bundle.putInt("spin_pos", tugas.getSpin_pos());
        bundle.putString("deadline", tugas.getDeadline());
        bundle.putString("deskripsi", tugas.getDeskripsi());

        intent.putExtras(bundle);
        dataSource.close();
        context.startActivity(intent);
    }
}
