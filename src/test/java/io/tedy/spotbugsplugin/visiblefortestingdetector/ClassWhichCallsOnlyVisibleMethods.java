package io.tedy.spotbugsplugin.visiblefortestingdetector;

public class ClassWhichCallsOnlyVisibleMethods {
  public void testPublicMethod() {
    ClassWithExposedMethods sut = new ClassWithExposedMethods();
    sut.validApiMethod(); // OK
  }

  public void testProtectedMethod() {
    ClassWithExposedMethods sut = new ClassWithExposedMethods();
    sut.validProtectedMethod(); // OK
  }

  public void testPackagePrivateMethod() {
    ClassWithExposedMethods sut = new ClassWithExposedMethods();
    sut.validPackagePrivateMethod(); // OK
  }
}
