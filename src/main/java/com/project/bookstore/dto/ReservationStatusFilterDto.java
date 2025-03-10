package com.project.bookstore.dto;

import com.project.bookstore.entity.types.ReservationStatus;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationStatusFilterDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<ReservationStatus> reservationStatusList = new ArrayList<>();

    public List<ReservationStatus> getReservationStatusList() {
        return reservationStatusList;
    }

    public void setReservationStatusList(List<ReservationStatus> reservationStatusList) {
        this.reservationStatusList = reservationStatusList;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
