package uinsgd.mcampus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

class TugasAdapter extends RecyclerView.Adapter<TugasAdapter.TugasHolder> {
    private List<Tugas> tugas;
    public Context context;

    static class TugasHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView matkul;
        TextView jenisTugas;
        TextView deadline;
        ImageButton imageButton_popup;

        TugasHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_tugas);
            matkul = (TextView) itemView.findViewById(R.id.daftar_matkul_tugas);
            jenisTugas = (TextView) itemView.findViewById(R.id.daftar_jenis_tugas);
            deadline = (TextView) itemView.findViewById(R.id.daftar_deadline);
            imageButton_popup = (ImageButton) itemView.findViewById(R.id.image_button_popup);
        }
    }

    TugasAdapter(Context context, List<Tugas> tugas) {
        this.context = context;
        this.tugas = tugas;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public TugasHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_tugas, viewGroup, false);

        return new TugasHolder(view);
    }

    @Override
    public void onBindViewHolder(TugasHolder tugasHolder, int position) {
        Calendar calendar = Calendar.getInstance();
        tugasHolder.cardView.setTag(tugasHolder);
        String[] datetime = tugas.get(position).getDeadline().split(" ");
        String[] tanggal = datetime[0].split("-");
        String[] jam = datetime[1].split(":");
        int[] dated = new int[tanggal.length];
        int[] timed = new int[jam.length];
        for (int i = 0; i < dated.length; i++) {
            dated[i] = Integer.valueOf(tanggal[i]);
        }
        for (int i = 0; i < timed.length; i++) {
            timed[i] = Integer.valueOf(jam[i]);
        }
        calendar.set(dated[0], dated[1] - 1, dated[2], timed[0], timed[1]);
        tugasHolder.matkul.setText(tugas.get(position).getNamaMatkul());
        tugasHolder.jenisTugas.setText(tugas.get(position).getJenisTugas());
        tugasHolder.deadline.setText(new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm",
                new Locale("id")).format(calendar.getTime()));
//        tugasHolder.deadline.setText(String.valueOf(dated[1]));
        tugasHolder.matkul.setTag(tugasHolder);
        tugasHolder.jenisTugas.setTag(tugasHolder);
        tugasHolder.deadline.setTag(tugasHolder);
        tugasHolder.imageButton_popup.setTag(tugasHolder);
        tugasHolder.imageButton_popup.setOnClickListener(popupClickListener);
        tugasHolder.matkul.setOnClickListener(clickListener);
        tugasHolder.jenisTugas.setOnClickListener(clickListener);
        tugasHolder.deadline.setOnClickListener(clickListener);
        tugasHolder.cardView.setOnClickListener(clickListener);
    }

    private View.OnClickListener popupClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TugasHolder tugasHolder = (TugasHolder) v.getTag();
            showPopUpMenu(tugasHolder.imageButton_popup, tugasHolder.getAdapterPosition());
        }
    };
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TugasHolder tugasHolder = (TugasHolder) v.getTag();
            int position = tugasHolder.getAdapterPosition();
            long idTugas = tugas.get(position).getIdTugas();
            DBDataSource dataSource = new DBDataSource(v.getContext());

            dataSource.open();

            Tugas tugas = dataSource.getTugas(idTugas);
            Intent intent = new Intent(context, LihatTugasActivity.class);
            Bundle bundle = new Bundle();

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            bundle.putLong("idTugas", idTugas);
            bundle.putString("namaMatkul", tugas.getNamaMatkul());
            bundle.putString("jenisTugas", tugas.getJenisTugas());
            bundle.putInt("spin_pos", tugas.getSpin_pos());
            bundle.putString("deadline", tugas.getDeadline());
            bundle.putString("deskripsi", tugas.getDeskripsi());

            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    };

    public int getItemCount() {
        return tugas.size();
    }

    private void showPopUpMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();

        menuInflater.inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new MenuItemClickListener(position, context, tugas));
        popupMenu.show();
    }
}
