package jp.winschool.spring.TrainSeatReservationApp.Entity;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import lombok.Data;

@Entity
@Data
public class ReservationDate {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer TrainId;
	
	private Time a;
	
	private Time b;
	
	private Time c;
	
	private Time d;
	
	private Time e;
	
}