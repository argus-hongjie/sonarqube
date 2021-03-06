/*
 * SonarQube
 * Copyright (C) 2009-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.usergroups.ws;

import javax.annotation.concurrent.Immutable;
import org.sonar.db.user.GroupDto;

import static java.util.Objects.requireNonNull;

/**
 * Reference to a user group, as used internally by the backend. It does
 * not support reference to virtual groups "anyone".
 *
 * @see GroupWsRef
 * @see GroupIdOrAnyone
 */
@Immutable
public class GroupId {

  private final long id;
  private final String organizationUuid;

  public GroupId(String organizationUuid, long id) {
    this.id = id;
    this.organizationUuid = requireNonNull(organizationUuid);
  }

  public long getId() {
    return id;
  }

  public String getOrganizationUuid() {
    return organizationUuid;
  }

  public static GroupId from(GroupDto dto) {
    return new GroupId(dto.getOrganizationUuid(), dto.getId());
  }
}
