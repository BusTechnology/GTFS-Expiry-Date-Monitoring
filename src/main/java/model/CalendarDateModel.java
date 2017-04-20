package model;

public class CalendarDateModel {
    String service_id;
    int date;
    int exception_type;

    public CalendarDateModel(String service_id, int date, int exception_type) {
        this.service_id = service_id;
        this.date = date;
        this.exception_type = exception_type;
    }

    public int getDate() {
        return date;
    }

}
