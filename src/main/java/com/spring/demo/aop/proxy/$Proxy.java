package com.spring.demo.aop.proxy;
import com.spring.demo.aop.dao.DemoDao;
public class $Proxy implements DemoDao{
	private DemoDao target;
	public $Proxy (DemoDao target){
		this.target =target;
	}
	public void query(String p0,Integer p) {
		System.out.println("log");
		 target.query(p0,p);
	}
	public void query(String p) {
		System.out.println("log");
		 target.query(p);
	}
	public String getSql(String p) {
		System.out.println("log");
		return target.getSql(p);
	}
}