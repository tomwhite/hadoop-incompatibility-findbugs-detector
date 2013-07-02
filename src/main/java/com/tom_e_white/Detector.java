package com.tom_e_white;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

public class Detector extends OpcodeStackDetector {
    private BugReporter bugReporter;

    public Detector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
    	System.out.println("tw: seen " + seen);
		if ((seen == INVOKEVIRTUAL || seen == INVOKEINTERFACE) && getClassConstantOperand().equals("org/apache/hadoop/mapreduce/JobContext")) {
	    	System.out.println("tw: bug! method invocation on " + getClassConstantOperand());
			bugReporter.reportBug(new BugInstance(this, "HADOOP_COMPATIBILITY_BUG", NORMAL_PRIORITY)
			                            .addClassAndMethod(this).addSourceLine(this));
		}
	}	
	
}
