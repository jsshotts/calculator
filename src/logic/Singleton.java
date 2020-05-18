package logic;

import java.util.Stack;

public class Singleton {
	
	private static Singleton instance;
	private Stack<Double> hist = new Stack<Double>();
	
	private Singleton() { }
	
	public static Singleton getInstance()
	{
		if (instance == null)
			instance = new Singleton();
		return instance;
	}
	
	public void add(Double x) { hist.push(x); }
	public Double prev() { return hist.pop(); }
}
