package inemuri;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferStrategy;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;


class GameMain {

	
	Timer timer = new Timer();
	MainLoop mainloop = new MainLoop();
	JFrame window = new JFrame();
	Insets insets;
	BufferStrategy buffer;

	Input input = new Input();
	GameManager gm = new GameManager();
	
	GameMain() {
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBackground(Color.BLACK);
		window.setResizable(false);
		window.setVisible(true);
		insets = window.getInsets();
		window.setSize(900 + insets.left + insets.right, 600 + insets.top + insets.bottom);
		window.setLocationRelativeTo(null);
		window.setIgnoreRepaint(true);
		window.createBufferStrategy(2);
		buffer = window.getBufferStrategy();
		
		window.addMouseListener(input);
		window.addMouseMotionListener(input);
		window.addKeyListener(input);
		window.addMouseWheelListener(input);
		
		timer.schedule(mainloop, 17, 17);
	}
	
	void dispose(){
		mainloop.cancel();
		timer.cancel();
		timer.purge();
		buffer.dispose();
		window.dispose();
		System.gc();
	}
	
	class MainLoop extends TimerTask {

		public void run() {
			Graphics2D graphic = (Graphics2D) buffer.getDrawGraphics();
			/////////////
			gm.firstUpdate();
			input.update(window);
			gm.mainUpdate();
			/////////////
			if (buffer.contentsLost() == false) {
				graphic.translate(insets.left, insets.top);
				graphic.clearRect(0, 0, 900, 600);
				//////////////////////////
				drawMain(graphic, window);
				gm.lateUpdate();
				//////////////////////////
				buffer.show();
				graphic.dispose();
			}
		}

		private void drawMain(Graphics2D g, JFrame w){
			g.setColor(Color.WHITE);
			g.drawString("HELLOWORLD测试测试テストテスト広い", 100, 100);
		}
	}
}



public class Main {

	public static void main(String[] args) {
		GameMain game = new GameMain();
	}
}
