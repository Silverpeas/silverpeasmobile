package com.silverpeas.mobile.shared.dto.navigation;

import com.silverpeas.mobile.shared.dto.RightDTO;

import java.io.Serializable;


public class ApplicationInstanceDTO extends SilverpeasObjectDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String type;
  private RightDTO rights;
  private boolean commentable;
  private boolean notifiable;
  private boolean ableToStoreContent;
  private boolean workflow;

  public String getType(){
		return type;
	}
	public void setType(String type){
		this.type = type;
	}

  public RightDTO getRights() {
    return rights;
  }

  public void setRights(final RightDTO rights) {
    this.rights = rights;
  }

  public boolean isCommentable() {
    return commentable;
  }

  public void setCommentable(final boolean commentable) {
    this.commentable = commentable;
  }

  public void setAbleToStoreContent(final boolean ableToStoreContent) {
    this.ableToStoreContent = ableToStoreContent;
  }

  public boolean isAbleToStoreContent() {
    return ableToStoreContent;
  }

  public boolean isNotifiable() {
    return notifiable;
  }

  public void setNotifiable(boolean notifiable) {
    this.notifiable = notifiable;
  }

  public void setWorkflow(boolean workflow) {
    this.workflow = workflow;
  }

  public boolean isWorkflow() {
    return workflow;
  }
}
