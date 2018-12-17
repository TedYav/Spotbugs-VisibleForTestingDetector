package io.tedy.spotbugsplugin.visiblefortestingdetector;

import static edu.umd.cs.findbugs.test.CountMatcher.containsExactly;
import static org.junit.Assert.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;

import edu.umd.cs.findbugs.BugCollection;
import edu.umd.cs.findbugs.test.SpotBugsRule;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcher;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcherBuilder;

public class VisibleForTestingDetectorTest {
  private static final String TEST_CLASS_ROOT = "target/test-classes";

  @Rule
  public SpotBugsRule spotbugs = new SpotBugsRule();

  @Test
  public void testDetector_allowsInternalUsageOfExposedMethods() {
    testDetector(ClassWithExposedMethods.class, 0);
  }

  @Test
  public void testDetector_allowsUsageOfExposedMethodsInTestClass() {
    testDetector(Arrays.asList(ClassWithExposedMethodsTest.class, ClassWithExposedMethods.class), 0);
  }

  @Test
  public void testDetector_allowsUsageOfExposedMethodsWithoutAnnotations() {
    testDetector(Arrays.asList(ClassWhichCallsOnlyVisibleMethods.class, ClassWithExposedMethods.class), 0);
  }

  @Test
  public void testDetector_flagsOtherUsagesOfExposedMethods() {
    testDetector(Arrays.asList(ClassWhichAbusesExposedMethods.class, ClassWithExposedMethods.class), 4);
  }

  private void testDetector(Class<?> clazz, int expectedNumFailures) {
    testDetector(Collections.singletonList(clazz), expectedNumFailures);
  }

  private void testDetector(List<Class<?>> classes, int expectedNumFailures) {
    Path[] testClassPaths = classes
        .stream()
        .map(VisibleForTestingDetectorTest::getPathToTestClass)
        .toArray(Path[]::new);
    BugCollection bugs = spotbugs.performAnalysis(testClassPaths);

    BugInstanceMatcher bugTypeMatcher = new BugInstanceMatcherBuilder()
        .bugType(VisibleForTestingDetector.VISIBLE_FOR_TESTING_ABUSE_BUG_TYPE)
        .build();

    assertThat(bugs, containsExactly(expectedNumFailures, bugTypeMatcher));
  }

  private static Path getPathToTestClass(Class<?> clazz) {
    return Paths.get(TEST_CLASS_ROOT, clazz.getCanonicalName().replace('.', '/') + ".class");
  }
}
