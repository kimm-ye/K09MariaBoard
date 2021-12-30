package common;
//자바빈스는 기본 패키지 이외 패키지에 있어야 해서 common 패키지를 만들고 그 아래에 작성함.

public class Person {
	//정보은닉을 위해 접근 지정자를 private 해야 한다.
	private String name;
	private int age;
	
	//기본 생성자
	public Person() {}
	//인자 생성자
	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	//외부에 접근하기 위해 getter / setter를 지정해야 한다.
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
