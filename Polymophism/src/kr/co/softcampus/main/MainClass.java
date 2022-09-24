package kr.co.softcampus.main;

import kr.co.softcampus.beans.HelloWorld;
import kr.co.softcampus.beans.HelloWorldEn;
import kr.co.softcampus.beans.HelloWorldKo;

public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloWorld hello1 = new HelloWorldKo();
		callMethod(hello1);
	}
	
	public static void callMethod(HelloWorld hello) {
		hello.sayHello();
	}

}
