package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.annotations;

import java.lang.annotation.*;

import org.sonar.api.rules.RuleType;

@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRuleType {
  RuleType ruletype();
  boolean isTemplate() default false;
}
