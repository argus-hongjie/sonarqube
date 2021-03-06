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
package org.sonar.db;

import java.util.IdentityHashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.sonar.db.ce.CeActivityDao;
import org.sonar.db.ce.CeQueueDao;
import org.sonar.db.ce.CeScannerContextDao;
import org.sonar.db.ce.CeTaskInputDao;
import org.sonar.db.component.ComponentDao;
import org.sonar.db.component.ComponentKeyUpdaterDao;
import org.sonar.db.component.ComponentLinkDao;
import org.sonar.db.component.ResourceDao;
import org.sonar.db.component.SnapshotDao;
import org.sonar.db.duplication.DuplicationDao;
import org.sonar.db.event.EventDao;
import org.sonar.db.issue.IssueChangeDao;
import org.sonar.db.issue.IssueDao;
import org.sonar.db.loadedtemplate.LoadedTemplateDao;
import org.sonar.db.measure.MeasureDao;
import org.sonar.db.measure.custom.CustomMeasureDao;
import org.sonar.db.metric.MetricDao;
import org.sonar.db.notification.NotificationQueueDao;
import org.sonar.db.organization.OrganizationDao;
import org.sonar.db.permission.AuthorizationDao;
import org.sonar.db.permission.GroupPermissionDao;
import org.sonar.db.permission.UserPermissionDao;
import org.sonar.db.permission.template.PermissionTemplateCharacteristicDao;
import org.sonar.db.permission.template.PermissionTemplateDao;
import org.sonar.db.property.InternalPropertiesDao;
import org.sonar.db.property.PropertiesDao;
import org.sonar.db.purge.PurgeDao;
import org.sonar.db.qualitygate.ProjectQgateAssociationDao;
import org.sonar.db.qualitygate.QualityGateConditionDao;
import org.sonar.db.qualitygate.QualityGateDao;
import org.sonar.db.qualityprofile.ActiveRuleDao;
import org.sonar.db.qualityprofile.QProfileChangeDao;
import org.sonar.db.qualityprofile.QualityProfileDao;
import org.sonar.db.rule.RuleDao;
import org.sonar.db.rule.RuleRepositoryDao;
import org.sonar.db.schemamigration.SchemaMigrationDao;
import org.sonar.db.source.FileSourceDao;
import org.sonar.db.user.AuthorDao;
import org.sonar.db.user.GroupDao;
import org.sonar.db.user.GroupMembershipDao;
import org.sonar.db.user.RoleDao;
import org.sonar.db.user.UserDao;
import org.sonar.db.user.UserGroupDao;
import org.sonar.db.user.UserTokenDao;
import org.sonar.db.webhook.WebhookDeliveryDao;

public class DbClient {

  private final Database database;
  private final MyBatis myBatis;
  private final SchemaMigrationDao schemaMigrationDao;
  private final AuthorizationDao authorizationDao;
  private final OrganizationDao organizationDao;
  private final QualityProfileDao qualityProfileDao;
  private final LoadedTemplateDao loadedTemplateDao;
  private final PropertiesDao propertiesDao;
  private final InternalPropertiesDao internalPropertiesDao;
  private final SnapshotDao snapshotDao;
  private final ComponentDao componentDao;
  private final ResourceDao resourceDao;
  private final ComponentKeyUpdaterDao componentKeyUpdaterDao;
  private final MeasureDao measureDao;
  private final UserDao userDao;
  private final UserGroupDao userGroupDao;
  private final UserTokenDao userTokenDao;
  private final GroupMembershipDao groupMembershipDao;
  private final RoleDao roleDao;
  private final GroupPermissionDao groupPermissionDao;
  private final PermissionTemplateDao permissionTemplateDao;
  private final PermissionTemplateCharacteristicDao permissionTemplateCharacteristicDao;
  private final IssueDao issueDao;
  private final IssueChangeDao issueChangeDao;
  private final CeActivityDao ceActivityDao;
  private final CeQueueDao ceQueueDao;
  private final CeTaskInputDao ceTaskInputDao;
  private final CeScannerContextDao ceScannerContextDao;
  private final FileSourceDao fileSourceDao;
  private final AuthorDao authorDao;
  private final ComponentLinkDao componentLinkDao;
  private final EventDao eventDao;
  private final PurgeDao purgeDao;
  private final QualityGateDao qualityGateDao;
  private final QualityGateConditionDao gateConditionDao;
  private final ProjectQgateAssociationDao projectQgateAssociationDao;
  private final DuplicationDao duplicationDao;
  private final NotificationQueueDao notificationQueueDao;
  private final CustomMeasureDao customMeasureDao;
  private final MetricDao metricDao;
  private final GroupDao groupDao;
  private final RuleDao ruleDao;
  private final RuleRepositoryDao ruleRepositoryDao;
  private final ActiveRuleDao activeRuleDao;
  private final QProfileChangeDao qProfileChangeDao;
  private final UserPermissionDao userPermissionDao;
  private final WebhookDeliveryDao webhookDeliveryDao;

  public DbClient(Database database, MyBatis myBatis, Dao... daos) {
    this.database = database;
    this.myBatis = myBatis;

    Map<Class, Dao> map = new IdentityHashMap<>();
    for (Dao dao : daos) {
      map.put(dao.getClass(), dao);
    }
    schemaMigrationDao = getDao(map, SchemaMigrationDao.class);
    authorizationDao = getDao(map, AuthorizationDao.class);
    organizationDao = getDao(map, OrganizationDao.class);
    qualityProfileDao = getDao(map, QualityProfileDao.class);
    loadedTemplateDao = getDao(map, LoadedTemplateDao.class);
    propertiesDao = getDao(map, PropertiesDao.class);
    internalPropertiesDao = getDao(map, InternalPropertiesDao.class);
    snapshotDao = getDao(map, SnapshotDao.class);
    componentDao = getDao(map, ComponentDao.class);
    resourceDao = getDao(map, ResourceDao.class);
    componentKeyUpdaterDao = getDao(map, ComponentKeyUpdaterDao.class);
    measureDao = getDao(map, MeasureDao.class);
    userDao = getDao(map, UserDao.class);
    userGroupDao = getDao(map, UserGroupDao.class);
    userTokenDao = getDao(map, UserTokenDao.class);
    groupMembershipDao = getDao(map, GroupMembershipDao.class);
    roleDao = getDao(map, RoleDao.class);
    groupPermissionDao = getDao(map, GroupPermissionDao.class);
    permissionTemplateDao = getDao(map, PermissionTemplateDao.class);
    permissionTemplateCharacteristicDao = getDao(map, PermissionTemplateCharacteristicDao.class);
    issueDao = getDao(map, IssueDao.class);
    issueChangeDao = getDao(map, IssueChangeDao.class);
    ceActivityDao = getDao(map, CeActivityDao.class);
    ceQueueDao = getDao(map, CeQueueDao.class);
    ceTaskInputDao = getDao(map, CeTaskInputDao.class);
    ceScannerContextDao = getDao(map, CeScannerContextDao.class);
    fileSourceDao = getDao(map, FileSourceDao.class);
    authorDao = getDao(map, AuthorDao.class);
    componentLinkDao = getDao(map, ComponentLinkDao.class);
    eventDao = getDao(map, EventDao.class);
    purgeDao = getDao(map, PurgeDao.class);
    qualityGateDao = getDao(map, QualityGateDao.class);
    gateConditionDao = getDao(map, QualityGateConditionDao.class);
    projectQgateAssociationDao = getDao(map, ProjectQgateAssociationDao.class);
    duplicationDao = getDao(map, DuplicationDao.class);
    notificationQueueDao = getDao(map, NotificationQueueDao.class);
    customMeasureDao = getDao(map, CustomMeasureDao.class);
    metricDao = getDao(map, MetricDao.class);
    groupDao = getDao(map, GroupDao.class);
    ruleDao = getDao(map, RuleDao.class);
    ruleRepositoryDao = getDao(map, RuleRepositoryDao.class);
    activeRuleDao = getDao(map, ActiveRuleDao.class);
    qProfileChangeDao = getDao(map, QProfileChangeDao.class);
    userPermissionDao = getDao(map, UserPermissionDao.class);
    webhookDeliveryDao = getDao(map, WebhookDeliveryDao.class);
  }

  public DbSession openSession(boolean batch) {
    return myBatis.openSession(batch);
  }

  public void closeSession(@Nullable DbSession session) {
    MyBatis.closeQuietly(session);
  }

  public Database getDatabase() {
    return database;
  }

  public SchemaMigrationDao schemaMigrationDao() {
    return schemaMigrationDao;
  }

  public AuthorizationDao authorizationDao() {
    return authorizationDao;
  }

  public OrganizationDao organizationDao() {
    return organizationDao;
  }

  public IssueDao issueDao() {
    return issueDao;
  }

  public IssueChangeDao issueChangeDao() {
    return issueChangeDao;
  }

  public QualityProfileDao qualityProfileDao() {
    return qualityProfileDao;
  }

  public LoadedTemplateDao loadedTemplateDao() {
    return loadedTemplateDao;
  }

  public PropertiesDao propertiesDao() {
    return propertiesDao;
  }

  public InternalPropertiesDao internalPropertiesDao() {
    return internalPropertiesDao;
  }

  public SnapshotDao snapshotDao() {
    return snapshotDao;
  }

  public ComponentDao componentDao() {
    return componentDao;
  }

  public ResourceDao resourceDao() {
    return resourceDao;
  }

  public ComponentKeyUpdaterDao componentKeyUpdaterDao() {
    return componentKeyUpdaterDao;
  }

  public MeasureDao measureDao() {
    return measureDao;
  }

  public UserDao userDao() {
    return userDao;
  }

  public UserGroupDao userGroupDao() {
    return userGroupDao;
  }

  public UserTokenDao userTokenDao() {
    return userTokenDao;
  }

  public GroupMembershipDao groupMembershipDao() {
    return groupMembershipDao;
  }

  public RoleDao roleDao() {
    return roleDao;
  }

  public GroupPermissionDao groupPermissionDao() {
    return groupPermissionDao;
  }

  public PermissionTemplateDao permissionTemplateDao() {
    return permissionTemplateDao;
  }

  public PermissionTemplateCharacteristicDao permissionTemplateCharacteristicDao() {
    return permissionTemplateCharacteristicDao;
  }

  public CeActivityDao ceActivityDao() {
    return ceActivityDao;
  }

  public CeQueueDao ceQueueDao() {
    return ceQueueDao;
  }

  public CeTaskInputDao ceTaskInputDao() {
    return ceTaskInputDao;
  }

  public CeScannerContextDao ceScannerContextDao() {
    return ceScannerContextDao;
  }

  public FileSourceDao fileSourceDao() {
    return fileSourceDao;
  }

  public AuthorDao authorDao() {
    return authorDao;
  }

  public ComponentLinkDao componentLinkDao() {
    return componentLinkDao;
  }

  public EventDao eventDao() {
    return eventDao;
  }

  public PurgeDao purgeDao() {
    return purgeDao;
  }

  public QualityGateDao qualityGateDao() {
    return qualityGateDao;
  }

  public QualityGateConditionDao gateConditionDao() {
    return gateConditionDao;
  }

  public ProjectQgateAssociationDao projectQgateAssociationDao() {
    return projectQgateAssociationDao;
  }

  public DuplicationDao duplicationDao() {
    return duplicationDao;
  }

  public NotificationQueueDao notificationQueueDao() {
    return notificationQueueDao;
  }

  public CustomMeasureDao customMeasureDao() {
    return customMeasureDao;
  }

  public MetricDao metricDao() {
    return metricDao;
  }

  public GroupDao groupDao() {
    return groupDao;
  }

  public RuleDao ruleDao() {
    return ruleDao;
  }

  public RuleRepositoryDao ruleRepositoryDao() {
    return ruleRepositoryDao;
  }

  public ActiveRuleDao activeRuleDao() {
    return activeRuleDao;
  }

  public QProfileChangeDao qProfileChangeDao() {
    return qProfileChangeDao;
  }

  public UserPermissionDao userPermissionDao() {
    return userPermissionDao;
  }

  public WebhookDeliveryDao webhookDeliveryDao() {
    return webhookDeliveryDao;
  }

  protected <K extends Dao> K getDao(Map<Class, Dao> map, Class<K> clazz) {
    return (K) map.get(clazz);
  }

  // should be removed. Still used by some old DAO in sonar-server
  public MyBatis getMyBatis() {
    return myBatis;
  }
}
