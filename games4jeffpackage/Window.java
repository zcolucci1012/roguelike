package games4jeffpackage;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;

public class Window extends Canvas{

	private static final long serialVersionUID = -610221877235495921L;
	private BufferedImageLoader loader = new BufferedImageLoader();
	private String state = "menu";
	private JFrame frame;
	private Main main;

	public Window(int width, int height, String title, Main main){
		frame = new JFrame(title);
		this.main = main;

		frame.setPreferredSize(new Dimension(width, height));
		frame.setMaximumSize(new Dimension(width, height));
		frame.setMinimumSize(new Dimension(width, height));
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(main);
		frame.setVisible(true);
		main.start();

	}

	public void tick(){
		state = main.getState();
		if (!state.equals("menu"))frame.setCursor(frame.getToolkit().createCustomCursor(loader.loadImage("assets/blank.png"), new Point(), null));
	}
}
