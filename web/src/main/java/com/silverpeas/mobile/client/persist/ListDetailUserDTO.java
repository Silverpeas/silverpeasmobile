package com.silverpeas.mobile.client.persist;

import com.silverpeas.mobile.shared.dto.DetailUserDTO;

import java.util.List;

/**
 * @author: svu
 */
public class ListDetailUserDTO {
  public List<DetailUserDTO> users;

  public ListDetailUserDTO() {
    super();
  }

  public ListDetailUserDTO(final List<DetailUserDTO> users) {
    this.users = users;
  }
}
