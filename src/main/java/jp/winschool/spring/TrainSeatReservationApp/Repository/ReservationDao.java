package jp.winschool.spring.TrainSeatReservationApp.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.winschool.spring.TrainSeatReservationApp.Entity.ReservationForm;


public interface ReservationDao extends JpaRepository<ReservationForm, Integer>{

}

