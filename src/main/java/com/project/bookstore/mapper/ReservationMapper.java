package com.project.bookstore.mapper;

import com.project.bookstore.dto.ReservationDto;
import com.project.bookstore.entity.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReservationMapper {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BookExemplarMapper bookExemplarMapper;

    public Reservation mapReservationFromReservationDto(ReservationDto reservationDto) {
        Reservation reservation = new Reservation();
        reservation.setStartDate(reservationDto.getStartDate());
        reservation.setEndDate(reservationDto.getEndDate());
        return reservation;
    }

    public ReservationDto mapReservationDtoFromReservation(Reservation reservation) {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(reservation.getId());
        reservationDto.setStartDate(reservation.getStartDate());
        reservationDto.setEndDate(reservation.getEndDate());
        reservationDto.setBookExemplar(bookExemplarMapper.mapBookExemplarDtoFromBookExemplar(reservation.getReservedExemplar()));
        reservationDto.setUser(userMapper.mapUserDtoFromUser(reservation.getReservedUser()));
        reservationDto.setReservationStatus(reservation.getReservationStatus());
        return reservationDto;
    }

    public List<Reservation> mapReservationListFromReservationDtoList(List<ReservationDto> reservationDtoList) {
        return reservationDtoList.stream()
                .map(this::mapReservationFromReservationDto)
                .toList();
    }

    public List<ReservationDto> mapReservationDtoListFromReservationList(List<Reservation> reservationsList) {
        return reservationsList.stream()
                .map(this::mapReservationDtoFromReservation)
                .toList();
    }

}
