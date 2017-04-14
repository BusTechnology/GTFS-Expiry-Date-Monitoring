package model;

public class CalendarModel {
	String service_id;
	int monday;
	int tuesday;
	int wednesday;
	int thursday;
	int friday;
	int saturday;
	int sunday;
	int start_date;
	int end_date;
	
	public CalendarModel(String service_id, int monday, int tuesday, int wednesday, int thursday, int friday,
			int saturday, int sunday, int start_date, int end_date) {
		super();
		this.service_id = service_id;
		this.monday = monday;
		this.tuesday = tuesday;
		this.wednesday = wednesday;
		this.thursday = thursday;
		this.friday = friday;
		this.saturday = saturday;
		this.sunday = sunday;
		this.start_date = start_date;
		this.end_date = end_date;
	}

	public int getEnd_date() {
		return end_date;
	}
}
