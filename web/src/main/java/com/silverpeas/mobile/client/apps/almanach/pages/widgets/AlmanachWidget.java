package com.silverpeas.mobile.client.apps.almanach.pages.widgets;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.constants.DateTimeConstants;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.silverpeas.mobile.client.apps.almanach.events.pages.LoadEventDetailDTOEvent;
import com.silverpeas.mobile.client.apps.almanach.resources.AlmanachResources;
import com.silverpeas.mobile.client.common.EventBus;
import com.silverpeas.mobile.shared.dto.EventDetailDTO;

@SuppressWarnings("deprecation")
public class AlmanachWidget extends Composite{

	protected AlmanachResources ressources = null;
	
	private static final String StyleCCellEmpty = "cellEmpty";
	private static final String StyleCInvalidDay = "invalidDay";
	private static final String StyleCValidDay = "validDay";
	private static final String StyleCToday = "today";
	private static final String StyleCWeekend = "weekend";
	private static final String StyleCSelected = "selectedDay";
	private static final String StyleCAfterSelected = "afterSelected";
	private static final String StyleCBeforeSelected = "beforeSelected";

	protected boolean needsRedraw = true;
	private boolean initialized = false;
	private boolean clickOnWeekNumbers = false;
	private int showWeekNumbers = 0;
	private Date cursorDate = getFirstDayOfMonth(new Date());
	private Date firstMonthDay = getFirstDayOfMonth(cursorDate);
	private long monthNumber = getMonthNumber(cursorDate);

	private Date minimalDate = setHourToZero(new Date());
	private Date selectedDate = setHourToZero(new Date());
	private Date maximalDate = AlmanachWidget.increaseDate(new Date(), 365);

	private static final int CONST_YEARS = 1;
	private static final int CONST_MONTHS = 2;
	private static final int CONST_WEEKS = 3;
	private static final int CONST_DAYS = 4;

	private static final String StyleCCellWeekNumber = "cellWeekNumbers";

	public static final DateTimeConstants dateTimeConstants = LocaleInfo
			.getCurrentLocale().getDateTimeConstants();
	private static final int weekStart = Integer.valueOf(
			dateTimeConstants.firstDayOfTheWeek()).intValue() - 1;
	public String[] WEEK_DAYS = dateTimeConstants.shortWeekdays();

	private final FlexTable calendarGrid = new FlexTable();

	protected AlmanachWidget() {
	}

	public AlmanachWidget(boolean create, Collection<EventDetailDTO> listEventDetailDTO) {
		if (create) {
			ressources = GWT.create(AlmanachResources.class);		
			ressources.css().ensureInjected();
			Window.addResizeHandler(new ResizeHandler() {				
				@Override
				public void onResize(ResizeEvent event) {
					if (initialized){
						needsRedraw = true;
						refresh();
					}					
				}
			});
			initWidget(calendarGrid);			
		}
		refresh();
		Iterator<EventDetailDTO> m = listEventDetailDTO.iterator();
		while (m.hasNext()) {
			EventDetailDTO eventDetailDTO = m.next();
			Date date = eventDetailDTO.getStartDate();
			Date monthDate = getFirstDayOfMonth(date);
			long monthNumberEvent = getMonthNumber(monthDate);
			if (monthNumberEvent == monthNumber) {
				for (int i = 1; i < 7; i++) {
					for (int k = 0; k < 7; k++) {
						final CellHTML cellHTML = (CellHTML) calendarGrid.getWidget(i, showWeekNumbers + k);
						if (getDayOfMonth(date) == cellHTML.getDay()) {
							cellHTML.getListEventDetailDTO().add(eventDetailDTO);
							calendarGrid.getCellFormatter().getElement(i, showWeekNumbers + k).addClassName(ressources.css().cellDayWithEvent());
							cellHTML.addClickHandler(new ClickHandler(){  
								  @Override  
								  public void onClick(ClickEvent event) {  
									  EventBus.getInstance().fireEvent(new LoadEventDetailDTOEvent(cellHTML.getListEventDetailDTO()));
								  }  
							});    
						}
					}
				}
			}
		}
	}
	
	public static int getDayOfMonth(Date aDate) {
	    DateTimeFormat df = DateTimeFormat.getFormat("dd");
		return Integer.parseInt(df.format(aDate));
	}

	public void refresh() {
		if (!needsRedraw)
			return;
		needsRedraw = false;

		if (!initialized) {
			initialized = true;
			calendarGrid.clear();			
			calendarGrid.setStylePrimaryName(ressources.css().calendar());
			calendarGrid.setCellSpacing(0);
			calendarGrid.getRowFormatter().setStyleName(0, ressources.css().weekHeader());
			int l = 0;
			for (int i = weekStart; i < 7; i++) {
				calendarGrid.getCellFormatter().setStyleName(0,
						showWeekNumbers + l, ressources.css().dayName());
				calendarGrid.setText(0, showWeekNumbers + l++, WEEK_DAYS[i]);
			}
			while (l < 7) {
				calendarGrid.getCellFormatter().setStyleName(0,
						showWeekNumbers + l, ressources.css().dayName());
				calendarGrid.setText(0, showWeekNumbers + l++, WEEK_DAYS[0]);
			}
			for (int i = 1; i < 7; i++) {
				for (int k = 0; k < 7; k++) {
					CellHTML html = new CellHTML();
					calendarGrid.setWidget(i, showWeekNumbers + k, html);
				}
				if (showWeekNumbers == 1) {
				}
			}
			if (showWeekNumbers == 1) {
				for (int i = 0; i < 7; i++) {
					calendarGrid.setText(i, 0, "");
					calendarGrid.getCellFormatter().setStyleName(i, 0,
							StyleCCellWeekNumber);
				}
			}
		}
		long todayNum = 1 + AlmanachWidget.compareDate(firstMonthDay,
				new Date());
		long minimalNum = 1 + AlmanachWidget.compareDate(firstMonthDay,
				minimalDate);
		long maximalNum = 1 + AlmanachWidget.compareDate(firstMonthDay,
				maximalDate);
		int numOfDays = AlmanachWidget.daysInMonth(cursorDate);
		long selectedNum = selectedDate != null ? 1 + AlmanachWidget
				.compareDate(firstMonthDay, selectedDate) : -1;
		int firstWDay = firstMonthDay.getDay();
		int sunday = (7 - weekStart) % 7;
		int saturday = 6 - weekStart;

		int j = 0 + weekStart;
		for (int i = 1; i < 7; i++) { // each week
			for (int k = 0; k < 7; k++, j++) { // each day in the week
				int displayNum = (firstWDay < weekStart) ? (j - firstWDay - 6)
						: (j - firstWDay + 1);

				// weekNumbers
				if (showWeekNumbers == 1 && k == 6 - weekStart) {
					int firstNum = displayNum - (i == 1 ? 0 : 6 - weekStart);
					if (firstNum > numOfDays) {
						calendarGrid.setText(i, 0, "");
					} else {
						Date refDate = new Date(cursorDate.getYear(),
								cursorDate.getMonth(), firstNum);
						int week = getWeekOfYear(refDate);
						putWeekNumber(i, displayNum, week);
					}
				}

				// Each calendar cell
				String styles = "";
				boolean enabled = true;
				if (j < firstWDay || displayNum > numOfDays || displayNum < 1) {
					styles = StyleCCellEmpty;
					enabled = false;
					displayNum = 0;
				} else {
					if (displayNum < minimalNum || displayNum > maximalNum) {
						styles = StyleCInvalidDay;
						enabled = false;
					} else if (displayNum == selectedNum) {
						styles = StyleCValidDay + " " + StyleCSelected;
					} else if (displayNum >= selectedNum) {
						styles = StyleCValidDay + " " + StyleCAfterSelected;
					} else {
						styles = StyleCValidDay + " " + StyleCBeforeSelected;
					}
					if (displayNum == todayNum) {
						styles += " " + StyleCToday;
					}
					if (k == sunday || k == saturday) {
						styles += " " + StyleCWeekend;
					}
					styles += " " + ressources.css().cellDays();
				}

				CellHTML html = (CellHTML) calendarGrid.getWidget(i,
						showWeekNumbers + k);
				html.setEnabled(enabled);
				html.setDay(displayNum);
				html.setStyleName(styles);
				
				int w = Window.getClientWidth() / 7;
				html.setHeight(w+"px");
				html.setWidth(w+"px");
				html.getElement().getStyle().setFontSize(w/3, Unit.PX);
								
			}
		}
	}

	private static class CellHTML extends HTML {
		private int day = -1;
		private boolean enabled = true;
		private Date date = null;
		private boolean events = false;
		private List<EventDetailDTO> listEventDetailDTO = new ArrayList<EventDetailDTO>();

		@SuppressWarnings("unused")
		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
			enabled = true;
		}

		public void setEnabled(boolean b) {
			enabled = b;
		}

		@SuppressWarnings("unused")
		public boolean isEnabled() {
			return enabled;
		}

		public int getDay() {
			return day;
		}

		public void setDay(int d) {
			if (day != d) {
				day = d;
				setCellContent();
			}
		}

		@SuppressWarnings("unused")
		public boolean getEvents() {
			return events;
		}

		@SuppressWarnings("unused")
		public void setEvents(boolean b) {
			events = b;
		}

		public List<EventDetailDTO> getListEventDetailDTO() {
			return listEventDetailDTO;
		}

		private void setCellContent() {
			setHTML(day < 1 ? "&nbsp;" : String.valueOf(day));
		}
	}

	public static Date getFirstDayOfMonth(Date date) {
		return setHourToZero(new Date(date.getYear(), date.getMonth(), 1));
	}

	public static Date setHourToZero(Date date) {
		Date d = new Date(date.getTime());
		d.setHours(0);
		d.setMinutes(0);
		d.setSeconds(0);

		// a hack to set milliseconds to zero
		long t = d.getTime() / 1000;
		t = t * 1000;

		return new Date(t);
	}

	public static long getMonthNumber(Date d) {
		return d.getYear() * 12 + d.getMonth();
	}

	public static Date increaseDate(Date d, int n) {
		Date ret = add(d, n, CONST_DAYS);
		return ret;
	}

	private static Date add(Date date, int value, int type) {
		Date d = setHourToZero(new Date(date.getTime()));
		if (type == CONST_YEARS)
			d.setYear(d.getYear() + value);
		if (type == CONST_MONTHS)
			d.setMonth(d.getMonth() + value);
		if (type == CONST_WEEKS)
			d.setDate(d.getDate() + 7 * value);
		if (type == CONST_DAYS)
			d.setDate(d.getDate() + value);
		return d;
	}

	public static int getWeekOfYear(Date date) {
		int base_wDay = 4;// - weekStart;
		Date jan01_Date = new Date(date.getYear(), 0, 1);
		int jan01_wDay = jan01_Date.getDay();
		int incr = jan01_wDay <= base_wDay ? (base_wDay - jan01_wDay)
				: (base_wDay + 7 - jan01_wDay);
		Date baseDay = AlmanachWidget.increaseDate(jan01_Date, incr);
		Date baseSun = AlmanachWidget.increaseDate(baseDay, -4);
		if (jan01_wDay > 4) {
			int n = AlmanachWidget.compareDate(baseSun, date);
			if (n < 0) {
				Date dec31 = new Date(date.getYear() - 1, 11, 31);
				return getWeekOfYear(dec31);
			}
		}
		int diff = AlmanachWidget.compareDate(baseSun, date);
		int ret = (int) Math.ceil(1 + diff / 7);
		return ret;
	}

	private void putWeekNumber(int i, int displayNum, int week) {
		if (clickOnWeekNumbers) {
			boolean enabled = true;
			Date weekDate = getFirstDayOfWeek(new Date(cursorDate.getYear(),
					cursorDate.getMonth(), displayNum));
			int diff = compareDate(minimalDate, weekDate);
			if (diff < 0 && diff + 7 < 0)
				enabled = false;
			diff = compareDate(maximalDate, weekDate);
			if (diff > 0 && diff + 7 > 0)
				enabled = false;

			if (enabled) {
				CellHTML weekHtml = (CellHTML) calendarGrid.getWidget(i, 0);
				if (weekHtml == null) {
					weekHtml = new CellHTML();
				}
				weekHtml.setEnabled(true);
				weekHtml.setDay(week);
				weekHtml.setDate(weekDate);
				calendarGrid.setWidget(i, 0, weekHtml);
				return;
			}
		}
		calendarGrid
				.setHTML(i, 0, "<div class=\"disabled\">" + week + "</div>");
	}

	public static Date getFirstDayOfWeek(Date date) {
		int n = date.getDay();
		if (n < weekStart)
			n += 7;
		int diff = n - weekStart;
		return increaseDate(date, -diff);
	}

	public static int compareDate(Date a, Date b) {

		long diff = setHourToZero(b).getTime() - setHourToZero(a).getTime();
		double hours = Math.ceil((double) diff / (1000 * 60 * 60));
		int days = (int) (hours / 24);
		if (hours % 24 > 12)
			days += 1;

		return days;
	}

	public static int daysInMonth(Date d) {
		int m = d.getMonth();
		switch (m) {
		case 1:
			int y = d.getYear() + 1900;
			return (y % 4 == 0 && y % 100 != 0) ? 29 : 28;
		case 3:
		case 5:
		case 8:
		case 10:
			return 30;
		default:
			return 31;
		}
	}

	public void setSelectedDate(Date d) {
		d = setHourToZero(d);
		if (d.getTime() == selectedDate.getTime())
			return;

		boolean a = belongsToMonth(selectedDate);
		boolean b = belongsToMonth(d);
		if (a && b && selectedDate.getTime() != d.getTime() || !a && b || a
				&& !b)
			needsRedraw = true;

		selectedDate = d;
	}

	public boolean belongsToMonth(Date d) {
		return monthNumber == getMonthNumber(d);
	}
}
