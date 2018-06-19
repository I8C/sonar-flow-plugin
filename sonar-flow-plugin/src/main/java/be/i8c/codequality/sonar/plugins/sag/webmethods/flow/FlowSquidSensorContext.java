package be.i8c.codequality.sonar.plugins.sag.webmethods.flow;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.sonar.api.batch.sensor.issue.NewIssue;

/**
 * @author DEWANST
 * This context stores information that is needed between flow files and node files.
 */
public class FlowSquidSensorContext {

  private HashSet<String> dependencies;
  private HashMap<URI,List<NewIssue>> topLevelIssues;
  
  public FlowSquidSensorContext() {
    super();
    dependencies = new HashSet<String>();
    topLevelIssues = new HashMap<URI,List<NewIssue>>();
  }


  public void addDependencies(Collection<String> newDependencies) {
    dependencies.addAll(newDependencies);
  }
  
  public HashSet<String> getDependencies() {
    return dependencies;
  }

  public void setDependencies(HashSet<String> dependencies) {
    this.dependencies = dependencies;
  }
  
  public void addTopLevelIssue(URI uri, NewIssue issue) {
    if(topLevelIssues.containsKey(uri)) {
      topLevelIssues.get(uri).add(issue);
    }else {
      ArrayList<NewIssue> list = new ArrayList<NewIssue>();
      list.add(issue);
      topLevelIssues.put(uri, list);
    }
  }
  
  public HashMap<URI,List<NewIssue>> getTopLevelIssues() {
    return topLevelIssues;
  }

  public void setTopLevelIssues(HashMap<URI,List<NewIssue>> topLevelIssues) {
    this.topLevelIssues = topLevelIssues;
  }
  
}
