package com.silverpeas.mobile.shared.dto;

import java.io.Serializable;
import java.util.Date;

public class EventDetailDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _name;
	private Date _startDate;
	private Date _endDate;
	private int _priority;
	private String _title;
	private String startHour;
	private String endHour;
	private String place;
	private String eventUrl;
	
	public String get_name() {
		return _name;
	}
	public void set_name(String _name) {
		this._name = _name;
	}
	public Date get_startDate() {
		return _startDate;
	}
	public void set_startDate(Date _startDate) {
		this._startDate = _startDate;
	}
	public Date get_endDate() {
		return _endDate;
	}
	public void set_endDate(Date _endDate) {
		this._endDate = _endDate;
	}
	public int get_priority() {
		return _priority;
	}
	public void set_priority(int _priority) {
		this._priority = _priority;
	}
	public String get_title() {
		return _title;
	}
	public void set_title(String _title) {
		this._title = _title;
	}
	public String getStartHour() {
		return startHour;
	}
	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}
	public String getEndHour() {
		return endHour;
	}
	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getEventUrl() {
		return eventUrl;
	}
	public void setEventUrl(String eventUrl) {
		this.eventUrl = eventUrl;
	}
}
