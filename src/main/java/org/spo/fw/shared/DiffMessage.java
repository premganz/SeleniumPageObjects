package org.spo.fw.shared;



public class DiffMessage {
	private boolean   passed=true;  
	private boolean   failed=true;  
	
	private String logFull="";
	
	private String diff="";
	private String diffInverse="";
	private String errorLog="";
	private String errorSection="";
	private String ruleComments;//where the failing rule logs directly.
	
	
	
	public String getRuleComments() {
		return ruleComments;
	}
	public void setRuleComments(String ruleComments) {
		this.ruleComments = ruleComments;
	}
	public String getErrorSection() {
		return errorSection;
	}
	public void setErrorSection(String errorSection) {
		this.errorSection = errorSection;
	}
	public boolean isPassed() {
		return passed;
	}
	public void setPassed(boolean passed) {
		this.passed = passed;
	}
	public String getLogFull() {
		return logFull;
	}
	public void setLogFull(String logFull) {
		this.logFull = logFull;
	}
	public String getDiff() {
		return diff;
	}
	public void setDiff(String diff) {
		this.diff = diff;
	}
	public String getDiffInverse() {
		return diffInverse;
	}
	public void setDiffInverse(String diffInverse) {
		this.diffInverse = diffInverse;
	}
	public String getErrorLog() {
		return errorLog;
	}
	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}
	public boolean isFailed() {
		return !passed;
	}
	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	
	
}