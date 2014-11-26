package com.silverpeas.mobile.server.comparator;

import com.silverpeas.delegatednews.model.DelegatedNews;

import java.util.Comparator;

/**
 * @author: svu
 */
public class DelegatedNewsBeginDateComparatorAsc implements Comparator<DelegatedNews> {
  static public DelegatedNewsBeginDateComparatorAsc comparator = new DelegatedNewsBeginDateComparatorAsc();

  public int compare(DelegatedNews p1, DelegatedNews p2) {
    int compareResult = p1.getBeginDate().compareTo(p2.getBeginDate());
    if (compareResult == 0) {
      // both objects have same begin date
      // first one is the one which has been validated after the other
      compareResult = 0 - p1.getValidationDate().compareTo(p2.getValidationDate());
    }

    return compareResult;
  }
}