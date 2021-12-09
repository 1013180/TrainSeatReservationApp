package jp.winschool.spring.TrainSeatReservationApp.Service;

import java.util.ArrayList;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import jp.winschool.spring.TrainSeatReservationApp.Entity.ReservationDate;
import jp.winschool.spring.TrainSeatReservationApp.Entity.ReservationForm;
import jp.winschool.spring.TrainSeatReservationApp.Repository.ReservationDao;
import jp.winschool.spring.TrainSeatReservationApp.Repository.ReservationDaoImpl;

@Service
public class ReservationServiceImpl {

	@Autowired
	private ReservationDaoImpl daoImpl;
	
	@Autowired
	private ReservationDao reservationDao;

	public ReservationServiceImpl(@Lazy ReservationDaoImpl daoImpl) {
		this.daoImpl = daoImpl;
	}

	public List<String> searchRideTime(String trainId, Time time) {
		List<Time> rideTime = daoImpl.findByRideStation(trainId, time);
		
		List<String> rideTimeStr = new ArrayList<String>();
		for(Time times : rideTime) {
			rideTimeStr.add(times.toString().substring(0, 5));
		}
		
		return rideTimeStr;
	}

	public List<ReservationForm> searchReservation() {
		List<ReservationForm> reservation = reservationDao.findAll();
		return reservation;
	}

	public void reservation(ReservationForm reservationForm) {
		daoImpl.save(reservationForm);
	}

	public void deleteReservation(int id) {
		daoImpl.deleteById(id);
	}

	public void toFirstSave(ReservationDate reservationDate) {
		daoImpl.toFirstSave(reservationDate);
	}
	
	public Optional<ReservationForm> getCanselReservation(int id) {
		Optional<ReservationForm> reservationForm = reservationDao.findById(id);
		return reservationForm;
	}
	
	//ここから下はヘルパーメソッド
	public int changeRideStationName(String StationName) {
		switch (StationName){
		case "新宿":
			return 1;
		case "箱根湯本":
			return 2;
		case "品川":
			return 3;
		case "渋谷":
			return 4;
		case "原宿":
			return 5;
		default:
			return 0;
		}
	}
	
	public int selectedRideStation(int RideTrainId, int dropOffTrainId) {
		int keyDecideRideStation = RideTrainId - dropOffTrainId;
		if(keyDecideRideStation < 0) {
			return RideTrainId;
		}else {
			return dropOffTrainId;
		}
	}
	
	public String changeCharId(int raidTrainId) {
		switch(raidTrainId) {
		case 1:
			return "a";
		case 2:
			return "b";
		case 3:
			return "c";
		case 4:
			return "d";
		case 5:
			return "e";
		default:
			return "E";
		}
	}
	
	public Date changeFormatDate(String date) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date dateUtil = dateFormat.parse(date);
		long longTime = dateUtil.getTime();
		Date date2 = new Date(longTime);
		return date2;
	}
	
	public Time changeFormatTime(String time) throws ParseException {
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm");
		java.util.Date dateUtil2 = dateFormat2.parse(time);
		long longTime2 = dateUtil2.getTime();
		Time time2 = new Time(longTime2);
		return time2;
	}
}
