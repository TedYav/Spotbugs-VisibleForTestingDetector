package io.tedy.spotbugsplugin.visiblefortestingdetector;

import com.google.common.annotations.VisibleForTesting;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.ba.XMethod;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.DescriptorFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Optional;


public class VisibleForTestingDetector extends BytecodeScanningDetector {
  public static final String VISIBLE_FOR_TESTING_ABUSE_BUG_TYPE = "TVIS_BAD_INVOKE_VISIBLE_FOR_TESTING";
  private static final ClassDescriptor VISIBLE_FOR_TESTING_DESCRIPTOR =
      DescriptorFactory.createClassDescriptor(VisibleForTesting.class);

  private final BugReporter bugReporter;

  public VisibleForTestingDetector(BugReporter bugReporter) {
    this.bugReporter = bugReporter;
    log("\n\n\n###################\nSTARTING RUN AT: " + new Date().toString() + "\n###################\n");
  }

  @Override
  public void sawMethod() {
    checkMethodAccess();
  }

  @Override
  public void sawIMethod() {
    checkMethodAccess();
  }

  private void checkMethodAccess() {
    XMethod invokedMethod = getXMethodOperand();
    XMethod invokingMethod = getXMethod();

    logInfo(invokedMethod, invokingMethod);
    if (isVisibleForTesting(invokedMethod)) {
      if (!wasInvocationPermitted(invokingMethod, invokedMethod)) {
        bugReporter.reportBug(generateBug());
      }
    }
  }

  private void logInfo(XMethod invokedMethod, XMethod invokingMethod) {
    log("InvokedMethod: " + xMethodToString(invokedMethod));
    log("InvokingMethod: " + xMethodToString(invokingMethod));
    log("\n");
  }

  private String xMethodToString(XMethod xMethod) {
    return Optional.ofNullable(xMethod).map(XMethod::getMethodDescriptor).map(Object::toString).orElse("NULL");
  }

  private boolean isVisibleForTesting(XMethod method) {
    return method != null && method.getAnnotation(VISIBLE_FOR_TESTING_DESCRIPTOR) != null;
  }

  private boolean wasInvocationPermitted(XMethod invokingMethod, XMethod invokedMethod) {
    return isMethodInTestClass(invokingMethod) || areMethodsInSameClass(invokingMethod, invokedMethod);
  }

  private boolean isMethodInTestClass(XMethod method) {
    ClassDescriptor classDescriptor = method.getClassDescriptor();
    return classDescriptor.getClassName().endsWith("Test");
  }

  private boolean areMethodsInSameClass(XMethod invokingMethod, XMethod invokedMethod) {
    ClassDescriptor invokingClassDescriptor = invokingMethod.getClassDescriptor();
    ClassDescriptor invokedClassDescriptor = invokedMethod.getClassDescriptor();
    return invokingClassDescriptor.equals(invokedClassDescriptor);
  }

  private BugInstance generateBug() {
    return new BugInstance(this, VISIBLE_FOR_TESTING_ABUSE_BUG_TYPE, HIGH_PRIORITY)
        .addClassAndMethod(this)
        .addCalledMethod(this)
        .addSourceLine(this, getPC());
  }

  private static final void log(String text) {
    try {
      String textToWrite = text + "\n";
      Path outFile = Paths.get("/Users/tyavuzku/out.txt");
      Files.write(outFile, textToWrite.getBytes(), StandardOpenOption.APPEND);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
