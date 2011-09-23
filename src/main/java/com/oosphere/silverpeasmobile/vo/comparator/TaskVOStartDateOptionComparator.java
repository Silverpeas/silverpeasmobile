package com.oosphere.silverpeasmobile.vo.comparator;

import java.util.Comparator;

import com.oosphere.silverpeasmobile.vo.TaskVO;

public class TaskVOStartDateOptionComparator implements Comparator<TaskVO> {
  
  public int compare(TaskVO task1, TaskVO task2) {
    
    String task1StartOption = task1.getStartDateOption(); 
    String task2StartOption = task2.getStartDateOption(); 
    
    if(task1StartOption.equals(task2StartOption)){
      return 0;
    } else if(TaskVO.OPTION_ORDER.get(task1StartOption) > TaskVO.OPTION_ORDER.get(task2StartOption)){
      return 1;
    } else{
      return -1;
    }
  }
}
