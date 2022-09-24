package kr.co.softcampus.main;

import kr.co.softcampus.beans.HelloWorldEn;
import kr.co.softcampus.beans.HelloWorldKo;

public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		HelloWorldEn hello = new HelloWorldEn();
		HelloWorldKo hello = new HelloWorldKo();
		callMethod(hello);
	}
	
	public static void callMethod(HelloWorldKo hello) {
		hello.sayHello();
	}
}
