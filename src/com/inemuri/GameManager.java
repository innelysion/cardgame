package inemuri;

import java.util.ArrayList;

import inemuri.CardGameObjects.Girl;
import inemuri.CardGameObjects.Enum.Party;

public class GameManager {

	ArrayList<Girl> girls;
	GameBattle battle;

	GameManager(){
		girls = new ArrayList<Girl>();
		girls.add(new Girl(1, Party.ALLY));
		girls.add(new Girl(2, Party.ALLY));
		girls.add(new Girl(3, Party.ALLY));
		girls.add(new Girl(4, Party.ENEMY));
		girls.add(new Girl(5, Party.ENEMY));
		girls.add(new Girl(6, Party.ENEMY));
		girls.get(0).setPosition(1);
		girls.get(1).setPosition(2);
		girls.get(2).setPosition(0);
		girls.get(3).setPosition(0);
		girls.get(4).setPosition(1);
		girls.get(5).setPosition(2);

		battle = new GameBattle(girls);
	}

	public void mainUpdate(){
		battle.update();
	}

	// 输入操作前的更新内容(比mainUpdate更早)
	public void firstUpdate() {
	}

	// 描绘操作后的更新内容(在mainUpdate和drawMain之后)
	public void lateUpdate() {
	}
}
