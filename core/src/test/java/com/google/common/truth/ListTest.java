/*
 * Copyright (c) 2011 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.common.truth;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.common.truth.Truth.assertThat;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static org.junit.Assert.fail;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.tools.JavaFileObject;

/**
 * Tests for List Subjects.
 *
 * @author David Beaumont
 */
@RunWith(JUnit4.class)
public class ListTest {

  @Test public void listContainsSequenceWithEmptyList() {
    assertThat(Arrays.asList(1, 2, 3)).containsSequence(Arrays.<Integer>asList());
  }

  @Test public void listContainsSequenceWithSingleton() {
    assertThat(Arrays.asList(1)).containsSequence(Arrays.asList(1));
  }

  @Test public void listContainsSequenceAtEnd() {
    assertThat(Arrays.asList(1, 2, 3)).containsSequence(Arrays.asList(2, 3));
  }

  @Test public void listContainsSequenceAtStart() {
    assertThat(Arrays.asList(1, 2, 3)).containsSequence(Arrays.asList(1, 2));
  }

  @Test public void listContainsSequenceWithManyFalseStarts() {
    assertThat(Arrays.asList(1, 1, 2, 1, 1, 2, 3, 4)).containsSequence(Arrays.asList(1, 2, 3));
  }

  @Test public void listContainsSequenceTooShortFailure() {
    try {
      assertThat(Arrays.asList(1, 2, 3)).containsSequence(Arrays.asList(1, 2, 3, 4));
      fail("Should have thrown.");
    } catch (AssertionError e) {
      assertThat(e.getMessage()).contains("contains sequence");
      assertThat(e.getMessage()).contains("[1, 2, 3, 4]");
    }
  }

  @Test public void listContainsSequenceNotContiguousFailure() {
    try {
      assertThat(Arrays.asList(1, 2, 2, 3)).containsSequence(Arrays.asList(1, 2, 3));
      fail("Should have thrown.");
    } catch (AssertionError e) {
      assertThat(e.getMessage()).contains("contains sequence");
      assertThat(e.getMessage()).contains("[1, 2, 3]");
    }
  }

  @Test public void listIsOrdered() {
    assertThat(Arrays.<Integer>asList()).isOrdered();
    assertThat(Arrays.asList(1)).isOrdered();
    assertThat(Arrays.asList(1, 2, 3, 4)).isOrdered();
  }

  @Test public void isOrderedFailure() {
    try {
      assertThat(Arrays.asList(1, 2, 2, 4)).isOrdered();
      fail("Should have thrown.");
    } catch (AssertionError e) {
      assertThat(e.getMessage()).contains("is strictly ordered");
      assertThat(e.getMessage()).contains("<2> <2>");
    }
  }

  @Test public void isOrderedWithNonComparableElementsFailure() {
    try {
      assertThat(Arrays.<Object>asList(1, "2", 3, "4")).isOrdered();
      fail("Should have thrown.");
    } catch (ClassCastException e) {}
  }

  @Test public void listIsPartiallyOrdered() {
    assertThat(Arrays.<Integer>asList()).isPartiallyOrdered();
    assertThat(Arrays.asList(1)).isPartiallyOrdered();
    assertThat(Arrays.asList(1, 1, 2, 3, 3, 3, 4)).isPartiallyOrdered();
  }

  @Test public void isPartiallyOrderedFailure() {
    try {
      assertThat(Arrays.asList(1, 3, 2, 4)).isPartiallyOrdered();
      fail("Should have thrown.");
    } catch (AssertionError e) {
      assertThat(e.getMessage()).contains("is partially ordered");
      assertThat(e.getMessage()).contains("<3> <2>");
    }
  }

  @Test public void isPartiallyOrderedWithNonComparableElementsFailure() {
    try {
      assertThat(Arrays.<Object>asList(1, "2", 2, "3")).isPartiallyOrdered();
      fail("Should have thrown.");
    } catch (ClassCastException e) {}
  }

  @Test public void listIsOrderedWithComparator() {
    assertThat(Arrays.<String>asList()).isOrdered(COMPARE_AS_DECIMAL);
    assertThat(Arrays.asList("1")).isOrdered(COMPARE_AS_DECIMAL);
    // Note: Use "10" and "20" to distinguish numerical and lexicographical ordering.
    assertThat(Arrays.asList("1", "2", "10", "20")).isOrdered(COMPARE_AS_DECIMAL);
  }

  @Test public void listIsOrderedWithComparatorFailure() {
    try {
      assertThat(Arrays.asList("1", "2", "2", "10")).isOrdered(COMPARE_AS_DECIMAL);
      fail("Should have thrown.");
    } catch (AssertionError e) {
      assertThat(e.getMessage()).contains("is strictly ordered");
      assertThat(e.getMessage()).contains("<2> <2>");
    }
  }

  @Test public void listIsPartiallyOrderedWithComparator() {
    assertThat(Arrays.<String>asList()).isPartiallyOrdered(COMPARE_AS_DECIMAL);
    assertThat(Arrays.asList("1")).isPartiallyOrdered(COMPARE_AS_DECIMAL);
    assertThat(Arrays.asList("1", "1", "2", "10", "10", "10", "20"))
        .isPartiallyOrdered(COMPARE_AS_DECIMAL);
  }

  @Test public void listIsPartiallyOrderedWithComparatorFailure() {
    try {
      assertThat(Arrays.asList("1", "10", "2", "20")).isPartiallyOrdered(COMPARE_AS_DECIMAL);
      fail("Should have thrown.");
    } catch (AssertionError e) {
      assertThat(e.getMessage()).contains("is partially ordered");
      assertThat(e.getMessage()).contains("<10> <2>");
    }
  }

  private static final Comparator<String> COMPARE_AS_DECIMAL = new Comparator<String>() {
    @Override public int compare(String a, String b) {
      return Integer.valueOf(a).compareTo(Integer.valueOf(b));
    }
  };

  private static class Foo {
    private final int x;

    private Foo(int x) {
      this.x = x;
    }
  }

  private static class Bar extends Foo {
    private Bar(int x) {
      super(x);
    }
  }

  private static final Comparator<Foo> FOO_COMPARATOR = new Comparator<Foo>() {
    @Override public int compare(Foo a, Foo b) {
      return Integer.compare(a.x, b.x);
    }
  };

  @Test public void listOrderedByBaseClassComparator() {
    List<Bar> targetList = Arrays.asList(new Bar(1), new Bar(2), new Bar(3));
    assertThat(targetList).isPartiallyOrdered(FOO_COMPARATOR);
    assertThat(targetList).isOrdered(FOO_COMPARATOR);
  }

  @Test public void listOrderedBySubClassDontCompile() {
    JavaFileObject file = JavaFileObjects.forSourceLines(
        "test.MyTest",
        "package test;",
        "import static com.google.common.truth.Truth.assertThat;",
        "import java.util.Arrays;",
        "import java.util.Comparator;",
        "import java.util.List;",
        "class MyTest {",
        "  private static class Foo {",
        "    protected final int x;",
        "    private Foo(int x) {",
        "      this.x = x;",
        "    }",
        "  }",
        "  private static class Bar extends Foo {",
        "    private Bar(int x) {",
        "      super(x);",
        "    }",
        "  }",
        "  private static final Comparator<Bar> BAR_COMPARATOR = new Comparator<Bar>() {",
        "    @Override public int compare(Bar a, Bar b) {",
        "      return Integer.compare(a.x, b.x);",
        "    }",
        "  };",
        "  public void testFoo() {",
        "    List<Foo> targetList = Arrays.asList(new Foo(1), new Foo(2), new Foo(3));",
        "    assertThat(targetList).isOrdered(BAR_COMPARATOR);",
        "  }",
        "}");

    assertAbout(javaSource()).that(file)
        .failsToCompile()
        .withErrorContaining("no suitable method found for isOrdered")
        .in(file)
        .onLine(25);
  }

  @Test public void listPartiallyOrderedBySubClassDontCompile() {
    JavaFileObject file = JavaFileObjects.forSourceLines(
        "test.MyTest",
        "package test;",
        "import static com.google.common.truth.Truth.assertThat;",
        "import java.util.Arrays;",
        "import java.util.Comparator;",
        "import java.util.List;",
        "class MyTest {",
        "  private static class Foo {",
        "    protected final int x;",
        "    private Foo(int x) {",
        "      this.x = x;",
        "    }",
        "  }",
        "  private static class Bar extends Foo {",
        "    private Bar(int x) {",
        "      super(x);",
        "    }",
        "  }",
        "  private static final Comparator<Bar> BAR_COMPARATOR = new Comparator<Bar>() {",
        "    @Override public int compare(Bar a, Bar b) {",
        "      return Integer.compare(a.x, b.x);",
        "    }",
        "  };",
        "  public void testFoo() {",
        "    List<Foo> targetList = Arrays.asList(new Foo(1), new Foo(2), new Foo(3));",
        "    assertThat(targetList).isPartiallyOrdered(BAR_COMPARATOR);",
        "  }",
        "}");

    assertAbout(javaSource()).that(file)
        .failsToCompile()
        .withErrorContaining("no suitable method found for isPartiallyOrdered")
        .in(file)
        .onLine(25);
  }
}