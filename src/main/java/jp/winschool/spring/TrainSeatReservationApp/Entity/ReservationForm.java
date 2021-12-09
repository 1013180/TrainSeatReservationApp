package jp.winschool.spring.TrainSeatReservationApp.Entity;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class ReservationForm {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private Date date;
	
	private Time time;
	
	private String RideStation;
	
	private String DropOffStation;
	
	private String timeStr;
	
	private String seat;

	
	public void timeToString() {
		String str = this.time.toString().substring(0, 5);
		timeStr = str;
	}

}
