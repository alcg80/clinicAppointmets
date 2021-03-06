package com.adrian.android.clinicappointments.appointments.ui.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adrian.android.clinicappointments.R;
import com.adrian.android.clinicappointments.domain.Util;
import com.adrian.android.clinicappointments.entities.Appointment;
import com.adrian.android.clinicappointments.libs.base.ImageLoader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by adrian on 6/07/16.
 */
public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {

    Util util;
    List<Appointment> appointments;
    ImageLoader imageLoader;
    OnItemClickListener onItemClickListener;


    public AppointmentsAdapter(Util util, List<Appointment> appointments, ImageLoader
            imageLoader, OnItemClickListener
                                       onItemClickListener) {
        this.util = util;
        this.imageLoader = imageLoader;
        this.appointments = appointments;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .content_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        hideAddress(holder);
        hideMap(holder);
        hideMapProgress(holder);

        Appointment appointment = appointments.get(position);

        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String timeStr = timeFormat.format(appointment.getInitDate());
        holder.txtHour.setText(timeStr);
        holder.txtPatient.setText(appointment.getPatient().getPatient());

        if (appointment.getLatitude() != null && appointment.getLongitude() != null) {
            showMapProgress(holder);
            String address = util.getFromLocation(Double.parseDouble(appointment.getLatitude()),
                    Double.parseDouble
                            (appointment.getLongitude()));
            holder.txtAddress.setText(address);
            String url = util.getStaticMapURL(address);
            imageLoader.load(holder.staticMapImg, url);
            hideMapProgress(holder);
            showAddress(holder);
            showMap(holder);
        }
        holder.setOnItemClickListener(appointment, this.onItemClickListener);

    }

    private void hideMapProgress(ViewHolder holder) {
        holder.mapProgress.setVisibility(View.GONE);

    }

    private void showMapProgress(ViewHolder holder) {
        holder.mapProgress.setVisibility(View.VISIBLE);
    }

    private void hideMap(ViewHolder holder) {
        holder.cardViewMap.setVisibility(View.GONE);
    }

    private void showMap(ViewHolder holder) {
        holder.cardViewMap.setVisibility(View.VISIBLE);
    }

    private void hideAddress(ViewHolder holder) {
        holder.txtAddress.setVisibility(View.GONE);
    }

    private void showAddress(ViewHolder holder) {
        holder.txtAddress.setVisibility(View.VISIBLE);
    }


    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public void addAppointment(Appointment appointment) {
        if (getAppointmentById(appointment.getId()) == null) {
            appointments.add(appointment);
//            sortAppointmentsByDate();
            notifyDataSetChanged();
        }
    }

    private void sortAppointmentsByDate() {
        Collections.sort(appointments, new Comparator<Appointment>() {
            @Override
            public int compare(Appointment lhs, Appointment rhs) {
                return lhs.compareTo(rhs);
            }
        });
    }

    public void removeAppointment(Appointment appointment) {
        appointments.remove(getAppointmentById(appointment.getId()));
        notifyDataSetChanged();
    }

    private Appointment getAppointmentById(String id) {
        Appointment appointment = null;
        for (Appointment item : appointments) {
            if (item.getId().equals(id)) {
                appointment = item;
            }
        }
        return appointment;
    }


    public void modifyAppointment(Appointment appointment) {
        Appointment appment = getAppointmentById(appointment.getId());
        if (appment != null) {
            appment.setPatient(appointment.getPatient());
            appment.setLongitude(appointment.getLongitude());
            appment.setLatitude(appointment.getLatitude());
            appment.setInitDate(appointment.getInitDate());
            appment.setEndDate(appointment.getEndDate());
        }
        notifyDataSetChanged();
    }

    public void clearAppointments() {
        appointments.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txtHour)
        TextView txtHour;
        @Bind(R.id.txtPatient)
        TextView txtPatient;
        @Bind(R.id.layoutPatient)
        LinearLayout layoutPatient;
        @Bind(R.id.imgEdit)
        ImageButton imgEdit;
        @Bind(R.id.imgDelete)
        ImageButton imgDelete;
        @Bind(R.id.layoutButtons)
        LinearLayout layoutButtons;
        @Bind(R.id.txtAddress)
        TextView txtAddress;
        @Bind(R.id.staticMapImg)
        ImageView staticMapImg;
        @Bind(R.id.cardViewMap)
        CardView cardViewMap;
        @Bind(R.id.mapProgress)
        ProgressBar mapProgress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setOnItemClickListener(final Appointment appointment, final
        OnItemClickListener listener) {
            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEditClick(appointment);
                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteClick(appointment);
                }
            });

            txtAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPlaceClick(appointment);
                }
            });

            staticMapImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPlaceClick(appointment);
                }
            });
        }

    }
}
