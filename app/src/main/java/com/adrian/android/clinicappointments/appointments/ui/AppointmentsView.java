package com.adrian.android.clinicappointments.appointments.ui;

import com.adrian.android.clinicappointments.entities.Appointment;

/**
 * Created by adrian on 6/07/16.
 */
public interface AppointmentsView {
    void onAppointmentAdded(Appointment appointment);

    void onAppointmentRemoved(Appointment appointment);

    void onAppointmentChanged(Appointment appointment);

    void onIncDate();

    void onDecDate();

    void onPickDate();

    void hideList();

    void showProgress();

    void showList();

    void hideProgress();

    void onAppointmentError(String error);

    void onDateChanged(Appointment appointment);

}
