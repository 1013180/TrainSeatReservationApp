package jp.winschool.spring.TrainSeatReservationApp.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.winschool.spring.TrainSeatReservationApp.Entity.ReservationForm;

public interface ReservationService {
	List<Time> searchRideTime(String station, Date date);
	List<ReservationForm> searchReservation();
	void reservation(ReservationForm reservationForm);
	void deleteReservation(int id);

}
