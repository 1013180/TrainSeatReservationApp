package jp.winschool.spring.TrainSeatReservationApp.Controller;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.winschool.spring.TrainSeatReservationApp.Entity.ReservationForm;
import jp.winschool.spring.TrainSeatReservationApp.Service.ReservationServiceImpl;

@Controller
public class ReservationController {
	
	private ReservationServiceImpl reservationService;
	
	@Autowired
	public ReservationController(@Lazy ReservationServiceImpl reservationService) {
		this.reservationService = reservationService;
	}
	
	//駅名のList
	private List<String> stationList = Arrays.asList(
			"新宿","箱根湯本","品川","渋谷","原宿"
			);
	
	//予約情報の一時保存用のフィールド
	private String rideStation = null;
	private String dropOffStation = null;
	private Date date = null;
	private Time time = null;
	private String seat = "自由席";
	private String outwardTripSeat = "自由席";
	//復路用のフィールド
	private Date returnTripDate = null;
	private Time returnTripTime = null;
	private String returnSeat = "自由席";
	
	private String error = "";
	
	
	
	//（片道）検索のトップページ。検索条件を選択したりできる画面を表示する。
	@GetMapping("/")
	public String indexOneWay(Model model) {
		
		//フィールドの初期化
		this.rideStation = null;
		this.dropOffStation = null;
		this.date = null;
		this.time = null;
		this.seat = "自由席";
		this.outwardTripSeat = "自由席";
		
		//復路用のフィールド
		this.returnTripDate = null;
		this.returnTripTime = null;
		this.returnSeat = "自由席";
		
		List<String> stationList = this.stationList;
		model.addAttribute("stationList", stationList);
		model.addAttribute("error", error);
		
		this.error = "";
		
		return "search";
	}
	
	//（往復）検索のトップページ。
	@GetMapping("/roundTripSearch")
	public String indexRoundTrip(Model model) {
		List<String> stationList = this.stationList;
		model.addAttribute("stationList", stationList);
		
		return "roundTripSearch";
	}
	
	
	
	//（片道）検索ボタンを押した時に処理されるハンドラ。検索結果の表示画面に移動する。
	@PostMapping("/result")
	public String searchOneWay(@RequestParam(name = "date", defaultValue = "Re" ) String date, 
			@RequestParam(name = "time", defaultValue = "Re" ) String time, 
			@RequestParam(name = "rideStation", defaultValue = "Re" ) String rideStation, 
			@RequestParam(name = "dropOffStation", defaultValue = "Re" ) String dropOffStation, 
			@RequestParam(name = "options", defaultValue = "自由席" ) String options, 
			Model model) throws ParseException {
		
		if(date.equals("Re")) {
			date = this.date.toString();
			time = this.time.toString();
			rideStation = this.rideStation;
			dropOffStation = this.dropOffStation;
		}else {
			if(rideStation.equals(dropOffStation)) {
				this.error = "乗車駅と降車駅が同じです";
				return "redirect:/";
			}
		}
		
		//指定席処理
		if(options == null) {
			this.seat= "自由席";
		}else {
			this.seat = options;
		}
		
		int RideTrainId = reservationService.changeRideStationName(rideStation);
		int dropOffTrainId = reservationService.changeRideStationName(dropOffStation);
		int trainId = reservationService.selectedRideStation(RideTrainId, dropOffTrainId);
		String raidCharId = reservationService.changeCharId(trainId);
		
		Date changedDate = reservationService.changeFormatDate(date);
		Time changedTime = reservationService.changeFormatTime(time);
		
		List<String> searchedList = reservationService.searchRideTime(raidCharId, changedTime);
		if(searchedList.size() == 0) {
			this.error = ("選択した時間以降で出発する電車がありません。");
			return "redirect:/";
		}
		model.addAttribute("searchedList", searchedList);
		model.addAttribute("rideStation", rideStation);
		model.addAttribute("dropOffStation", dropOffStation);
		model.addAttribute("time", changedTime.toString().substring(0, 5));
		model.addAttribute("seat", this.seat);
		
		this.rideStation = rideStation;
		this.dropOffStation = dropOffStation;
		this.date = changedDate;
		this.time = reservationService.changeFormatTime(time);
		
		return "result";
	}
	
	//（往復）検索ボタンを押した時に処理されるハンドラ。検索結果の表示画面に移動する。
	@PostMapping("/roundTripResult")
	public String searchRoundTrip(@RequestParam(name = "toDate", defaultValue = "Re" ) String toDate,
			@RequestParam(name = "toTime", defaultValue = "Re" ) String toTime,
			@RequestParam(name = "rideStation", defaultValue = "Re" ) String rideStation,
			@RequestParam(name = "dropOffStation", defaultValue = "Re" ) String dropOffStation,
			@RequestParam(name = "backDate", defaultValue = "Re" ) String backDate,
			@RequestParam(name = "backTime", defaultValue = "Re" ) String backTime,
			@RequestParam String root, 
			Model model) throws ParseException {
		
		//往路か復路かの判断
		model.addAttribute("root", root);
		
		//2回目に訪れた時の処理
		if(toDate.equals("Re")) {
			toDate = this.date.toString();
			toTime = this.time.toString();
			rideStation = this.rideStation;
			dropOffStation = this.dropOffStation;
			backDate = this.returnTripDate.toString();
			backTime = this.returnTripTime.toString();
		}else {
			if(rideStation.equals(dropOffStation)) {
				this.error = "乗車駅と降車駅が同じです";
				return "redirect:/";
			}
		}
		
		//往路の処理
		int RideTrainId = reservationService.changeRideStationName(rideStation);
		int dropOffTrainId = reservationService.changeRideStationName(dropOffStation);
		int trainId = reservationService.selectedRideStation(RideTrainId, dropOffTrainId);
		String raidCharId = reservationService.changeCharId(trainId);
		
		Date changedDate = reservationService.changeFormatDate(toDate);
		Time changedTime = reservationService.changeFormatTime(toTime);
		
		Time selectedTime;
		if(this.time != null) {
			selectedTime = this.time;
		}else {
			selectedTime = changedTime;
		}
		
		List<String> searchedList = reservationService.searchRideTime(raidCharId, changedTime);
		if(searchedList.size() == 0) {
			this.error = ("選択した時間以降で出発する往路の電車がありません。");
			return "redirect:/";
		}
		model.addAttribute("searchedList", searchedList);
		model.addAttribute("rideStation", rideStation);
		model.addAttribute("dropOffStation", dropOffStation);
		model.addAttribute("time", changedTime.toString().substring(0, 5));
		model.addAttribute("outwardTripSeat", this.outwardTripSeat);
		model.addAttribute("selectedTime", selectedTime);
		
		this.rideStation = rideStation;
		this.dropOffStation = dropOffStation;
		this.date = changedDate;
		this.time = reservationService.changeFormatTime(toTime);
		
		//復路の処理
		int returnTripRideTrainId = reservationService.changeRideStationName(dropOffStation);
		int returnTripDropOffTrainId = reservationService.changeRideStationName(rideStation);
		int returnTripTrainId = reservationService.selectedRideStation(returnTripRideTrainId, returnTripDropOffTrainId);
		String returnTripRaidCharId = reservationService.changeCharId(returnTripTrainId);
		
		Date returnTripChangedDate = reservationService.changeFormatDate(backDate);
		Time returnTripChangedTime = reservationService.changeFormatTime(backTime);
		
		Time returnSelectedTime;
		if(this.returnTripTime != null) {
			returnSelectedTime = this.returnTripTime;
		}else {
			returnSelectedTime = returnTripChangedTime;
		}
		
		List<String> returnTripSearchedList = reservationService.searchRideTime(returnTripRaidCharId, returnTripChangedTime);
		if(returnTripSearchedList.size() == 0) {
			this.error = ("選択した時間以降で出発する復路の電車がありません。");
			return "redirect:/";
		}
		model.addAttribute("returnTripSearchedList", returnTripSearchedList);
		model.addAttribute("returnTripRideStation", dropOffStation);
		model.addAttribute("returnTripDropOffStation", rideStation);
		model.addAttribute("returnTripTime", returnTripChangedTime.toString().substring(0, 5));
		model.addAttribute("returnTripSeat", this.returnSeat);
		model.addAttribute("returnSelectedTime", returnSelectedTime);
		
		this.returnTripDate = returnTripChangedDate;
		this.returnTripTime = reservationService.changeFormatTime(backTime);
		
		return "roundTripResult";
	}
	
	
	
	//指定席選択画面
	@GetMapping("/reservedSeat")
	public String reservedSeat(@RequestParam String root, 
			@RequestParam(name = "date", required = false ) String date,
			@RequestParam(name = "returnTripDate", required = false ) String returnTripDate,
			Model model) throws ParseException {
		
		if(date != null) {
			this.time = reservationService.changeFormatTime(date);
		}
		if(returnTripDate != null) {
			this.returnTripTime = reservationService.changeFormatTime(returnTripDate);
		}
		
		model.addAttribute("root", root);
		
		return "reservedSeat";
	}
	
	
	//指定席選択画面で「決定」ボタンを押した後の画面
	@PostMapping("/confirmedReservedSeat")
	public String confirmedReservedSeat(@RequestParam String root, @RequestParam String options) {
		if(root.equals("outwardTrip") || root.equals("returnTrip")) {
			//指定席処理
			if(root.equals("outwardTrip")) {
				if(options != null) {
					this.outwardTripSeat = options;
				}
			}
			if(root.equals("returnTrip")) {
				if(options != null) {
					this.returnSeat = options;
				}
			}
			return "forward:/roundTripResult";
		}else if(root.equals("oneWay")){
			this.seat = options;
			return "forward:/result";
		}else {
			return "search";
		}
	}
	
	//（片道）検索結果画面で予約ボタンを押した時に処理されるハンドラ。予約する情報の確認をする。
	@PostMapping("/confirmation")
	public String confirmation(@RequestParam(name = "date", required = false ) String date,  Model model) throws ParseException {
		
		model.addAttribute("rideStation", this.rideStation);
		model.addAttribute("dropOffStation", this.dropOffStation);
		model.addAttribute("time", date);
		model.addAttribute("date", this.date);
		model.addAttribute("seat", this.seat);
		
		if(date != null) {
			this.time = reservationService.changeFormatTime(date);
		}
		
		return "confirmation";
	}
	
	//（往復）検索結果画面で予約ボタンを押した時に処理されるハンドラ。予約する情報の確認をする。
	@PostMapping("/roundTripConfirmation")
	public String roundTripConfirmation(@RequestParam(name = "date", required = false ) String date, 
			@RequestParam(name = "returnTripDate", required = false ) String returnTripDate,  
			Model model) throws ParseException {
		
		//往路
		model.addAttribute("rideStation", this.rideStation);
		model.addAttribute("dropOffStation", this.dropOffStation);
		model.addAttribute("time", date);
		model.addAttribute("date", this.date);
		model.addAttribute("seat", this.outwardTripSeat);
		
		//復路
		model.addAttribute("returnTripRideStation", this.dropOffStation);
		model.addAttribute("returnTripDropOffStation", this.rideStation);
		model.addAttribute("returnTripTime", returnTripDate);
		model.addAttribute("returnTripDate", this.returnTripDate);
		model.addAttribute("returnSeat", this.returnSeat);
		
		if(date != null) {
			this.time = reservationService.changeFormatTime(date);
		}
		if(returnTripDate != null) {
			this.returnTripTime = reservationService.changeFormatTime(returnTripDate);
		}
		
		return "roundTripConfirmation";
	}
		
	
	
	
	//（片道）予約OKボタンを押した時に処理されるハンドラ。予約の実行をする。そして検索画面に移動する。
	@PostMapping("/reserve")
	public String reserveOneWay() {
		ReservationForm reservationForm = new ReservationForm();
		reservationForm.setTime(this.time);
		reservationForm.setDate(this.date);
		reservationForm.setRideStation(this.rideStation);
		reservationForm.setDropOffStation(this.dropOffStation);
		reservationForm.setSeat(this.seat);
		
		reservationService.reservation(reservationForm);
		
		//予約完了したので、フォーム上の情報をリセット
		this.rideStation = null;
		this.dropOffStation = null;
		this.time = null;
		this.seat = null;
		
		return "completed";
	}
	
	//（往復）予約ボタンを押した時に処理されるハンドラ。予約の実行をする。そして検索画面に移動する。
	@PostMapping("/reserveRoundTrip")
	public String reserveRoundTrip() {
		
		//往路
		ReservationForm reservationForm = new ReservationForm();
		reservationForm.setTime(this.time);
		reservationForm.setDate(this.date);
		reservationForm.setRideStation(this.rideStation);
		reservationForm.setDropOffStation(this.dropOffStation);
		reservationForm.setSeat(this.outwardTripSeat);
		
		reservationService.reservation(reservationForm);
		
		//復路
		ReservationForm returnTripReservationForm = new ReservationForm();
		returnTripReservationForm.setTime(this.returnTripTime);
		returnTripReservationForm.setDate(this.returnTripDate);
		returnTripReservationForm.setRideStation(this.dropOffStation);
		returnTripReservationForm.setDropOffStation(this.rideStation);
		returnTripReservationForm.setSeat(this.returnSeat);
		
		reservationService.reservation(returnTripReservationForm);
		
		//予約完了したので、フォーム上の情報をリセット
		this.time = null;
		this.outwardTripSeat = null;
		this.rideStation = null;
		this.dropOffStation = null;
		this.returnTripTime = null;
		this.returnSeat = null;
		
		return "completed";
	}
	
	
	
	//トップページで予約確認ボタンを押した時に処理されるハンドラ。予約済情報の一覧を取得する。予約済項目表示画面に移動する。
	@GetMapping("/cancel")
	public String cancel(Model model) {
		List<ReservationForm> reservationList = reservationService.searchReservation();
		for(ReservationForm reservation : reservationList) {
			reservation.timeToString();
		}
		model.addAttribute("reservationList", reservationList);
		
		return "cancel";
	}
	
	
	
	//キャンセル確認画面
	@GetMapping("/confirmedCancel/{id}")
	public String confirmedCancel(@PathVariable int id, Model model) {
		Optional<ReservationForm> reservation = reservationService.getCanselReservation(id);
		ReservationForm reservationForm = reservation.get();
		reservationForm.timeToString();
		model.addAttribute("reservationForm", reservationForm);
		System.out.println(reservationForm);
		return "confirmedCancel";
	}
	
	
	
	//キャンセル確認画面にて「OK」ボタンを押した時に処理されるハンドラ。予約済情報を削除する。予約済項目表示画面に移動する。
	@GetMapping("/completedCansel/{id}")
	public String completedCancel(@PathVariable int id) {
		reservationService.deleteReservation(id);
		
		return "completedCansel";
	}
	
}
