package vip.yeee.memoo.base.model.rest;

import java.io.Serializable;

/**
 * 封装API的错误码
 */
public interface IErrorCode extends Serializable {

    /**
     * 错误码
     * @return
     */
    long getCode();

    /**
     * 错误信息
     * @return
     */
    String getMessage();
}
