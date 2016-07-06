package com.dmg.vm;
public class Programmer {

	private String name;
	public void code(){
		System.out.println("I'm a Programmer,Just Coding.....");
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName(){
		System.out.println("name====="+name);
		return name;
	}
}
