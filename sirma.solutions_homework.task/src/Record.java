import java.util.*;

public class Record {
    private String EmpID;
    private String ProjectID;
    private Date DateFrom;
    private Date DateTo;

    Record(String EmpID, String ProjectID, Date DateFrom, Date DateTo){
        this.EmpID = EmpID;
        this.ProjectID = ProjectID;
        this.DateFrom = DateFrom;
        this.DateTo = DateTo;
    }

    public String getEmployeеID(){
        return EmpID;
    }

    public String getProjectID(){
        return ProjectID;
    }

    public Date getDateFrom() {
        return DateFrom;
    }

    public Date getDateTo() {
        return DateTo;
    }

}
