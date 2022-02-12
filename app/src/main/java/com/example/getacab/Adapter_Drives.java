package com.example.getacab;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;


class Adapter_Drive<driveItemClickListener> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<Drive> drives = new ArrayList<>();
    private DriveItemClickListener driveItemClickListener;

    public Adapter_Drive(Activity activity, ArrayList<Drive> drives) {
        this.activity = activity;
        this.drives = drives;
    }


    public Adapter_Drive setDriveItemClickListener(DriveItemClickListener driveItemClickListener) {
        this.driveItemClickListener = driveItemClickListener;
        return this;
    }

    @Override
    public RecyclerView.ViewHolder
    onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_drive, viewGroup, false);
        return new DriveViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        DriveViewHolder driveViewHolder = (DriveViewHolder) holder;
        Drive drive = getItem(position);
        driveViewHolder.drive_LBL_name.setText(drive.getName());
        driveViewHolder.drive_LBL_phone.setText(drive.getPhone());
        driveViewHolder.drive_LBL_fCity.setText(drive.getfCity());
        driveViewHolder.drive_LBL_fStreet.setText(drive.getfStreet());
        driveViewHolder.drive_LBL_eCity.setText(drive.geteCity());
        driveViewHolder.drive_LBL_eStreet.setText(drive.geteStreet());
        driveViewHolder.drive_LBL_cost.setText(drive.getCost());
    }

    @Override
    public int getItemCount() {
        if (drives==null){
            return 0;
        }
        return drives.size();
    }

    private Drive getItem(int position) {
        return drives.get(position);
    }

    public interface DriveItemClickListener {
        void driveItemClicked();
    }

    public class DriveViewHolder extends RecyclerView.ViewHolder {
        public MaterialTextView drive_LBL_name;
        public MaterialTextView drive_LBL_phone;
        public MaterialTextView drive_LBL_fCity;
        public MaterialTextView drive_LBL_fStreet;
        public MaterialTextView drive_LBL_eCity;
        public MaterialTextView drive_LBL_eStreet;
        public MaterialTextView drive_LBL_cost;


        public DriveViewHolder(final View itemView) {
            super(itemView);
            this.drive_LBL_name = itemView.findViewById(R.id.drive_LBL_name);
            this.drive_LBL_phone = itemView.findViewById(R.id.drive_LBL_phone);
            this.drive_LBL_fCity = itemView.findViewById(R.id.drive_LBL_fCity);
            this.drive_LBL_fStreet = itemView.findViewById(R.id.drive_LBL_fStreet);
            this.drive_LBL_eCity = itemView.findViewById(R.id.drive_LBL_eCity);
            this.drive_LBL_eStreet = itemView.findViewById(R.id.drive_LBL_eStreet);
            this.drive_LBL_cost = itemView.findViewById(R.id.drive_LBL_cost);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d("pttt", "position= " + getItem(getAdapterPosition()).getLot()+getItem(getAdapterPosition()).getLot());

//                    driveItemClickListener.driveItemClicked(getItem(getAdapterPosition()), getItem(getAdapterPosition()).geteCity(),getItem(getAdapterPosition()).geteStreet());
                }
            });


        }
    }
}

