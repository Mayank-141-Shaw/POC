package com.neosoft.microservices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFactory {
	
	private Date date;
	
	DateFactory(String date) throws ParseException{
		this.setDate(toDate(date));
	}
	
	static Date toDate(String date) throws ParseException {
		return new SimpleDateFormat("yyyy-mm-dd").parse(date);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
