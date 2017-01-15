package com.jcif.awt;

import java.awt.Component;

import javax.swing.JFrame;

import com.jcif.mvc.ViewProvider;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class JFXColorPickerCtrl implements ViewProvider {

	public JFXPanel fxPanel = new JFXPanel();

	public static void main(String[] args) {
		// This method is invoked on the EDT thread
		JFrame frame = new JFrame("Swing and JavaFX");
		JFXColorPickerCtrl fx = new JFXColorPickerCtrl();
		frame.add(fx.getView());
		frame.setSize(300, 200);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public JFXColorPickerCtrl() {

		fxPanel.setSize(100, 50);
		fxPanel.setMinimumSize(fxPanel.getSize());
		fxPanel.setPreferredSize(fxPanel.getSize());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				initFX(fxPanel);
			}
		});
	}

	private static void initFX(JFXPanel fxPanel) {
		// This method is invoked on the JavaFX thread
		Scene scene = createScene();
		fxPanel.setScene(scene);
	}

	private static Scene createScene() {
		Scene scene = new Scene(new VBox(20), 300, 300);
		scene.setFill(Color.web("#ccffcc"));
		VBox box = (VBox) scene.getRoot();

		ToolBar tb = new ToolBar();
		box.getChildren().add(tb);

		final ColorPicker colorPicker = new ColorPicker();
		tb.getItems().addAll(colorPicker);

		StackPane stack = new StackPane();
		box.getChildren().add(stack);

		colorPicker.setOnAction(new EventHandler() {
			@Override
			public void handle(Event t) {
				System.err.println(colorPicker.getValue());
			}
		});

		return scene;
	}

	@Override
	public Component getView() {

		return fxPanel;
	}
}
