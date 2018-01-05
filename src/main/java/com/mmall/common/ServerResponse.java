package com.mmall.common;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

//保證序列話json的時候，如果是null值，key也會消失
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {

	private int status;
	private String msg;
	private T data;

	private ServerResponse(int status) {
		this.status = status;
	}

	private ServerResponse(int status, T data) {
		this.status = status;
		this.data = data;
	}

	private ServerResponse(int status, String msg) {
		this.status = status;
		this.msg = msg;
	}

	private ServerResponse(int status, String msg, T data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	// 不再json序列化當中
	@JsonIgnore
	public boolean isSuccess() {
		return this.status == ResponseCode.SUCESS.getCode();
	}

	public int getStatus() {
		return status;
	}

	public String getMsg() {
		return msg;
	}

	public T getData() {
		return data;
	}

	// 成功回傳方法
	public static <T> ServerResponse<T> creatBySuccess() {
		return new ServerResponse<T>(ResponseCode.SUCESS.getCode());
	}

	public static <T> ServerResponse<T> creatBySuccessMessage(String msg) {
		return new ServerResponse<T>(ResponseCode.SUCESS.getCode(), msg);

	}

	public static <T> ServerResponse<T> creatBySuccess(T data) {
		return new ServerResponse<T>(ResponseCode.SUCESS.getCode(), data);
	}

	public static <T> ServerResponse<T> creatBySuccess(String msg, T data) {
		return new ServerResponse<T>(ResponseCode.SUCESS.getCode(), msg, data);
	}

	// 失敗回傳方法

	public static <T> ServerResponse<T> creatByError() {
		return new ServerResponse<T>(ResponseCode.ERROR.getCode());
	}

	public static <T> ServerResponse<T> creatByErrorMessage(String errorMessage) {
		return new ServerResponse<T>(ResponseCode.SUCESS.getCode(), errorMessage);

	}

	public static <T> ServerResponse<T> creatByErrorMessage(Integer errorCode, String errorMessage) {
		return new ServerResponse<T>(errorCode, errorMessage);

	}

}
