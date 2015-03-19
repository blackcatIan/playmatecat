package com.playmatecat.mina;


import java.io.Serializable;

/**
 * client请求nio server的传递数据适配器
 * @author blackcat
 *
 */
public class NioTransferAdapter implements Serializable {

	private static final long serialVersionUID = 1L;

	/**请求的服务名**/
	private String restServiceName;
	
	/**传递的数据**/
	private String JSONdata;
	
	/**JSONdata所对应的数据类型**/
	private Class<? extends Object> clazz;
	
	public NioTransferAdapter(String JSONdata) {
		this.JSONdata = JSONdata;
	}
	
	public NioTransferAdapter(String restServiceName,String JSONdata,Class<? extends Object> clazz) {
		this.restServiceName = restServiceName;
		this.JSONdata = JSONdata;
		this.clazz = clazz;
	}

	public String getRestServiceName() {
		return restServiceName;
	}

	public void setRestServiceName(String restServiceName) {
		this.restServiceName = restServiceName;
	}

	public String getJSONdata() {
		return JSONdata;
	}

	public void setJSONdata(String jSONdata) {
		JSONdata = jSONdata;
	}

	public Class<? extends Object> getClazz() {
		return clazz;
	}

	public void setClazz(Class<? extends Object> clazz) {
		this.clazz = clazz;
	}

	
}
