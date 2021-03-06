package org.robolectric.annotation.processing;

import static org.robolectric.annotation.processing.SingleClassSubject.singleClass;
import static org.truth0.Truth.ASSERT;

import org.junit.Test;

public class ImplementsValidatorTest {
  @Test
  public void implementsWithoutClassOrClassName_shouldNotCompile() {
    final String testClass = "org.robolectric.annotation.processing.shadows.ShadowImplementsWithoutClass";
    ASSERT.about(singleClass())
      .that(testClass)
      .failsToCompile()
      .withErrorContaining("@Implements: must specify <value> or <className>")
      .onLine(5);
  }

  @Test
  public void anything_withoutClassName_shouldNotCompile() {
    final String testClass = "org.robolectric.annotation.processing.shadows.ShadowImplementsAnythingWithoutClassName";
    ASSERT.about(singleClass())
      .that(testClass)
      .failsToCompile()
      .withErrorContaining("@Implements: Anything class specified but no <className> attribute")
      .onLine(6);
  }

  @Test
  public void anything_withUnresolvableClassName_shouldNotCompile() {
    final String testClass = "org.robolectric.annotation.processing.shadows.ShadowImplementsAnythingWithUnresolvableClassName";
    ASSERT.about(singleClass())
      .that(testClass)
      .failsToCompile()
      .withErrorContaining("@Implements: could not resolve class <some.Stuff>")
      .onLine(6);
  }
  
  @Test
  public void value_withClassName_shouldNotCompile() {
    final String testClass = "org.robolectric.annotation.processing.shadows.ShadowImplementsDummyWithOuterDummyClassName";
    ASSERT.about(singleClass())
      .that(testClass)
      .failsToCompile()
      .withErrorContaining("@Implements: cannot specify both <value> and <className> attributes")
      .onLine(7);
  }
}
