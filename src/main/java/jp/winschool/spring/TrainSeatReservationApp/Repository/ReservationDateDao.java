package jp.winschool.spring.TrainSeatReservationApp.Repository;

import java.sql.Time;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.winschool.spring.TrainSeatReservationApp.Entity.ReservationDate;

public interface ReservationDateDao extends JpaRepository<ReservationDate, Integer>{
	List<ReservationDate> findTop2ByAGreaterThan(Time time);
	List<ReservationDate> findTop2ByBGreaterThan(Time time);
	List<ReservationDate> findTop2ByCGreaterThan(Time time);
	List<ReservationDate> findTop2ByDGreaterThan(Time time);
	List<ReservationDate> findTop2ByEGreaterThan(Time time);
}
