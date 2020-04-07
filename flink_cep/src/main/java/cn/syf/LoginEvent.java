package cn.syf;


    public class LoginEvent {
        private String id;
        private String ip;
        private String status;
        private int count;
        public LoginEvent() {
        }
        public LoginEvent(String id, String ip, String status, int count) {
            this.id = id;
            this.ip = ip;
            this.status = status;
            this.count = count;
        }
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getIp() {
            return ip;
        }
        public void setIp(String ip) {
            this.ip = ip;
        }
        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public int getCount() {
            return count;
        }
        public void setCount(int count) {
            this.count = count;
        }
        @Override
        public String toString() {
            return "LoginEvent{" +
                    "id='" + id + '\'' +
                    ", ip='" + ip + '\'' +
                    ", status='" + status + '\'' +
                    ", count=" + count +
                    '}';
        }
    }


