package com.oosphere.silverpeasmobile.handler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.oosphere.silverpeasmobile.bean.WebBeanFactory;
import com.oosphere.silverpeasmobile.contact.ContactManager;
import com.oosphere.silverpeasmobile.exception.SilverpeasMobileException;
import com.oosphere.silverpeasmobile.kmelia.KmeliaManager;
import com.oosphere.silverpeasmobile.task.TaskDateUtils;
import com.oosphere.silverpeasmobile.task.TaskManager;
import com.oosphere.silverpeasmobile.utils.StringUtils;
import com.oosphere.silverpeasmobile.vo.TaskVO;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.beans.admin.UserDetail;

public class TaskHandler extends Handler {

  @Override
  public String getPage(HttpServletRequest request) throws SilverpeasMobileException {
    String page = "tasks.jsp";
    String subAction = request.getParameter("subAction");
    if ("tasks".equals(subAction)) {
      TaskManager taskManager = getTaskManager(request);
      page = tasks(request, taskManager);
    } else if ("task".equals(subAction)) {
      TaskManager taskManager = getTaskManager(request);
      KmeliaManager kmeliaManager = getKmeliaManager(request);
      page = task(request, taskManager, kmeliaManager);
    } else if ("close".equals(subAction)) {
      TaskManager taskManager = getTaskManager(request);
      page = close(request, taskManager);
    } else if ("update".equals(subAction)) {
      TaskManager taskManager = getTaskManager(request);
      page = update(request, taskManager);
    } else if ("create".equals(subAction)) {
      TaskManager taskManager = getTaskManager(request);
      page = create(request, taskManager);
    } else if ("new".equals(subAction)) {
      TaskManager taskManager = getTaskManager(request);
      page = newTask(request, taskManager);
    }
    return "task/" + page;
  }

  private String newTask(HttpServletRequest request, TaskManager taskManager)
      throws SilverpeasMobileException {
    String userId = request.getParameter("userId");

    TaskVO task = new TaskVO();
    request.setAttribute("task", task);

    addUsersOfSameDomainInRequest(request, userId);
    
    request.setAttribute("daysBeforeEndOfWeek", getDaysBeforeEndOfWeek());
    request.setAttribute("daysBeforeEndOfNextWeek", getDaysBeforeEndOfNextWeek());

    request.setAttribute("userId", userId);
    return "task.jsp";
  }

  private String update(HttpServletRequest request, TaskManager taskManager)
      throws SilverpeasMobileException {
    TaskVO taskVO = createTaskVOFromRequest(request);

    taskManager.updateTask(taskVO);

    return tasks(request, taskManager);
  }

  private TaskVO createTaskVOFromRequest(HttpServletRequest request) {
    String taskId = request.getParameter("taskId");
    String taskName = request.getParameter("name");
    String taskDescription = request.getParameter("description");
    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");
    String startTime = request.getParameter("startTime");
    String endTime = request.getParameter("endTime");
    String endDateOption = request.getParameter("endDateOption");
    String startDateOption = request.getParameter("startDateOption");
    String percentCompletedAsString = request.getParameter("completed");
    int percentCompleted = 0;

    if (StringUtils.isValued(percentCompletedAsString)) {
      percentCompleted = Integer.valueOf(percentCompletedAsString);
    }

    TaskVO taskVO = new TaskVO();
    taskVO.setId(taskId);
    taskVO.setName(taskName);
    taskVO.setDescription(taskDescription);
    taskVO.setStartDate(startDate);
    taskVO.setEndDate(endDate);
    taskVO.setEndTime(endTime);
    taskVO.setStartTime(startTime);
    taskVO.setPercentCompleted(percentCompleted);
    taskVO.setEndDateOption(endDateOption);
    taskVO.setStartDateOption(startDateOption);
    return taskVO;
  }

  private String create(HttpServletRequest request, TaskManager taskManager)
      throws SilverpeasMobileException {
    TaskVO taskVO = createTaskVOFromRequest(request);

    String attendeeId = request.getParameter("attendeeId");
    List<String> attendees = new ArrayList<String>();
    attendees.add(attendeeId);
    taskVO.setAttendees(attendees);

    String userId = request.getParameter("userId");
    taskManager.createTask(taskVO, userId);

    return tasks(request, taskManager);
  }

  private String close(HttpServletRequest request, TaskManager taskManager)
      throws SilverpeasMobileException {
    String taskId = request.getParameter("taskId");

    taskManager.closeTask(taskId);

    return tasks(request, taskManager);
  }

  private String tasks(HttpServletRequest request, TaskManager taskManager)
      throws SilverpeasMobileException {
    String userId = request.getParameter("userId");

    List<TaskVO> listTasks = taskManager.getUserOpenTasks(userId);
    request.setAttribute("tasks", listTasks);

    request.setAttribute("userId", userId);
    return "tasks.jsp";
  }

  private String task(HttpServletRequest request, TaskManager taskManager,
      KmeliaManager kmeliaManager) throws SilverpeasMobileException {
    String userId = request.getParameter("userId");
    String taskId = request.getParameter("taskId");

    TaskVO task = taskManager.getTask(taskId);
    request.setAttribute("task", task);

    // List<UserDetail> listComponentUsers = kmeliaManager.getComponentUsers(componentId);
    // request.setAttribute("listComponentUsers", listComponentUsers);
    addUsersOfSameDomainInRequest(request, userId);
    
    request.setAttribute("daysBeforeEndOfWeek", getDaysBeforeEndOfWeek());
    request.setAttribute("daysBeforeEndOfNextWeek", getDaysBeforeEndOfNextWeek());

    request.setAttribute("userId", userId);
    return "task.jsp";
  }

  private void addUsersOfSameDomainInRequest(HttpServletRequest request, String userId)
      throws SilverpeasMobileException {
    ContactManager contactManager = getContactManager(request);
    List<UserDetail> listUsers = contactManager.getAll(userId);
    request.setAttribute("listUsers", listUsers);
  }

  private TaskManager getTaskManager(HttpServletRequest request)
      throws SilverpeasMobileException {
    WebBeanFactory beanFactory = new WebBeanFactory();
    return new TaskManager(beanFactory.getCalendarBm(), beanFactory.getKmeliaBm());
  }

  private KmeliaManager getKmeliaManager(HttpServletRequest request)
      throws SilverpeasMobileException {
    WebBeanFactory beanFactory = new WebBeanFactory();
    OrganizationController organizationController = new OrganizationController();
    return new KmeliaManager(beanFactory.getAdminBm(), beanFactory.getKmeliaBm(),
        organizationController);
  }

  private ContactManager getContactManager(HttpServletRequest request)
      throws SilverpeasMobileException {
    OrganizationController organizationController = new OrganizationController();
    return new ContactManager(organizationController);
  }
  
  private int getDaysBeforeEndOfWeek(){
    TaskDateUtils taskDateUtils = new TaskDateUtils();
    Calendar today = Calendar.getInstance(); 
    return taskDateUtils.calculateDaysUntilEndOfWeek(today);
  }
  
  private int getDaysBeforeEndOfNextWeek(){
    TaskDateUtils taskDateUtils = new TaskDateUtils();
    Calendar today = Calendar.getInstance(); 
    return taskDateUtils.calculateDaysUntilEndOfNextWeek(today);
  }

}
