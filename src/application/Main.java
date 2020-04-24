package application;

import javafx.scene.control.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
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
				new Button(" 7 "), new Button(" 8 "), new Button(" 9 "),
				new Button(" 4 "), new Button(" 5 "), new Button(" 6 "),
				new Button(" 1 "), new Button(" 2 "), new Button(" 3 "),
				new Button(" 0 ")
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
		
		GridPane buttonGrid = new GridPane();
		buttonGrid.setAlignment(Pos.CENTER);
		buttonGrid.setHgap(10);
		buttonGrid.setVgap(10);
		buttonGrid.setPadding(new Insets(25, 25, 25, 25));
		
		VBox vBox = new VBox();
		vBox.getChildren().add(display);
		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().add(buttonGrid);
		
		Button clear = new Button("CE");
		Button sign = new Button("+/-");
		Button decimal = new Button(" . ");
		Button equals = new Button(" = ");
		Button plus = new Button(" + ");
		Button minus = new Button(" - ");
		Button times = new Button(" * ");
		Button divide = new Button(" / ");
		
		clear.setOnAction(e -> {
			op = 0x00; 
			op1 = op2 = 0;
			factor = 10; 
			currentOp = 0; 
			updateDisplay();});
		
		sign.setOnAction(e -> {
			if (currentOp == 0 || currentOp == 3) {
				op1 = op1 * -1;
			} 
			else {
				op2 = op2 * -1;
			} updateDisplay();});
		
		equals.setOnAction(e -> calculate());
		decimal.setOnAction(e -> factor = factor == 10? 0.1 : 10);
		plus.setOnAction(e ->   {op = 0x01; factor = currentOp == 2 ? factor : 10; currentOp = currentOp == 1 ? 1 : 2; updateDisplay();});
		minus.setOnAction(e ->  {op = 0x02; factor = currentOp == 2 ? factor : 10; currentOp = currentOp == 1 ? 1 : 2; updateDisplay();});
		times.setOnAction(e ->  {op = 0x04; factor = currentOp == 2 ? factor : 10; currentOp = currentOp == 1 ? 1 : 2; updateDisplay();});
		divide.setOnAction(e -> {op = 0x08; factor = currentOp == 2 ? factor : 10; currentOp = currentOp == 1 ? 1 : 2; updateDisplay();});

		Button [] numButtons = makeNumberButtons();
		for (int i = 0; i < 3; i++)	{ buttonGrid.add(numButtons[i], i, 2); }
		for (int i = 3; i < 6; i++)	{ buttonGrid.add(numButtons[i], i-3, 3); }
		for (int i = 6; i < 9; i++) { buttonGrid.add(numButtons[i], i-6, 4); }
		
		buttonGrid.add(clear, 0, 1);
		buttonGrid.add(divide,  4, 1);
		buttonGrid.add(times, 4, 2);
		buttonGrid.add(minus, 4, 3);
		buttonGrid.add(plus, 4, 4);
		buttonGrid.add(equals, 4, 5);
		buttonGrid.add(sign, 0, 5);
		buttonGrid.add(numButtons[9], 1, 5);
		buttonGrid.add(decimal, 2, 5);
		
		updateDisplay();
		Scene scene = new Scene(vBox, 250, 250);
		
		window.setTitle("Calculator");
		window.setScene(scene);
		window.show();		
	}
}