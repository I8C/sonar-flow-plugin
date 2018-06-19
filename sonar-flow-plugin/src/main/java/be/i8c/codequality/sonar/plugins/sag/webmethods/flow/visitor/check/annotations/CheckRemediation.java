package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRemediation {
  String func();
  String constantCost() default "";
  String linearDesc() default "";
  String linearOffset() default "";
  String linearFactor() default "";
}
