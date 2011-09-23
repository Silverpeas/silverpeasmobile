package com.oosphere.silverpeasmobile.vo.comparator;

import java.util.Comparator;

import com.oosphere.silverpeasmobile.vo.TaskVO;

public class TaskVOListSeparatorOptionComparator implements Comparator<TaskVO> {
  
  public int compare(TaskVO task1, TaskVO task2) {
    
    String task1ListSeparator = task1.getListSeparator(); 
    String task2ListSeparator = task2.getListSeparator(); 
    
    if(task1ListSeparator.equals(task2ListSeparator)){
      return 0;
    } else if(TaskVO.OPTION_ORDER_FOR_LIST.get(task1ListSeparator) > TaskVO.OPTION_ORDER_FOR_LIST.get(task2ListSeparator)){
      return 1;
    } else{
      return -1;
    }
  }
}
