package com.oosphere.silverpeasmobile.task;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.oosphere.silverpeasmobile.exception.SilverpeasMobileException;
import com.oosphere.silverpeasmobile.utils.StringUtils;
import com.oosphere.silverpeasmobile.vo.TaskVO;

public class TaskDateUtils {
  
  public static final String STANDARD_END_TIME = "23:59";
  public static final String STANDARD_START_TIME = "00:01";
  private static final String EMPTY_STRING = "";
  private static final String DD_MM_YYYY = "dd-MM-yyyy";
  private static final String DD_MM_YYYY_HH_MM = "dd-MM-yyyy HH:mm";

  public TaskDateUtils() {
  }
  
  public String calculateOptionForListItem(Date taskStartDate) {
    String option = "";
    
    Calendar dateCalendar = Calendar.getInstance();
    dateCalendar.setTime(taskStartDate);
    
    Calendar today = Calendar.getInstance();
    
    if (today.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR)
        && today.get(Calendar.MONTH) == dateCalendar.get(Calendar.MONTH)
        && today.get(Calendar.DAY_OF_MONTH) == dateCalendar.get(Calendar.DAY_OF_MONTH)) {
      option = TaskVO.OPTION_LABELS.get(TaskVO.TODAY);
    } else if (today.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR)
        && today.get(Calendar.DAY_OF_YEAR) + 1 == dateCalendar.get(Calendar.DAY_OF_YEAR)) {
      option = TaskVO.OPTION_LABELS.get(TaskVO.TOMORROW);
    } else if (today.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR)
        && today.get(Calendar.WEEK_OF_YEAR) == dateCalendar.get(Calendar.WEEK_OF_YEAR)) {
      option = TaskVO.OPTION_LABELS.get(TaskVO.THISWEEK);
    } else if (today.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR)
        && today.get(Calendar.WEEK_OF_YEAR) + 1 == dateCalendar.get(Calendar.WEEK_OF_YEAR)) {
      option = TaskVO.OPTION_LABELS.get(TaskVO.NEXTWEEK);
    } else {
      option = TaskVO.OPTION_LABELS.get(TaskVO.LATER);
    }
    
    return option;
  }

  public String getDateLabel(String option, String day, String hour) {
    String label = "";

    if (TaskVO.SPECIFIC.equals(option)) {
      if (StringUtils.isValued(hour) && !STANDARD_START_TIME.equals(hour) && !STANDARD_END_TIME.equals(hour)) {
        label = day + " " + hour;
      } else {
        label = day;
      }
    } else {
      label = TaskVO.OPTION_LABELS.get(option);
    }

    label = "(" + label + ")";
    return label;
  }

  public String calculateDateOption(String hour, String day, Date date) {
    String option = EMPTY_STRING;

    if (hour != null && !EMPTY_STRING.equals(hour) && !STANDARD_START_TIME.equals(hour)  && !STANDARD_END_TIME.equals(hour)) {
      option = TaskVO.SPECIFIC;
    } else if (day == null || EMPTY_STRING.equals(day)) {
      option = TaskVO.LATER;
    } else {
      Calendar dateCalendar = Calendar.getInstance();
      dateCalendar.setTime(date);

      Calendar today = Calendar.getInstance();

      if (today.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR)
          && today.get(Calendar.MONTH) == dateCalendar.get(Calendar.MONTH)
          && today.get(Calendar.DAY_OF_MONTH) == dateCalendar.get(Calendar.DAY_OF_MONTH)) {
        option = TaskVO.TODAY;
      } else if (today.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR)
          && today.get(Calendar.DAY_OF_YEAR) + 1 == dateCalendar.get(Calendar.DAY_OF_YEAR)) {
        option = TaskVO.TOMORROW;
      } else if (today.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR)
          && today.get(Calendar.WEEK_OF_YEAR) == dateCalendar.get(Calendar.WEEK_OF_YEAR)
          && dateCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
        option = TaskVO.THISWEEK;
      } else if (today.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR)
          && today.get(Calendar.WEEK_OF_YEAR) + 1 == dateCalendar.get(Calendar.WEEK_OF_YEAR)
          && dateCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
        option = TaskVO.NEXTWEEK;
      } else {
        option = TaskVO.SPECIFIC;
      }
    }

    return option;
  }

  public Date calculateDateByOption(String option, String hour, String day)
      throws SilverpeasMobileException {
    Date calculatedDate = new Date();

    if (TaskVO.SPECIFIC.equals(option)) {
      if (StringUtils.isValued(day) && StringUtils.isValued(hour)) {
        calculatedDate = convertToDate(day + " " + hour, DD_MM_YYYY_HH_MM);
      } else if (StringUtils.isValued(day)) {
        calculatedDate = convertToDate(day, DD_MM_YYYY);
      }
    } else if (TaskVO.TODAY.equals(option)) {
      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.HOUR_OF_DAY, 23);
      calendar.set(Calendar.MINUTE, 59);
      calculatedDate = calendar.getTime();
    } else if (TaskVO.TOMORROW.equals(option)) {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DAY_OF_YEAR, 1);
      calendar.set(Calendar.HOUR_OF_DAY, 23);
      calendar.set(Calendar.MINUTE, 59);
      calculatedDate = calendar.getTime();
    } else if (TaskVO.THISWEEK.equals(option)) {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DAY_OF_YEAR, calculateDaysUntilEndOfWeek(calendar));
      calendar.set(Calendar.HOUR_OF_DAY, 23);
      calendar.set(Calendar.MINUTE, 59);
      calculatedDate = calendar.getTime();
    } else if (TaskVO.NEXTWEEK.equals(option)) {
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DAY_OF_YEAR, calculateDaysUntilEndOfNextWeek(calendar));
      calendar.set(Calendar.HOUR_OF_DAY, 23);
      calendar.set(Calendar.MINUTE, 59);
      calculatedDate = calendar.getTime();
    } else if (TaskVO.LATER.equals(option)) {
      calculatedDate = null;
    }

    return calculatedDate;
  }

  public int calculateDaysUntilEndOfWeek(Calendar calendar) {
    return Calendar.FRIDAY - calendar.get(Calendar.DAY_OF_WEEK);
  }

  public int calculateDaysUntilEndOfNextWeek(Calendar calendar) {
    return Calendar.FRIDAY - calendar.get(Calendar.DAY_OF_WEEK) + 7;
  }

  public String formatDate(Date date) {
    if (date != null) {
      DateFormat dateFormat = new SimpleDateFormat(DD_MM_YYYY);
      return dateFormat.format(date);
    }
    return EMPTY_STRING;
  }

  public Date convertToDate(String date, String format) throws SilverpeasMobileException {
    Date convertedDate = null;
    if (date != null && date.length() > 0) {
      DateFormat dateFormat = new SimpleDateFormat(format);
      try {
        convertedDate = dateFormat.parse(date);
      } catch (ParseException e) {
        throw new SilverpeasMobileException(this, "convertToDate",
            "Error while converting to Date date string = " +
                date, e);
      }
    }
    return convertedDate;
  }
}