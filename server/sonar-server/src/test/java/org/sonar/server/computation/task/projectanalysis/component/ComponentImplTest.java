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
package org.sonar.server.computation.task.projectanalysis.component;

import java.util.Arrays;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.sonar.server.computation.task.projectanalysis.component.Component.Type.FILE;
import static org.sonar.server.computation.task.projectanalysis.component.ComponentImpl.builder;

public class ComponentImplTest {

  static final String KEY = "KEY";
  static final String UUID = "UUID";

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void verify_key_uuid_and_name() throws Exception {
    ComponentImpl component = buildSimpleComponent(FILE, KEY).setUuid(UUID).setName("name").build();

    assertThat(component.getKey()).isEqualTo(KEY);
    assertThat(component.getUuid()).isEqualTo(UUID);
    assertThat(component.getName()).isEqualTo("name");
  }

  @Test
  public void builder_throws_NPE_if_component_arg_is_Null() {
    thrown.expect(NullPointerException.class);

    builder(null);
  }

  @Test
  public void set_key_throws_NPE_if_component_arg_is_Null() {
    thrown.expect(NullPointerException.class);

    builder(FILE).setUuid(null);
  }

  @Test
  public void set_uuid_throws_NPE_if_component_arg_is_Null() {
    thrown.expect(NullPointerException.class);

    builder(FILE).setKey(null);
  }

  @Test
  public void build_without_key_throws_NPE_if_component_arg_is_Null() {
    thrown.expect(NullPointerException.class);

    builder(FILE).setUuid("ABCD").build();
  }

  @Test
  public void build_without_uuid_throws_NPE_if_component_arg_is_Null() {
    thrown.expect(NullPointerException.class);

    builder(FILE).setKey(KEY).build();
  }

  @Test
  public void get_name_from_batch_component() {
    String name = "project";
    ComponentImpl component = buildSimpleComponent(FILE, "file").setName(name).build();
    assertThat(component.getName()).isEqualTo(name);
  }

  @Test
  public void getFileAttributes_throws_ISE_if_BatchComponent_does_not_have_type_FILE() {
    Arrays.stream(Component.Type.values())
      .filter(type -> type != FILE)
      .forEach((componentType) -> {
        ComponentImpl component = buildSimpleComponent(componentType, componentType.name()).build();
        try {
          component.getFileAttributes();
          fail("A IllegalStateException should have been raised");
        } catch (IllegalStateException e) {
          assertThat(e).hasMessage("Only component of type FILE have a FileAttributes object");
        }
      });
  }

  @Test
  public void isUnitTest_returns_true_if_IsTest_is_set_in_BatchComponent() {
    ComponentImpl component = buildSimpleComponent(FILE, "file").setFileAttributes(new FileAttributes(true, null, 1)).build();

    assertThat(component.getFileAttributes().isUnitTest()).isTrue();
  }

  @Test
  public void isUnitTest_returns_value_of_language_of_BatchComponent() {
    String languageKey = "some language key";
    ComponentImpl component = buildSimpleComponent(FILE, "file").setFileAttributes(new FileAttributes(false, languageKey, 1)).build();

    assertThat(component.getFileAttributes().getLanguageKey()).isEqualTo(languageKey);
  }

  @Test
  public void build_with_child() throws Exception {
    ComponentImpl child = builder(FILE)
      .setName("CHILD_NAME")
      .setKey("CHILD_KEY")
      .setUuid("CHILD_UUID")
      .setReportAttributes(ReportAttributes.newBuilder(2).build())
      .build();
    ComponentImpl componentImpl = builder(Component.Type.DIRECTORY)
      .setName("DIR")
      .setKey(KEY)
      .setUuid(UUID)
      .setReportAttributes(ReportAttributes.newBuilder(1).build())
      .addChildren(child)
      .build();

    assertThat(componentImpl.getChildren()).hasSize(1);
    Component childReloaded = componentImpl.getChildren().iterator().next();
    assertThat(childReloaded.getKey()).isEqualTo("CHILD_KEY");
    assertThat(childReloaded.getUuid()).isEqualTo("CHILD_UUID");
    assertThat(childReloaded.getType()).isEqualTo(FILE);
  }

  @Test
  public void equals_compares_on_uuid_only() {
    ComponentImpl.Builder builder = buildSimpleComponent(FILE, "1").setUuid(UUID);

    assertThat(builder.build()).isEqualTo(builder.build());
    assertThat(builder.build()).isEqualTo(buildSimpleComponent(FILE, "2").setUuid(UUID).build());
    assertThat(builder.build()).isNotEqualTo(buildSimpleComponent(FILE, "1").setUuid("otherUUid").build());
  }

  @Test
  public void hashCode_is_hashcode_of_uuid() {
    ComponentImpl.Builder builder = buildSimpleComponent(FILE, "1").setUuid(UUID);

    assertThat(builder.build().hashCode()).isEqualTo(builder.build().hashCode());
    assertThat(builder.build().hashCode()).isEqualTo(buildSimpleComponent(FILE, "2").setUuid(UUID).build().hashCode());
    assertThat(builder.build().hashCode()).isEqualTo(UUID.hashCode());
  }

  private static ComponentImpl.Builder buildSimpleComponent(Component.Type type, String key) {
    return builder(type)
      .setName("name_" + key)
      .setKey(key)
      .setUuid("uuid_" + key)
      .setReportAttributes(ReportAttributes.newBuilder(key.hashCode())
        .build());
  }
}
