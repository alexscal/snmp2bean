package com.rogueai.framework.snmp2bean.exception;

public class AssertFailureException extends RuntimeException {

    private static final long serialVersionUID = -8490651697630543342L;

    public AssertFailureException(String msg) {
        super(msg);
    }

}
