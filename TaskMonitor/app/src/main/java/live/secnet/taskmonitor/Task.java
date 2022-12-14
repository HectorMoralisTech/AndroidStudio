package live.secnet.taskmonitor;

public class Task {

    private int id;
    private String taskName;
    private String taskDesc;
    private String taskDate;
    private boolean taskCompleted;

    public Task(int _id, String _name, String _desc, String _date)
    {
        this.id = _id;
        this.taskName = _name;
        this.taskDesc = _desc;
        this.taskDate = _date;

        this.taskCompleted = false;
    }

    @Override
    public String toString()
    {
        String _val = "Task ID: " + this.id +
                " | Task Name: " + this.taskName + "" +
                " | Task Date: " + this.taskDate +
                " | Task Description: " + this.taskDesc +
                " | Complete Status: " + this.taskCompleted + ".";

        return _val;
    }

    public int getId() { return this.id; }
    public String getTaskName() { return this.taskName; }
    public String getTaskDate() { return this.taskDate; }
    public String getTaskDesc() { return this.taskDesc; }
    public boolean isCompleted() { return this.taskCompleted; }

    public void setTaskName(String _name) { this.taskName = _name; }
    public void setTaskDate(String _date) { this.taskDate = _date; }
    public void setTaskDesc(String _desc) { this.taskDesc = _desc; }
    public void setIsComplete(boolean _val) { this.taskCompleted = _val;}
}

