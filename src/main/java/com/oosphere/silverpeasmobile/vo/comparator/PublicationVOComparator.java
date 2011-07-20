package com.oosphere.silverpeasmobile.vo.comparator;

import java.util.Comparator;

import com.oosphere.silverpeasmobile.vo.PublicationVO;

public class PublicationVOComparator implements Comparator<PublicationVO> {

  public int compare(PublicationVO publication1, PublicationVO publication2) {
    return publication1.getName().toLowerCase().compareTo(publication2.getName().toLowerCase());
  }

}
