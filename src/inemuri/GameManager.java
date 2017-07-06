package inemuri;

public class GameManager {

	GameBattle battle = new GameBattle(null, null);

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
