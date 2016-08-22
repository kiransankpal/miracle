package model.am.util;

import java.util.Map;

public class FndUserBean {
    private String UserName;
        private Integer UserId;
        private Integer RespId;
        private Integer RespApplId;
        private Map userInfo;

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserId(Integer UserId) {
            this.UserId = UserId;
        }

        public Integer getUserId() {
            return UserId;
        }

        public void setRespId(Integer RespId) {
            this.RespId = RespId;
        }

        public Integer getRespId() {
            return RespId;
        }

        public void setRespApplId(Integer RespApplId) {
            this.RespApplId = RespApplId;
        }

        public Integer getRespApplId() {
            return RespApplId;
        }

        public void setUserInfo(Map userInfo) {
            this.userInfo = userInfo;
        }

        public Map getUserInfo() {
            return userInfo;
        }
}
