package com.envision.api;

import java.util.List;

public class Test {

	/**
	 * msg : success
	 * metrics : [{"GMT.APProduction":"1438.900000","mdmId":"1c17e79fed006000","timestamp":"2018-09-27"}]
	 * body : {"metrics":[{"timestamp":"2018-09-27","GMT.APProduction":"1438.900000","mdmId":"1c17e79fed006000"}],"status":0,"msg":"success"}
	 * status : 0
	 */

	private String msg;
	private String body;
	private int status;
	private List<MetricsBean> metrics;

	public static void main(String[] args) {

	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<MetricsBean> getMetrics() {
		return metrics;
	}

	public void setMetrics(List<MetricsBean> metrics) {
		this.metrics = metrics;
	}


	public static class MetricsBean {
		@com.alibaba.fastjson.annotation.JSONField(name = "GMT.APProduction")
		private String _$GMTAPProduction100; // FIXME check this code
		private String mdmId;
		private String timestamp;

		public String get_$GMTAPProduction100() {
			return _$GMTAPProduction100;
		}

		public void set_$GMTAPProduction100(String _$GMTAPProduction100) {
			this._$GMTAPProduction100 = _$GMTAPProduction100;
		}

		public String getMdmId() {
			return mdmId;
		}

		public void setMdmId(String mdmId) {
			this.mdmId = mdmId;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}
	}
	
}
