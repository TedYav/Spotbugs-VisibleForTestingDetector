package io.tedy.spotbugsplugin.visiblefortestingdetector;

public class ClassWhichAbusesExposedMethods {
  public void abuseExposedMethods() {
    ClassWithExposedMethods sut = new ClassWithExposedMethods();
    sut.privateMethodThatIsExposedForTesting(); // Failure 1
    ClassWithExposedMethods.privateStaticMethodThatIsExposedForTesting(); // Failure 2
  }

  public static void abuseExposedMethodsStatically() {
    ClassWithExposedMethods sut = new ClassWithExposedMethods();
    sut.privateMethodThatIsExposedForTesting(); // Failure 3
    ClassWithExposedMethods.privateStaticMethodThatIsExposedForTesting(); // Failure 4
  }
}
