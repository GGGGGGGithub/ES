package com.huawei.helpcenter.pojo;
/**
 * 创建集群对象
 * @author Dreamer
 *
 */
public final class Colony {
	private String  project_id;//项目编码
	private Object cluster;//集群对象
	private Object instance;//实例对象
	private String name;//集群名称
	private Integer instanceNum;//集群实例个数，取值范围为1～32
	public String getProject_id() {
		return project_id;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public Object getCluster() {
		return cluster;
	}
	public void setCluster(Object cluster) {
		this.cluster = cluster;
	}
	public Object getInstance() {
		return instance;
	}
	public void setInstance(Object instance) {
		this.instance = instance;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getInstanceNum() {
		return instanceNum;
	}
	public void setInstanceNum(Integer instanceNum) {
		this.instanceNum = instanceNum;
	}
	
}
