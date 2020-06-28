package com.example.music.common.sms;

public class SmsEntity {

    private long sendTs;

    private long expireTs;

    private String code;

    public long getSendTs() {
        return sendTs;
    }

    public void setSendTs(long sendTs) {
        this.sendTs = sendTs;
    }

    public long getExpireTs() {
        return expireTs;
    }

    public void setExpireTs(long expireTs) {
        this.expireTs = expireTs;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
