package com.example.mobility.model;

import java.util.List;

public final class ResponseData {

	private List<Object> data;

	private Object tokens;

	private long totalCount;

	public ResponseData(Object response) {
		this.tokens = response;
	}

	/**
	 * @param data
	 * @param totalCount
	 */
	public ResponseData(List<Object> data, long totalCount) {
		this.data = data;
		this.totalCount = totalCount;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public List<Object> getData() {
		return data;
	}

	public void setData(List<Object> data) {
		this.data = data;
	}

	public Object getTokens() {
		return tokens;
	}

	public void setTokens(Object tokens) {
		this.tokens = tokens;
	}

	@Override
	public String toString() {
		return "Response{" + "data=" + data + '}';

	}

	public static ResponseData getRespone(List data) {
		ResponseData list = new ResponseData(data, data.size());
		return list;

	}

	public static ResponseData getRespone(List data, long total) {
		ResponseData list = new ResponseData(data, total);
		return list;

	}

	public static ResponseData getRespone(Object data) {
		return new ResponseData(data);

	}

}
