package io.tedy.spotbugsplugin.visiblefortestingdetector;

public class ClassWithExposedMethodsTest {
  public void testPublicMethod() {
    ClassWithExposedMethods sut = new ClassWithExposedMethods();
    sut.validApiMethod(); // OK
  }

  public void testMethodsExposedForTesting() {
    ClassWithExposedMethods sut = new ClassWithExposedMethods();
    sut.privateMethodThatIsExposedForTesting(); // OK
    ClassWithExposedMethods.privateStaticMethodThatIsExposedForTesting(); // OK
  }
}
