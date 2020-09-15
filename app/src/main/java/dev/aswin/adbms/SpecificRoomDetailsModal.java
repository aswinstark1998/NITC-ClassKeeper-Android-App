package dev.aswin.adbms;

public class SpecificRoomDetailsModal {
    private int Engaged;
    private String Remarks;
    private String Slot;
    private String Subject;
    private String Teacher;

    public SpecificRoomDetailsModal(){}

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public SpecificRoomDetailsModal(int Engaged, String Remarks, String Slot, String Subject, String Teacher) {
        this.Slot = Slot;
        this.Teacher = Teacher;
        this.Subject = Subject;
        this.Remarks = Remarks;
        this.Engaged = Engaged;
    }

    public String getSlot() {
        return Slot;
    }

    public void setSlot(String Slot) {
        this.Slot = Slot;
    }


    public String getTeacher() {
        return Teacher;
    }

    public void setTeacher(String Teacher) {
        this.Teacher = Teacher;
    }

    public int getEngaged() {
        return Engaged;
    }

    public void setEngaged(int status) {
        this.Engaged = status;
    }
}
