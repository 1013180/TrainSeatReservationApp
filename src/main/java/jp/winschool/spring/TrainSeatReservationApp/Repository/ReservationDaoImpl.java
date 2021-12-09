package jp.winschool.spring.TrainSeatReservationApp.Repository;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import jp.winschool.spring.TrainSeatReservationApp.Entity.ReservationDate;
import jp.winschool.spring.TrainSeatReservationApp.Entity.ReservationForm;

@Repository
public class ReservationDaoImpl {
	
	
	private ReservationDao dao;
	private ReservationDateDao dateDao;
	
	@Autowired
	public ReservationDaoImpl(@Lazy ReservationDao dao, @Lazy ReservationDateDao dateDao) {
		this.dao = dao;
		this.dateDao = dateDao;
	}
		
	public List<Time> findByRideStation(String trainId, Time time){
		
		List<ReservationDate> rideTime = null;
		List<Time> rideTimes = new ArrayList<>();
		switch(trainId) {
		case "a":
			rideTime = dateDao.findTop2ByAGreaterThan(time);
			for(ReservationDate rd : rideTime) {
				rideTimes.add(rd.getA());
			}
			break;
		case "b":
			rideTime = dateDao.findTop2ByAGreaterThan(time);
			for(ReservationDate rd : rideTime) {
				rideTimes.add(rd.getB());
			}
			break;
		case "c":
			rideTime = dateDao.findTop2ByAGreaterThan(time);
			for(ReservationDate rd : rideTime) {
				rideTimes.add(rd.getC());
			}
			break;
		case "d":
			rideTime = dateDao.findTop2ByAGreaterThan(time);
			for(ReservationDate rd : rideTime) {
				rideTimes.add(rd.getD());
			}
			break;
		case "e":
			rideTime = dateDao.findTop2ByAGreaterThan(time);
			for(ReservationDate rd : rideTime) {
				rideTimes.add(rd.getE());
			}
			break;
		
		}
		return rideTimes;
	}
	
//	public List<ReservationForm> findAll(){
//		List<ReservationForm> reservationForms = dao.findAll();
//		return reservationForms;
//	}
	
	public void save(ReservationForm reservationForm) {
		dao.save(reservationForm);
	}
	
	public void deleteById(int id) {
		dao.deleteById(id);
	}
	public void toFirstSave(ReservationDate reservationDate) {
		dateDao.save(reservationDate);
	}
	
}
