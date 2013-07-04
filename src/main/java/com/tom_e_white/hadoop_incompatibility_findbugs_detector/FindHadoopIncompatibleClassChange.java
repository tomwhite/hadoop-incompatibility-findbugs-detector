package com.tom_e_white.hadoop_incompatibility_findbugs_detector;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import java.util.Arrays;
import java.util.List;

public class FindHadoopIncompatibleClassChange extends OpcodeStackDetector {
  private static final List<String> INCOMPATIBLE_CLASSES = Arrays.asList(new String[] {
      // Classes changed to interfaces in https://issues.apache.org/jira/browse/MAPREDUCE-954
      "org/apache/hadoop/mapreduce/JobContext",
      "org/apache/hadoop/mapreduce/TaskAttemptContext",
      "org/apache/hadoop/mapreduce/TaskInputOutputContext",
      "org/apache/hadoop/mapreduce/MapContext",
      "org/apache/hadoop/mapreduce/ReduceContext",

      // Classes changed to interfaces in https://issues.apache.org/jira/browse/MAPREDUCE-901
      "org/apache/hadoop/mapreduce/Counter",
      "org/apache/hadoop/mapreduce/CounterGroup",
  });

  private BugReporter bugReporter;

  public FindHadoopIncompatibleClassChange(BugReporter bugReporter) {
    this.bugReporter = bugReporter;
  }

  @Override
  public void sawOpcode(int seen) {
    if ((seen == INVOKEVIRTUAL /* Hadoop 1 */ || seen == INVOKEINTERFACE /* Hadoop 2 */)
        && INCOMPATIBLE_CLASSES.contains(getClassConstantOperand())) {
      bugReporter.reportBug(new BugInstance(this, "HADOOP_CLASS_CHANGE_INCOMPATIBILITY_BUG",
          NORMAL_PRIORITY)
          .addClassAndMethod(this)
          .addString(getDottedClassConstantOperand())
          .addSourceLine(this));
    }
  }

}
