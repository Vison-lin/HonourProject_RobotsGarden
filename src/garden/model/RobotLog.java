package garden.model;

public class RobotLog {

    private String log;

    public RobotLog() {
        this.log = "";
    }

    public RobotLog(String log) {
        this.log = log;
    }

    public String getLog() {
        return this.log;
    }

    /**
     * Set the current log with a new one.
     * <p>
     * If you want to add new log into existing log, please use method addToLog instead.
     *
     * @param log new log to REPLACE the existing one
     */
    public void setLog(String log) {
        this.log = log;
    }

    /**
     * Add new log into existing log
     * <p>
     * Note that the new log will be inserted into a new line
     *
     * @param msg new message/log to be added
     */
    public void addToLog(String msg) {
        log = log + "\n" + msg + "\n";
    }

    /**
     * Print the whole log for current robot
     *
     * @return the log in String
     */
    @Override
    public String toString() {
        return this.log;
    }
}
