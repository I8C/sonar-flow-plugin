package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckProperties {
  CheckProperty[] value();
}