package be.i8c.codequality.sonar.plugins.sag.webmethods.flow.metric;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

/**
 * Contains Flow specific metrics
 * @author DEWANST
 */
public class FlowMetrics implements Metrics {

  private static final String DOMAIN = "i8C-Flow-plugin";
  private static final String FLOW_DEPENDENCIES_KEY = "flow.dependencies";
  private static final String FLOW_TOPLEVEL_KEY = "flow.istoplevel";

  
  public static final Metric<Serializable> FLOW_DEPENDENCIES 
    = new Metric.Builder(FLOW_DEPENDENCIES_KEY, "Flow dependencies", Metric.ValueType.DATA)
      .setDescription("Flow dependency list in csv format")
      .setQualitative(false)
      .setDomain(FlowMetrics.DOMAIN)
      .setHidden(false)
      .create();
  
  public static final Metric<Boolean> IS_TOPLEVEL 
  = new Metric.Builder(FLOW_TOPLEVEL_KEY, "Is toplevel flow service", Metric.ValueType.BOOL)
    .setDescription("Is this service a toplevel service within this project? "
        + "This means it has no references from other services.")
    .setQualitative(false)
    .setDomain(FlowMetrics.DOMAIN)
    .setHidden(false)
    .create();
  
  @SuppressWarnings("rawtypes")
  @Override
  public List<Metric> getMetrics() {
    return Arrays.asList(
        FlowMetrics.FLOW_DEPENDENCIES,
        FlowMetrics.IS_TOPLEVEL
);
  }

}
