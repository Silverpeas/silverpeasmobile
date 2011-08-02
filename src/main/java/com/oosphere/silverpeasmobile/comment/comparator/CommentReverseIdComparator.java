package com.oosphere.silverpeasmobile.comment.comparator;

import java.util.Comparator;

import com.silverpeas.comment.model.Comment;

public class CommentReverseIdComparator implements Comparator<Comment> {
  
  public int compare(Comment comment1, Comment comment2){
    if(Integer.valueOf(comment1.getCommentPK().getId()).intValue() > Integer.valueOf(comment2.getCommentPK().getId()).intValue()){
      return -1;
    } else if (Integer.valueOf(comment1.getCommentPK().getId()).intValue() < Integer.valueOf(comment2.getCommentPK().getId()).intValue()){
      return 1;
    } else {
      return 0;
    }
  }
}
