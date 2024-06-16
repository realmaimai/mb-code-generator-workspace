package com.maimai.controller;

import com.maimai.entities.vo.ResponseVO;

import com.maimai.enums.ResponseCodeEnum;

public class BaseController {
    protected static final String STATUS_SUCCESS = "success";

    protected static final String STATUS_ERROR = "error";

    protected<T> ResponseVO<T> getSuccessResponseVO(T data) {
        ResponseVO<T> responseVO = new ResponseVO<>();
        responseVO.setStatus(STATUS_SUCCESS);
        responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        responseVO.setData(data);
        return responseVO;
    }
}
