package jp.winschool.spring.TrainSeatReservationApp;


import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jp.winschool.spring.TrainSeatReservationApp.Entity.ReservationDate;
import jp.winschool.spring.TrainSeatReservationApp.Service.ReservationServiceImpl;

@SpringBootApplication
public class TrainSeatReservationAppApplication implements ApplicationRunner{

	public static void main(String[] args) {
		SpringApplication.run(TrainSeatReservationAppApplication.class, args);
	}

	@Autowired(required = false)
	private ReservationServiceImpl reservationServiceImpl;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if(reservationServiceImpl == null) {
			return;
		}

//		//時刻表を作成
//		for(int i = 0; i < 8; i++) {
//			Time dateDepartureTime = Time.valueOf("9:00:00");
//			Calendar calendarDepartureTime = Calendar.getInstance();
//			calendarDepartureTime.setTime(dateDepartureTime);
//			ReservationDate reservationDate = new ReservationDate();
//
//			calendarDepartureTime.add(Calendar.HOUR, i); 
//			reservationDate.setA(new Time(calendarDepartureTime.getTime().getTime())); //9:00をセット
//			calendarDepartureTime.add(Calendar.MINUTE, 30); //9:30をセット
//			reservationDate.setB(new Time(calendarDepartureTime.getTime().getTime()));
//			calendarDepartureTime.add(Calendar.MINUTE, 30); //10:00をセット
//			reservationDate.setC(new Time(calendarDepartureTime.getTime().getTime()));
//			calendarDepartureTime.add(Calendar.MINUTE, 30); //10:30をセット
//			reservationDate.setD(new Time(calendarDepartureTime.getTime().getTime()));
//			calendarDepartureTime.add(Calendar.MINUTE, 30); //11:00をセット
//			reservationDate.setE(new Time(calendarDepartureTime.getTime().getTime()));
//			calendarDepartureTime.add(Calendar.MINUTE, 30); //11:30をセット
//
//			//時刻表をデータベースに保存
//			reservationServiceImpl.toFirstSave(reservationDate);
//		}

	}



}
