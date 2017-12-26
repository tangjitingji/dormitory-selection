package cn.edu.pku.tangjiting.dormitoryselection.entity;

import java.io.Serializable;

public class StudentResponse {
    private String code;
    private User data;

    public String getErrcode() {
        return code;
    }

    public void setErrcode(String errcode) {
        this.code = errcode;
    }

    public void setData(User data){
        this.data = data;
    }
    public User getData(){
        return this.data;
    }

    @Override
    public String toString() {
        return "StudentResponse{" +
                "errcode='" + code + '\'' +
                ", data=" + data +
                '}';
    }

    public static class User implements Serializable{
        private String studentid;
        private String name;
        private String gender;
        private String vcode;
        private String location;
        private int grade;

        public String getStudentid() {
            return studentid;
        }

        public void setStudentid(String studentid) {
            this.studentid = studentid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getVcode() {
            return vcode;
        }

        public void setVcode(String vcode) {
            this.vcode = vcode;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        @Override
        public String toString() {
            return "User{" +
                    "studentid='" + studentid + '\'' +
                    ", name='" + name + '\'' +
                    ", gender='" + gender + '\'' +
                    ", vcode='" + vcode + '\'' +
                    ", location='" + location + '\'' +
                    ", grade=" + grade +
                    '}';
        }
    }


}
