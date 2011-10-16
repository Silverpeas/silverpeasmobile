package com.oosphere.silverpeasmobile.task;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.oosphere.silverpeasmobile.exception.SilverpeasMobileException;
import com.oosphere.silverpeasmobile.utils.StringUtils;
import com.oosphere.silverpeasmobile.vo.TaskRelatedResumeVO;
import com.oosphere.silverpeasmobile.vo.TaskVO;
import com.oosphere.silverpeasmobile.vo.comparator.TaskVOListSeparatorOptionComparator;
import com.stratelia.webactiv.calendar.control.CalendarBm;
import com.stratelia.webactiv.calendar.model.Attendee;
import com.stratelia.webactiv.calendar.model.ToDoHeader;
import com.stratelia.webactiv.kmelia.control.ejb.KmeliaBm;
import com.stratelia.webactiv.kmelia.model.KmeliaPublication;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;
import com.stratelia.webactiv.util.publication.model.PublicationPK;

public class TaskManager {
  
  
  private TaskDateUtils taskDateUtils = new TaskDateUtils();
  private CalendarBm calendarBm;
  private KmeliaBm kmeliaBm;

  public TaskManager(CalendarBm calendarBm, KmeliaBm kmeliaBm) {
    this.calendarBm = calendarBm;
    this.kmeliaBm = kmeliaBm;
  }

  public List<TaskVO> getUserOpenTasks(String userId) throws SilverpeasMobileException {
    try {
      List<ToDoHeader> taskList =
          new ArrayList<ToDoHeader>(calendarBm.getNotCompletedToDosForUser(userId));

      taskList = removeCompletedTasksFromList(taskList);
      
      List<TaskVO> taskVOList = new ArrayList<TaskVO>();
      for (ToDoHeader toDoHeader : taskList) {
        TaskVO taskVO = constructTaskVO(toDoHeader);
        taskVOList.add(taskVO);
      }
      
      taskVOList = setListSeparatorByOption(taskVOList);
      
      Collections.sort(taskVOList, new TaskVOListSeparatorOptionComparator());
      
      taskVOList = setFirstOptionItemShowListSeparator(taskVOList);

      return taskVOList;
    } catch (RemoteException e) {
      throw new SilverpeasMobileException(this, "getUserTasks",
          "Error while getting todos for user " + userId, e);
    }
  }
  
  private List<TaskVO> setListSeparatorByOption(List<TaskVO> listTask) throws SilverpeasMobileException{
    List<TaskVO> modifiedList = new ArrayList<TaskVO>(listTask);
    
    for (TaskVO taskVO : modifiedList) {
      String option = calculateListBlockOption(taskVO);
      taskVO.setListSeparator(option);
    }
    
    return modifiedList;
  }
  
  private List<TaskVO> setFirstOptionItemShowListSeparator(List<TaskVO> listTask) throws SilverpeasMobileException{
    List<TaskVO> modifiedList = new ArrayList<TaskVO>(listTask); 
    
    String previousOption = "";
    for (TaskVO taskVO : modifiedList) {
      String option = taskVO.getListSeparator();
      
      if(!previousOption.equals(option)){
        taskVO.setShowListSeparator(true);
        previousOption = option;
      }
    }
    
    return modifiedList;
  }
  
  private String calculateListBlockOption(TaskVO task) throws SilverpeasMobileException{
    String option = "";
    
    Date today = Calendar.getInstance().getTime();
    
    if(task.getEnd()!=null && task.getEnd().before(today)){
      option = TaskVO.OPTION_LABELS.get(TaskVO.DUE);
    } else if(task.getStart()!=null && task.getStart().before(today)){
      option = TaskVO.OPTION_LABELS.get(TaskVO.STARTED);
    } else {
      if(task.getStartDateOption().equalsIgnoreCase("("+TaskVO.SPECIFIC+")") || task.getStartDateOption().equalsIgnoreCase(TaskVO.SPECIFIC)){
        Date taskStartDate = taskDateUtils.calculateDateByOption(TaskVO.SPECIFIC, task.getStartTime(), task.getStartDate());
        option = taskDateUtils.calculateOptionForListItem(taskStartDate);
      } else {
        option = TaskVO.OPTION_LABELS.get(task.getStartDateOption());
        
      }
    }
    
    return option;
  }
  
  

  public TaskVO getTask(String taskId) throws SilverpeasMobileException {
    try {
      ToDoHeader todo = calendarBm.getToDoHeader(taskId);
      TaskVO taskVO = constructTaskVO(todo);

//      List<String> attendeesNames = getAttendeesNamesList(taskId);
//      taskVO.setAttendees(attendeesNames);
      
      String attendeeId = getFirstAttendeeId(taskId);
      taskVO.setAttendeeId(attendeeId);

      return taskVO;
    } catch (RemoteException e) {
      throw new SilverpeasMobileException(this, "getTask", "Error while getting todo todoId=" +
          taskId, e);
    }
  }

  public void createTask(TaskVO taskVO, String userId) throws SilverpeasMobileException {
    try {

      ToDoHeader todo = constructToDoHeader(taskVO, userId);
      String todoId = calendarBm.addToDo(todo);

      for (String attendeeId : taskVO.getAttendees()) {
        Attendee attendee = new Attendee(attendeeId);
        calendarBm.addToDoAttendee(todoId, attendee);
      }

    } catch (RemoteException e) {
      throw new SilverpeasMobileException(this, "createTask", "Error while creating todo", e);
    }
  }

  public void updateTask(TaskVO taskVO) throws SilverpeasMobileException {
    try {
      ToDoHeader todo = calendarBm.getToDoHeader(taskVO.getId());
      todo.setName(taskVO.getName());
      todo.setDescription(taskVO.getDescription());
      todo.setPercentCompleted(taskVO.getPercentCompleted());

      Date startDate =
          taskDateUtils.calculateDateByOption(taskVO.getStartDateOption(), taskVO.getStartTime(),
              taskVO.getStartDate());
      todo.setStartDate(startDate);
      if (StringUtils.isValued(taskVO.getStartTime())) {
        todo.setStartHour(taskVO.getStartTime());
      }

      Date endDate =
          taskDateUtils.calculateDateByOption(taskVO.getEndDateOption(), taskVO.getEndTime(), taskVO.getEndDate());
      todo.setEndDate(endDate);
      if (StringUtils.isValued(taskVO.getEndTime())) {
        todo.setEndHour(taskVO.getEndTime());
      }

      calendarBm.updateToDo(todo);

    } catch (RemoteException e) {
      throw new SilverpeasMobileException(this, "updateTask", "Error while updating todo", e);
    } catch (ParseException e) {
      throw new SilverpeasMobileException(this, "updateTask", "Error while parsing time", e);
    }
  }

  public void closeTask(String taskId) throws SilverpeasMobileException {
    try {
      ToDoHeader todo = calendarBm.getToDoHeader(taskId);
      todo.setPercentCompleted(100);
      calendarBm.updateToDo(todo);
    } catch (RemoteException e) {
      throw new SilverpeasMobileException(this, "closeTask", "Error while getting todo todoId=" +
          taskId, e);
    }
  }

  private TaskRelatedResumeVO getTaskRelated(ToDoHeader todo) throws SilverpeasMobileException {
    TaskRelatedResumeVO related = new TaskRelatedResumeVO();

    if (StringUtils.isValued(todo.getExternalId()) && (StringUtils.isValued(todo.getComponentId()))) {
      String componentId = todo.getComponentId();
      String externalId = todo.getExternalId();
      if (componentId.startsWith("projectManager")) {
        related = getProjectManagerResume(componentId, externalId);
      } else if (componentId.startsWith("demandeCongesSimple")) {
        related = getDemandeCongesResume(componentId, externalId);
      } else if (componentId.startsWith("kmelia")) {
        related = getPublicationResume(componentId, externalId);
      } else if (componentId.startsWith("mailinglist")) {
        related = getMailinglistResume(componentId, externalId);
      }
    }

    return related;
  }

  private TaskRelatedResumeVO getMailinglistResume(String componentId, String externalId) {
    TaskRelatedResumeVO resume = new TaskRelatedResumeVO();
    resume.setType("Mailinglist");

    return resume;
  }

  private TaskRelatedResumeVO getPublicationResume(String componentId, String externalId)
      throws SilverpeasMobileException {
    TaskRelatedResumeVO resume = new TaskRelatedResumeVO();
    resume.setType("Document");

    try {
      KmeliaPublication publication =
          kmeliaBm.getPublication(new PublicationPK(externalId, componentId));
      PublicationDetail publicationDetail = publication.getDetail();
      String name = publicationDetail.getName();
      
      resume.setText(name);
      
    } catch (RemoteException e) {
      throw new SilverpeasMobileException(this, "getPublicationResume",
          "Error while retreiving publication component=" +
              componentId + " id=" + externalId, e);
    }

    return resume;
  }

  private TaskRelatedResumeVO getDemandeCongesResume(String componentId, String externalId) {
    TaskRelatedResumeVO resume = new TaskRelatedResumeVO();
    resume.setType("Leave Request");

    return resume;
  }

  private TaskRelatedResumeVO getProjectManagerResume(String componentId, String externalId) {
    TaskRelatedResumeVO resume = new TaskRelatedResumeVO();
    resume.setType("Project");
    
    //projectManagerBm.

    return resume;
  }

  private List<ToDoHeader> removeCompletedTasksFromList(List<ToDoHeader> taskList) {
    List<ToDoHeader> taskListClean = new ArrayList<ToDoHeader>();

    for (ToDoHeader toDoHeader : taskList) {
      if (toDoHeader.getPercentCompleted() != 100) {
        taskListClean.add(toDoHeader);
      }
    }

    return taskListClean;
  }

  

//  private List<String> getAttendeesNamesList(String taskId) throws RemoteException {
//    List<String> attendeesNames = new ArrayList<String>();
//    List<Attendee> attendeeList =
//        new ArrayList<Attendee>(calendarBm.getToDoAttendees(taskId));
//
//    for (Attendee attendee : attendeeList) {
//      UserDetail userDetail = organizationController.getUserDetail(attendee.getUserId());
//      attendeesNames.add(userDetail.getFirstName() + " " + userDetail.getLastName());
//    }
//
//    return attendeesNames;
//  }
  
  private String getFirstAttendeeId(String taskId) throws RemoteException {
    String attendeeId = new String();
    List<Attendee> attendeeList =
        new ArrayList<Attendee>(calendarBm.getToDoAttendees(taskId));

    if(attendeeList.size()>0){
      Attendee attendee = attendeeList.get(0);
      attendeeId = attendee.getUserId();
    }
    
    return attendeeId;
  }

  private ToDoHeader constructToDoHeader(TaskVO taskVO, String userId)
      throws SilverpeasMobileException {
    ToDoHeader todo = new ToDoHeader();

    try {
      todo.setName(taskVO.getName());
      todo.setDescription(taskVO.getDescription());
      todo.setDelegatorId(userId);
      todo.setPercentCompleted(taskVO.getPercentCompleted());

      Date startDate =
          taskDateUtils.calculateDateByOption(taskVO.getStartDateOption(), taskVO.getStartTime(),
              taskVO.getStartDate());
      todo.setStartDate(startDate);
      if (StringUtils.isValued(taskVO.getStartTime())) {
        todo.setStartHour(taskVO.getStartTime());
      } else{
        todo.setStartHour(TaskDateUtils.STANDARD_START_TIME);
      }

      Date endDate =
          taskDateUtils.calculateDateByOption(taskVO.getEndDateOption(), taskVO.getEndTime(), taskVO.getEndDate());
      todo.setEndDate(endDate);
      if (StringUtils.isValued(taskVO.getEndTime())) {
        todo.setEndHour(taskVO.getEndTime());
      } else{
        todo.setEndHour(TaskDateUtils.STANDARD_END_TIME);
      }
    } catch (ParseException e) {
      throw new SilverpeasMobileException(this, "constructToDoHeader", "Error while parsing time",
          e);
    }

    return todo;
  }

  private TaskVO constructTaskVO(ToDoHeader todo) throws SilverpeasMobileException {

    TaskVO taskVO = new TaskVO();
    taskVO.setId(todo.getId());
    taskVO.setName(todo.getName());
    taskVO.setDescription(todo.getDescription());
    taskVO.setPercentCompleted(todo.getPercentCompleted());
    
    taskVO.setEnd(todo.getEndDate());
    String formattedEndDate = taskDateUtils.formatDate(todo.getEndDate());
    taskVO.setEndDate(formattedEndDate);
    if(!TaskDateUtils.STANDARD_END_TIME.equals(todo.getEndHour())){
      taskVO.setEndTime(todo.getEndHour());
    }
    String endDateOption =
        taskDateUtils.calculateDateOption(taskVO.getEndTime(), todo.getEndDay(), todo.getEndDate());
    taskVO.setEndDateOption(endDateOption);
    taskVO.setEndDateLabel(taskDateUtils.getDateLabel(endDateOption, formattedEndDate, todo.getEndHour()));
    
    taskVO.setStart(todo.getStartDate());
    String formattedStartDate = taskDateUtils.formatDate(todo.getStartDate());
    taskVO.setStartDate(formattedStartDate);
    if(!TaskDateUtils.STANDARD_START_TIME.equals(todo.getStartHour())){
      taskVO.setStartTime(todo.getStartHour());
    }
    String startDateOption =
        taskDateUtils.calculateDateOption(taskVO.getStartTime(), todo.getStartDay(), todo.getStartDate());
    taskVO.setStartDateOption(startDateOption);
    taskVO
        .setStartDateLabel(taskDateUtils.getDateLabel(startDateOption, formattedStartDate, todo.getStartHour()));
    
    TaskRelatedResumeVO related = getTaskRelated(todo);
    taskVO.setObjectRelated(related);
    
    return taskVO;
  }
}
