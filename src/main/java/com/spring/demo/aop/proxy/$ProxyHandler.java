package com.spring.demo.aop.proxy;
import com.spring.demo.aop.dao.DemoDao;
import com.spring.demo.aop.handler.MyInvocationHandler;
import java.lang.Exception;
import java.lang.Exception;
import java.lang.reflect.Method;
public class $ProxyHandler implements DemoDao{
	private MyInvocationHandler h;
	public $ProxyHandler (MyInvocationHandler h) {
		this.h = h;
	}
	public void query(String param1,Integer param2) {
		try {
			Method method = Class.forName("com.spring.demo.aop.dao.DemoDao").getDeclaredMethod("query",new Class[]{String.class,Integer.class});
			h.invoke(method, new Object[]{param1,param2});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void query(String param1) {
		try {
			Method method = Class.forName("com.spring.demo.aop.dao.DemoDao").getDeclaredMethod("query",new Class[]{String.class});
			h.invoke(method, new Object[]{param1});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public String getSql(String param1) {
		try {
			Method method = Class.forName("com.spring.demo.aop.dao.DemoDao").getDeclaredMethod("getSql",new Class[]{String.class});
			return (String) h.invoke(method, new Object[]{param1});
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null; 
	}
}