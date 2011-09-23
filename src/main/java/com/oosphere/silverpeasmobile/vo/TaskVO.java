package com.oosphere.silverpeasmobile.vo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskVO {

  private String id;
  private String name;
  private String description;
  private String endDate;
  private String endTime;
  private String startDate;
  private String startTime;
  private String userId;
  private List<String> attendees;
  private String attendeeId;
  private int percentCompleted;
  private TaskRelatedResumeVO objectRelated;
  private String startDateOption;
  private String endDateOption;
  private String endDateLabel;
  private String startDateLabel;
  private Boolean showListSeparator;
  private String listSeparator;
  private Date end;
  private Date start;
  
  public static final String NEXTWEEK = "nextweek";
  public static final String THISWEEK = "thisweek";
  public static final String TOMORROW = "tomorrow";
  public static final String TODAY = "today";
  public static final String LATER = "later";
  public static final String SPECIFIC = "specific";
  public static final String DUE = "due";
  public static final String STARTED = "started";
  
  public static Map<String, String> OPTION_LABELS = new HashMap<String, String>() {
    {
      put(DUE, "Overdue");
      put(STARTED, "Started");
      put(TODAY, "Today");
      put(TOMORROW, "Tomorrow");
      put(THISWEEK, "This Week");
      put(NEXTWEEK, "Next Week");
      put(LATER, "Later");
    }
  };
  
  public static Map<String, Integer> OPTION_ORDER = new HashMap<String, Integer>() {
    {
      put(TaskVO.TODAY, 0);
      put(TaskVO.TOMORROW, 1);
      put(TaskVO.THISWEEK, 2);
      put(TaskVO.NEXTWEEK, 3);
      put(TaskVO.LATER, 4);
    }
  };
  
  public static Map<String, Integer> OPTION_ORDER_FOR_LIST = new HashMap<String, Integer>() {
    {
      put(TaskVO.DUE, 0);
      put(TaskVO.STARTED, 1);
      put(TaskVO.TODAY, 2);
      put(TaskVO.TOMORROW, 3);
      put(TaskVO.THISWEEK, 4);
      put(TaskVO.NEXTWEEK, 5);
      put(TaskVO.LATER, 6);
      
      put(OPTION_LABELS.get(TaskVO.DUE), 0);
      put(OPTION_LABELS.get(TaskVO.STARTED), 1);
      put(OPTION_LABELS.get(TaskVO.TODAY), 2);
      put(OPTION_LABELS.get(TaskVO.TOMORROW), 3);
      put(OPTION_LABELS.get(TaskVO.THISWEEK), 4);
      put(OPTION_LABELS.get(TaskVO.NEXTWEEK), 5);
      put(OPTION_LABELS.get(TaskVO.LATER), 6);
    }
  };
  

  public TaskVO() {
  }
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public List<String> getAttendees() {
    return attendees;
  }

  public void setAttendees(List<String> attendees) {
    this.attendees = attendees;
  }
  
  public String getAttendeeId() {
    return attendeeId;
  }

  public void setAttendeeId(String attendeeId) {
    this.attendeeId = attendeeId;
  }

  public int getPercentCompleted() {
    return percentCompleted;
  }

  public void setPercentCompleted(int percentCompleted) {
    this.percentCompleted = percentCompleted;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public TaskRelatedResumeVO getObjectRelated() {
    return objectRelated;
  }

  public void setObjectRelated(TaskRelatedResumeVO objectRelated) {
    this.objectRelated = objectRelated;
  }

  public String getStartDateOption() {
    return startDateOption;
  }

  public void setStartDateOption(String startDateOption) {
    this.startDateOption = startDateOption;
  }

  public String getEndDateOption() {
    return endDateOption;
  }

  public void setEndDateOption(String endDateOption) {
    this.endDateOption = endDateOption;
  }

  public String getEndDateLabel() {
    return endDateLabel;
  }

  public void setEndDateLabel(String endDateLabel) {
    this.endDateLabel = endDateLabel;
  }

  public String getStartDateLabel() {
    return startDateLabel;
  }

  public void setStartDateLabel(String startDateLabel) {
    this.startDateLabel = startDateLabel;
  }

  public Boolean getShowListSeparator() {
    return showListSeparator;
  }

  public void setShowListSeparator(Boolean showListSeparator) {
    this.showListSeparator = showListSeparator;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public String getListSeparator() {
    return listSeparator;
  }

  public void setListSeparator(String listSeparator) {
    this.listSeparator = listSeparator;
  }
  
  
  
}
