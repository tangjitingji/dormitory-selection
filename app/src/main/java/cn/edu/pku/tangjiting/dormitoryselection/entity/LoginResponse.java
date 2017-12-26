package cn.edu.pku.tangjiting.dormitoryselection.entity;


public class LoginResponse {
    private String errcode;

    private Data data;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "errcode='" + errcode + '\'' +
                ", data=" + data +
                '}';
    }

    public static class Data
    {
        private String errmsg;

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "errmsg='" + errmsg + '\'' +
                    '}';
        }
    }
}
