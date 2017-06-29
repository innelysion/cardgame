package inemuri;

import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;

//Input manager
public class Input implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

	// Keyboard status (press)
	static GameKeyPressed keyPr = new GameKeyPressed();
	// Keyborad status (release)
	static GamekeyReleased keyRe = new GamekeyReleased();
	// Mouse status (press)
	static GameMousePressed musPr = new GameMousePressed();
	// Mouse status (release)
	static GameMouseReleased musRe = new GameMouseReleased();

	static int DIR4, DIR8; // 十字と8方向入力
	static int M_X, M_Y, M_W; // カーソル座標とマウスホイール

	// Private variables
	private Insets sz; // ウィンドウの枠
	private boolean left_first = false;
	private boolean right_first = false;

	public void update(JFrame wind) {

		sz = wind.getInsets();
		M_X = MouseInfo.getPointerInfo().getLocation().x - wind.getLocationOnScreen().x - sz.left;
		M_Y = MouseInfo.getPointerInfo().getLocation().y - wind.getLocationOnScreen().y - sz.top;
		keyRe.reset();
		musRe.reset();
	}

	private void dirCheck() {

		if (keyPr.LEFT && keyPr.UP && keyPr.RIGHT && keyPr.DOWN) {
			DIR8 = 0;
			DIR4 = 0;
		} else if (keyPr.LEFT && keyPr.UP && keyPr.RIGHT) {
			DIR8 = 3;
			DIR4 = 3;
		} else if (keyPr.UP && keyPr.RIGHT && keyPr.DOWN) {
			DIR8 = 5;
			DIR4 = 5;
		} else if (keyPr.RIGHT && keyPr.DOWN && keyPr.LEFT) {
			DIR8 = 7;
			DIR4 = 7;
		} else if (keyPr.DOWN && keyPr.LEFT && keyPr.UP) {
			DIR8 = 1;
			DIR4 = 1;
		} else if (keyPr.LEFT && keyPr.UP) {
			DIR8 = 2;
			DIR4 = 1;
		} else if (keyPr.UP && keyPr.RIGHT) {
			DIR8 = 4;
			DIR4 = 3;
		} else if (keyPr.RIGHT && keyPr.DOWN) {
			DIR8 = 6;
			DIR4 = 3;
		} else if (keyPr.DOWN && keyPr.LEFT) {
			DIR8 = 8;
			DIR4 = 1;
		} else if (keyPr.UP && keyPr.DOWN) {
			DIR8 = 0;
			DIR4 = 0;
		} else if (keyPr.LEFT) {
			DIR8 = 1;
			DIR4 = 1;
			if (!keyPr.RIGHT) {
				right_first = false;
			}
			if (left_first != true && right_first != true) {
				left_first = true;
			}
			if (keyPr.RIGHT && left_first) {
				DIR8 = 5;
				DIR4 = 3;
			}
		} else if (keyPr.UP) {
			DIR8 = 3;
			DIR4 = 2;
		} else if (keyPr.RIGHT) {
			DIR8 = 5;
			DIR4 = 3;
			if (!keyPr.LEFT) {
				left_first = false;
			}
			if (right_first != true && left_first != true) {
				right_first = true;
			}
			if (keyPr.LEFT && right_first) {
				DIR8 = 1;
				DIR4 = 1;
			}
		} else if (keyPr.DOWN) {
			DIR8 = 7;
			DIR4 = 4;
		} else {
			left_first = false;
			right_first = false;
			DIR8 = 0;
			DIR4 = 0;
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {

		// TODO Auto-generated method stub
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_SHIFT://
			keyPr.SHIFT = true;
			break;
		case KeyEvent.VK_ESCAPE:
			keyPr.ESC = true;
			break;
		case KeyEvent.VK_LEFT:
			keyPr.LEFT = true;
			break;
		case KeyEvent.VK_UP:
			keyPr.UP = true;
			break;
		case KeyEvent.VK_RIGHT:
			keyPr.RIGHT = true;
			break;
		case KeyEvent.VK_DOWN:
			keyPr.DOWN = true;
			break;
		case KeyEvent.VK_NUMPAD4:
			keyPr.LEFT = true;
			break;
		case KeyEvent.VK_NUMPAD8:
			keyPr.UP = true;
			break;
		case KeyEvent.VK_NUMPAD6:
			keyPr.RIGHT = true;
			break;
		case KeyEvent.VK_NUMPAD2:
			keyPr.DOWN = true;
			break;
		case KeyEvent.VK_Z:
			keyPr.Z = true;
			break;
		case KeyEvent.VK_X:
			keyPr.X = true;
			break;
		case KeyEvent.VK_C:
			keyPr.C = true;
			break;
		}

		dirCheck();

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_SHIFT://
			keyRe.SHIFT = true;
			keyPr.SHIFT = false;
			break;
		case KeyEvent.VK_ESCAPE:
			keyRe.ESC = true;
			keyPr.ESC = false;
			break;
		case KeyEvent.VK_LEFT:
//			keyRe.LEFT = true;
			keyPr.LEFT = false;
			break;
		case KeyEvent.VK_UP:
//			keyRe.UP = true;
			keyPr.UP = false;
			break;
		case KeyEvent.VK_RIGHT:
//			keyRe.RIGHT = true;
			keyPr.RIGHT = false;
			break;
		case KeyEvent.VK_DOWN:
//			keyRe.DOWN = true;
			keyPr.DOWN = false;
			break;
		case KeyEvent.VK_NUMPAD4:
//			keyRe.LEFT = true;
			keyPr.LEFT = false;
			break;
		case KeyEvent.VK_NUMPAD8:
//			keyRe.UP = true;
			keyPr.UP = false;
			break;
		case KeyEvent.VK_NUMPAD6:
//			keyRe.RIGHT = true;
			keyPr.RIGHT = false;
			break;
		case KeyEvent.VK_NUMPAD2:
//			keyRe.DOWN = true;
			keyPr.DOWN = false;
			break;
		case KeyEvent.VK_Z:
			keyRe.Z = true;
			keyPr.Z = false;
			break;
		case KeyEvent.VK_X:
			keyRe.X = true;
			keyPr.X = false;
			break;
		case KeyEvent.VK_C:
			keyRe.C = true;
			keyPr.C = false;
			break;
		default:
//			keyRe.SHIFT = false;
////			keyRe.ESC = false;
//			keyRe.LEFT = false;
//			keyRe.UP = false;
//			keyRe.RIGHT = false;
//			keyRe.DOWN = false;
			keyRe.Z = false;
			keyRe.X = false;
			keyRe.C = false;
		}

		dirCheck();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

		// MB = arg0.getButton();
		switch (arg0.getButton()) {
		case 1:
			musPr.LBTN = true;
			break;
		case 2:
			musPr.MBTN = true;
			break;
		case 3:
			musPr.RBTN = true;
			break;
		}

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

		// MB = arg0.getButton();
		switch (arg0.getButton()) {
		case 1:
			musRe.LBTN = false;
			break;
		case 2:
			musRe.MBTN = false;
			break;
		case 3:
			musRe.RBTN = false;
			break;
		}

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub

		M_W -= arg0.getWheelRotation();

	}

}

class GameKeyPressed {
	boolean SHIFT, ESC, Z, X, C, LEFT, UP, RIGHT, DOWN;
}

class GamekeyReleased {
	boolean SHIFT, ESC, Z, X, C;// , LEFT, UP, RIGHT, DOWN;
	private boolean rSHIFT, rESC, rZ, rX, rC;// , rLEFT, rUP, rRIGHT, rDOWN;

	void reset() {
		// Maybe Add more
		boolean[] key = { SHIFT, ESC, Z, X, C };// LEFT, UP, RIGHT, DOWN };
		boolean[] keyr = { rSHIFT, rESC, rZ, rX, rC };// , rLEFT, rUP, rRIGHT,
														// rDOWN };
		for (int i = 0; i < key.length; i++) {
			if (key[i] && keyr[i]) {
				key[i] = false;
				keyr[i] = false;
			} else if (key[i]) {
				keyr[i] = true;
			}
		}
		// Maybe Add more
		SHIFT = key[0];
		ESC = key[1];
		Z = key[2];
		X = key[3];
		C = key[4];
//		LEFT = key[5];
//		RIGHT = key[6];
//		UP = key[7];
//		DOWN = key[8];
		// Maybe Add more
		rSHIFT = keyr[0];
		rESC = keyr[1];
		rZ = keyr[2];
		rX = keyr[3];
		rC = keyr[4];
//		rLEFT = keyr[5];
//		rRIGHT = keyr[6];
//		rUP = keyr[7];
//		rDOWN = keyr[8];
	}
}

class GameMousePressed {
	boolean LBTN, MBTN, RBTN;
}

class GameMouseReleased {
	boolean LBTN, MBTN, RBTN;
	private boolean rLBTN, rMBTN, rRBTN;

	void reset() {
		if (LBTN && rLBTN) {
			LBTN = false;
			rLBTN = false;
		} else if (LBTN) {
			rLBTN = true;
		}
		if (MBTN && rMBTN) {
			MBTN = false;
			rMBTN = false;
		} else if (MBTN) {
			rMBTN = true;
		}
		if (RBTN && rRBTN) {
			RBTN = false;
			rRBTN = false;
		} else if (RBTN) {
			rRBTN = true;
		}
	}
}