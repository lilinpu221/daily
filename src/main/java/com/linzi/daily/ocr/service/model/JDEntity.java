package com.linzi.daily.ocr.service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JDEntity {
    private String time;
    private String orderNo;
    private String title;
    private String seqNo;
    private String sender;
    private String receiver;
    private String verifyCode;
}
