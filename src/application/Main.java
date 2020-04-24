package application;

import javafx.scene.control.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Main extends Application {
	
	int hSpace = 10;
	int vSpace = 5;
	int op = 0x0;
	double op1 = 0;
	double op2 = 0;
	int currentOp = 0;
	double factor = 10;

	Label display = new Label();
	
	public static void main(String[] args) { launch(args); }
	
	public void updateDisplay() {
		if (currentOp == 0 || currentOp == 3) { display.setText(String.valueOf(op1)); }
		if (currentOp == 1) { display.setText(String.valueOf(op1) + opToString()); }
		if (currentOp == 2) { display.setText(String.valueOf(op1) + opToString() + String.valueOf(op2)); }
	}
	
	public String opToString() {
		if ((op & 0x01) == 0x01) { return " + "; }
		if ((op & 0x02) == 0x02) { return " - "; }
		if ((op & 0x04) == 0x04) { return " * "; }
		if ((op & 0x08) == 0x08) { return " / "; }
		throw new RuntimeException("unexpected operation");
	}
	
	public void calculate() {
		double res = 0;
		if (currentOp == 0) {display.setText(String.valueOf(op1)); return;}
		if (currentOp == 1) {display.setText("a second operand is required"); return;}
		if ((op & 0x01) == 0x01) { res = op1 + op2; display.setText(String.valueOf(op1 + op2)); }
		if ((op & 0x02) == 0x02) { res = op1 - op2; display.setText(String.valueOf(op1 - op2)); }
		if ((op & 0x04) == 0x04) { res = op1 * op2; display.setText(String.valueOf(op1 * op2)); }
		if ((op & 0x08) == 0x08 && op2 == 0) { display.setText(String.valueOf("cannot divide by zero")); }
		else if ((op & 0x08) == 0x08) { res = op1 / op2; display.setText(String.valueOf(op1 / op2)); }
		currentOp = 3;
		op = 0x0;
		op1 = res;
		op2 = 0;
		factor = 10;
	}
	
	public Button[] makeNumberButtons() {
		Button[] numButtons = {
				new Button("7"), new Button("8"), new Button("9"),
				new Button("4"), new Button("5"), new Button("6"),
				new Button("1"), new Button("2"), new Button("3"),
				new Button("0")
			};
			
		for (Button b : numButtons)
		{
			b.setOnAction(e -> {
				if (currentOp == 3) {
					currentOp = 0;
					op1 = 0;
				}
				if (currentOp == 0) {
					currentOp = 0;
					if (factor == 10) {
						op1 = op1 * factor + Double.valueOf(b.getText());
					}
					else {
						op1 = op1 + factor * Double.valueOf(b.getText());
						factor = factor/10;
					}
				}
				else {
					currentOp = 2;
					if (factor == 10) {
						op2 = op2 * factor + Double.valueOf(b.getText());
					}
					else {
						op2 = op2 + factor * Double.valueOf(b.getText());
						factor = factor/10;
					}
				}
				updateDisplay();
			});
		}
		return numButtons;
	}
	
	@Override
	public void start(Stage window) {
		
		VBox rootLayout = new VBox(vSpace);
		HBox r0 = new HBox(hSpace);
		HBox r1 = new HBox(hSpace);
		HBox r2 = new HBox(hSpace);
		HBox r3 = new HBox(hSpace);
		HBox r4 = new HBox(hSpace);
		HBox r5 = new HBox(hSpace);
		
		rootLayout.getChildren().add(r0);
		rootLayout.getChildren().add(r1);
		rootLayout.getChildren().add(r2);
		rootLayout.getChildren().add(r3);
		rootLayout.getChildren().add(r4);
		rootLayout.getChildren().add(r5);
		
		Button clear = new Button("CE");
		clear.setOnAction(e -> {
			op = 0x00; 
			op1 = op2 = 0;
			factor = 10; 
			currentOp = 0; 
			updateDisplay();});
		
		Button sign = new Button("+/-");
		sign.setOnAction(e -> {
			if (currentOp == 0 || currentOp == 3) {
				op1 = op1 * -1;
			} 
			else {
				op2 = op2 * -1;
			} updateDisplay();});
		
		Button decimal = new Button(".");
		decimal.setOnAction(e -> {factor = factor == 10? 0.1 : 10;});
		
		Button equals = new Button("=");
		equals.setOnAction(e -> {calculate();});
		
		Button plus = new Button("+");
		Button minus = new Button("-");
		Button times = new Button("*");
		Button divide = new Button("/");
		
		plus.setOnAction(e ->   {op = 0x01; factor = currentOp == 2 ? factor : 10; currentOp = currentOp == 1 ? 1 : 2; updateDisplay();});
		minus.setOnAction(e ->  {op = 0x02; factor = currentOp == 2 ? factor : 10; currentOp = currentOp == 1 ? 1 : 2; updateDisplay();});
		times.setOnAction(e ->  {op = 0x04; factor = currentOp == 2 ? factor : 10; currentOp = currentOp == 1 ? 1 : 2; updateDisplay();});
		divide.setOnAction(e -> {op = 0x08; factor = currentOp == 2 ? factor : 10; currentOp = currentOp == 1 ? 1 : 2; updateDisplay();});
		
		r0.getChildren().add(clear);
		r0.getChildren().add(display);
		r1.getChildren().add(divide);
		r2.getChildren().add(times);
		r3.getChildren().add(minus);
		r4.getChildren().add(plus);
		r5.getChildren().add(equals);
		
		Button [] numButtons = makeNumberButtons();
		for (int i = 0; i < 3; i++)	{ r2.getChildren().add(numButtons[i]); }
		for (int i = 3; i < 6; i++)	{ r3.getChildren().add(numButtons[i]); }
		for (int i = 6; i < 9; i++) { r4.getChildren().add(numButtons[i]); }
		
		r5.getChildren().add(sign);
		r5.getChildren().add(numButtons[9]);
		r5.getChildren().add(decimal);
		
		updateDisplay();
		Scene scene = new Scene(rootLayout,200,200);
		
		window.setTitle("Calculator");
		window.setScene(scene);
		window.show();		
	}
}