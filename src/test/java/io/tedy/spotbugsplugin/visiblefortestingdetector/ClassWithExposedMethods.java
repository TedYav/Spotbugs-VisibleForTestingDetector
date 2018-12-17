package io.tedy.spotbugsplugin.visiblefortestingdetector;

import com.google.common.annotations.VisibleForTesting;


public class ClassWithExposedMethods {
  public void validApiMethod() {
    privateMethodThatIsExposedForTesting(); // OK
    privateStaticMethodThatIsExposedForTesting(); // OK
  }

  @VisibleForTesting
  void privateMethodThatIsExposedForTesting() {
    System.out.println("Private method exposed for testing");
  }

  @VisibleForTesting
  static void privateStaticMethodThatIsExposedForTesting() {
    System.out.println("Private static method exposed for testing");
  }

  protected void validProtectedMethod() {
    validApiMethod();
  }

  void validPackagePrivateMethod() {
    validApiMethod();
  }
}
