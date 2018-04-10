package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.visitor.check.type;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface FlowCheckProperties {
  FlowCheckProperty[] value();
}