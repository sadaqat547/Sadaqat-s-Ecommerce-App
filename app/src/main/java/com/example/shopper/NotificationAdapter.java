package com.example.shopper;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder>{
    private List<NotificationModel> notificationModelList;
    private BottomSheetDialog notificationsDialog;

    public NotificationsAdapter(List<NotificationModel> notificationModelList,BottomSheetDialog notificationsDialog){
        this.notificationModelList = notificationModelList;
        this.notificationsDialog = notificationsDialog;

    }

    @NonNull
    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notifications_items,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ViewHolder viewHolder, int position) {

        String image = notificationModelList.get(position).getImage();
        String icon = notificationModelList.get(position).getIcon();
        String body = notificationModelList.get(position).getBody();
        Date date = notificationModelList.get(position).getDate();
        boolean readed = notificationModelList.get(position).isReaded();

        viewHolder.setData(image,icon,body,date,readed);
    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView imageView;
        private CircleImageView icon;
        private TextView textView;
        private TextView date;
        private ConstraintLayout notificationbackground;


        private CircleImageView dialogImage;
        private TextView dialogdata,dialogremoveNoti,dialogopenNoti,dialogreportnoti;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.notificationImage);
            icon = itemView.findViewById(R.id.notificationIcon);
            textView = itemView.findViewById(R.id.notificationsData);
            date = itemView.findViewById(R.id.notificationstime);
            notificationbackground = itemView.findViewById(R.id.notificationbackground);
////            dialogImage = notificationsDialog.findViewById(R.id.dialogImage);
//            dialogdata = notificationsDialog.findViewById(R.id.dialogdata);
//            dialogremoveNoti = notificationsDialog.findViewById(R.id.dialogremovenoti);
//            dialogopenNoti = notificationsDialog.findViewById(R.id.dialogopennoti);
//            dialogreportnoti = notificationsDialog.findViewById(R.id.dialogreportNoti);
////
        }

        private void setData(String image,String iconn,String body,Date datee,boolean readed){

            Glide.with(itemView.getContext()).load(image).into(imageView);
            Glide.with(itemView.getContext()).load(iconn).into(icon);
            textView.setText(body);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("  EEE,dd MMM yyyy hh:mm aa");

            date.setText(String.valueOf(simpleDateFormat.format(datee)));
            if (readed){
                notificationbackground.setBackgroundColor(itemView.getResources().getColor(R.color.white));
            }else {
                notificationbackground.setBackgroundColor(itemView.getResources().getColor(R.color.notificationsnotread));

            }

//            dialogdata.setText(body);
//            dialogremoveNoti.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(itemView.getContext(), "Pending work", Toast.LENGTH_SHORT).show();
//                }
//            });
//            dialogopenNoti.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(itemView.getContext(), "Pending work", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//            dialogreportnoti.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(itemView.getContext(), "Pending work", Toast.LENGTH_SHORT).show();
//
//                }
//            });





        }
    }
}
