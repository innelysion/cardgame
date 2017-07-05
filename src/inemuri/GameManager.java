package inemuri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import inemuri.CardGameObjects.*;
import inemuri.CardGameObjects.Enum.*;

public class GameManager {

	// 队伍和卡组
	private ArrayList<Girl> playerTeam, enemyTeam;
	private ArrayList<Card> playerDeck, enemyDeck;
	// 全体引用list
	private ArrayList<Girl> allGirls;
	private ArrayList<Card> allCards;
	// 对局数据
	private int turns; // 当前回合
	private int phase; // 当前阶段
	private int playerTeamSize, enemyTeamSize; // 队伍人数
	private int playerActivingGirl, enemyActivingGirl; // 正在行动的角色索引
	private int playerHandSize, enemyHandSize; // 手牌上限
	private int playerExtraDraws, enemyExtraDraws; // 额外抽卡次数
	private int playerDeckResetTimes, enemyDeckResetTimes; // 牌库抓空后重置次数
	private HashMap<String, Integer> gameData; // 以上各项int数据的容器，传给卡牌和角色使用

	GameManager() {
		playerTeam = new ArrayList<Girl>();
		enemyTeam = new ArrayList<Girl>();
		playerDeck = new ArrayList<Card>();
		enemyDeck = new ArrayList<Card>();

		turns = 0;
		phase = -1;
		playerTeamSize = enemyTeamSize = 3;
		playerActivingGirl = enemyActivingGirl = -1;
		playerHandSize = enemyHandSize = 10;
		playerExtraDraws = enemyExtraDraws = 0;
		playerDeckResetTimes = enemyDeckResetTimes = 0;
		gameData = new HashMap<String, Integer>();
		updategameData();
	}

	public void mainUpdate() {

		// 步进操作
		if (Input.keyRe.Z && phase < 100) {
			phase++;
		}
		switch (phase) {
		case -1:
			System.out.println("【读取游戏】————————————————————————————————————————————————");
			runGameStartProcess();
			phase++;
			break;
		case 1:
			System.out.println("【双方准备阶段】————————————————————————————————————————————");
			runPrepareProcess();
			phase++;
			break;
		case 3:
			System.out.println("【双方抽排阶段】————————————————————————————————————————————");
			runDrawProcess();
			phase++;
			break;
		case 5:
			System.out.println("【我方行动】————————————————————————————————————————————————");
			runPlayerActionProcess();
			phase++;
			break;
		case 7:
			System.out.println("【敌方行动】————————————————————————————————————————————————");
			runEnemyActionProcess();
			phase++;
			break;
		case 9:
			System.out.println("【结算】————————————————————————————————————————————————————");
			runBattleProcess();
			phase++;
			break;
		case 11:
			System.out.println("【回合结束】————————————————————————————————————————————————");
			runTurnEndProcess();
			phase = 0;
			break;
		case 1000:
			System.out.println("【游戏结束！！！！！】———————————————————————————————————————");
			phase++;
			break;
		}
	}

	private void runPrepareProcess() {
		checkEffects();
		if (checkGameOver()) {
			return;
		}
		playerNextGirl();
		enemyNextGirl();
	}

	private void runDrawProcess() {
		int playerHand = (int) playerDeck.stream().filter(c -> c.isIn(Zone.HAND)).count();
		int enemyHand = (int) enemyDeck.stream().filter(c -> c.isIn(Zone.HAND)).count();
		drrraaaaaaaawCards(playerDeck, playerHandSize - playerHand, true);
		drrraaaaaaaawCards(enemyDeck, enemyHandSize - enemyHand, false);

		System.out.print("我方手牌★");
		playerDeck.stream().filter(c -> c.isIn(Zone.HAND)).forEach(c -> System.out.print("/" + c.getName()));
		System.out.println("★合计" + playerDeck.stream().filter(c -> c.isIn(Zone.HAND)).count() + "张");
		System.out.print("敌方手牌★");
		enemyDeck.stream().filter(c -> c.isIn(Zone.HAND)).forEach(c -> System.out.print("/" + c.getName()));
		System.out.println("★合计" + enemyDeck.stream().filter(c -> c.isIn(Zone.HAND)).count() + "张");

	}

	private void runPlayerActionProcess() {
		System.out.print("我方打出了★");
		playerDeck.stream().filter(c -> c.isIn(Zone.HAND) && !c.isType(Type.EQUIPMENT)).forEach(c -> {
			c.putInto(Zone.PLAYSTACK);
			System.out.print("/" + c.getName());
		});
		System.out.println();
	}

	private void runEnemyActionProcess() {
		System.out.print("敌方打出了★");
		enemyDeck.stream().filter(c -> c.isIn(Zone.HAND) && !c.isType(Type.EQUIPMENT)).forEach(c -> {
			c.putInto(Zone.PLAYSTACK);
			System.out.print("/" + c.getName());
		});
		System.out.println();
	}

	private void runBattleProcess() {
		playStackResolve();
	}

	private void runTurnEndProcess() {
	}

	private void runGameStartProcess() {
		// 添加角色，设置位置
		playerTeam.add(new Girl(1, Party.ALLY));
		playerTeam.add(new Girl(2, Party.ALLY));
		playerTeam.add(new Girl(3, Party.ALLY));
		enemyTeam.add(new Girl(4, Party.ENEMY));
		enemyTeam.add(new Girl(5, Party.ENEMY));
		enemyTeam.add(new Girl(6, Party.ENEMY));
		playerTeam.get(0).setPosition(1);
		playerTeam.get(1).setPosition(2);
		playerTeam.get(2).setPosition(0);
		enemyTeam.get(0).setPosition(0);
		enemyTeam.get(1).setPosition(1);
		enemyTeam.get(2).setPosition(2);

		// 添加卡组
		System.out.print("我方参战角色★");
		playerTeam.stream().forEach(g -> {
			System.out.print("【" + g.getName() + "】");
			playerDeck.addAll(g.getMainDeck(Party.ALLY));
		});
		System.out.println();
		System.out.print("敌方参战角色★");
		enemyTeam.stream().forEach(g -> {
			System.out.print("【" + g.getName() + "】");
			enemyDeck.addAll(g.getMainDeck(Party.ENEMY));
		});
		System.out.println();

		// 设置全体引用
		allGirls = new ArrayList<Girl>();
		allCards = new ArrayList<Card>();
		allGirls.addAll(playerTeam);
		allGirls.addAll(enemyTeam);
		allCards.addAll(playerDeck);
		allCards.addAll(enemyDeck);

		// 洗牌
		playerDeck.forEach(c -> c.putInto(Zone.LIBRARY));
		enemyDeck.forEach(c -> c.putInto(Zone.LIBRARY));
		Collections.shuffle(playerDeck);
		Collections.shuffle(enemyDeck);

		System.out.println("我方队伍卡组★");
		playerDeck.forEach(c -> System.out.print("/" + c.getName()));
		System.out.println();
		System.out.println("敌方队伍卡组★");
		enemyDeck.forEach(c -> System.out.print("/" + c.getName()));
		System.out.println();
	}

	// 抽卡堆叠结算
	private void drawStackResolve(boolean isMyTurn) {
		ArrayList<Card> deck = isMyTurn ? playerDeck : enemyDeck;
		deck.stream().filter(c -> c.isIn(Zone.DRAWSTACK)).forEach(c -> {
			checkEffects(c);
			// 事件卡结算后不经手牌直接进入墓地
			if (c.isType(Type.EVENT)) {
				System.out.println("触发事件★/" + c.getName());
				c.putInto(Zone.GRAVEYARD);
			} else {
				c.putInto(Zone.HAND);
			}
		});
	}

	// 出卡堆叠计算
	private void playStackResolve() {
		allCards.stream().filter(c -> c.isIn(Zone.PLAYSTACK)).forEach(c -> c.putInto(Zone.GRAVEYARD));
		System.out.print("我方弃牌堆数量★");
		System.out.print(playerDeck.stream().filter(c -> c.isIn(Zone.GRAVEYARD)).count() + "     ");
		System.out.print("敌方弃牌堆数量★");
		System.out.print(enemyDeck.stream().filter(c -> c.isIn(Zone.GRAVEYARD)).count());
		System.out.println();
	}

	// 我方下一个角色行动
	private void playerNextGirl() {
		boolean flag = false;
		while (flag == false) {
			// 直到行动标识移动到非战斗不能的队员身上才跳出
			playerActivingGirl += playerActivingGirl < playerTeamSize - 1 ? 1 : -(playerTeamSize - 1);
			if (playerTeam.stream().anyMatch(g -> !g.isDead() && g.getPosition() == playerActivingGirl)) {
				flag = true;
				System.out.println("轮到我方【" + playerTeam.get(playerActivingGirl).getName() + "】行动★");
			}
		}
	}

	// 敌方下一个角色行动
	private void enemyNextGirl() {
		boolean flag = false;
		while (flag == false) {
			// 直到行动标识移动到非战斗不能的队员身上才跳出
			enemyActivingGirl += enemyActivingGirl < enemyTeamSize - 1 ? 1 : -(enemyTeamSize - 1);
			if (enemyTeam.stream().anyMatch(g -> !g.isDead() && g.getPosition() == enemyActivingGirl)) {
				flag = true;
				System.out.println("轮到敌方【" + enemyTeam.get(enemyActivingGirl).getName() + "】行动★");
			}
		}
	}

	// 基础属性处理（伤害)
	private ArrayList<Element> collectElements(ArrayList<Card> cards) {
		return null;
	}

	// 进行全部角色与卡片的特效处理
	private void checkEffects() {
		// 更新对局数据
		updategameData();
		// 处理角色特效
		// allGirls.forEach(g -> {
		// g.setgameData(gameData);
		// allGirls.stream().filter(g.getGirlsSelector()).forEach(g.getGirlsModifier());
		// allCards.stream().filter(g.getCardSelector()).forEach(g.getCardModifier());
		// gameData = g.getgameData();
		// });
		// // 处理卡牌特效
		// allCards.forEach(c -> {
		// c.setgameData(gameData);
		// allGirls.stream().filter(c.getGirlsSelector()).forEach(c.getGirlsModifier());
		// allCards.stream().filter(c.getCardSelector()).forEach(c.getCardModifier());
		// gameData = c.getgameData();
		// });
		// 胜负判定
		checkParty();
	}

	// 只进行某张卡的特效处理
	private void checkEffects(Card c) {
		// 更新对局数据
		updategameData();
		// 处理卡牌特效
		// c.reciveGameData(gameData);
		// allGirls.stream().filter(c.getGirlsSelector()).forEach(c.getGirlsModifier());
		// allCards.stream().filter(c.getCardSelector()).forEach(c.getCardModifier());
		// gameData = c.getgameData();
		// 胜负判定
		checkParty();
	}

	// 检测并处理战斗不能的角色进行胜负判定，以及重整队列和决定当前行动角色
	private void checkParty() {
		// 检查HP和设置战斗不能
		if (checkGameOver()) {
			return;
		}
		// 检查战斗不能标识，如果当前角色战斗不能则轮换下一名角色行动
		allGirls.stream().filter(g -> g.getHp() <= 0 && !g.isDead()).forEach(g -> {
			g.setDeadOrNot(true);
			if (g.belongs(Party.ALLY) && g.getPosition() == playerActivingGirl) {
				playerNextGirl();
			} else if (g.belongs(Party.ENEMY) && g.getPosition() == enemyActivingGirl) {
				enemyNextGirl();
			}
		});
	}

	private boolean checkGameOver() {
		// 一方所有角色HP为0则判负，两方同时全灭平局
		boolean p = playerTeam.stream().allMatch(g -> g.getHp() <= 0);
		boolean e = enemyTeam.stream().allMatch(g -> g.getHp() <= 0);
		boolean gameover = p || e;
		if (gameover) {
			phase = gameover ? 999 : phase;
			System.out.println((p && e ? "平局★" : p ? "敌方胜利★" : "我方胜利★"));
		}
		return gameover;
	}

	private void updategameData() {
		gameData.put("phase", phase);
		gameData.put("turns", turns);
		gameData.put("playerTeamSize", playerTeamSize);
		gameData.put("enemyTeamSize", enemyTeamSize);
		gameData.put("playerActivingGirl", playerActivingGirl);
		gameData.put("enemyActivingGirl", enemyActivingGirl);
		gameData.put("playerHandSize", playerHandSize);
		gameData.put("enemyHandSize", enemyHandSize);
		gameData.put("playerExtraDraws", playerExtraDraws);
		gameData.put("enemyExtraDraws", enemyExtraDraws);
		gameData.put("playerDeckResetTimes", playerDeckResetTimes);
		gameData.put("enemyDeckResetTimes", enemyDeckResetTimes);
	}

	// 抽卡处理和抽卡堆叠处理
	private void drrraaaaaaaawCards(ArrayList<Card> deck, int times, boolean isMyTurn) {
		boolean gameover = false;
		// 抽卡次数
		for (int i = 0; i < times; i++) {
			// 如果牌库被抽空，则将墓地（墓地也为空则判负）洗入牌库后，再进行抽卡
			if (deck.stream().noneMatch(c -> c.isIn(Zone.LIBRARY))) {
				if (deck.stream().noneMatch(c -> c.isIn(Zone.GRAVEYARD))) {
					// 判负
					System.out.println((isMyTurn ? "我方" : "敌方") + "无法继续抽卡★");
					gameover = true;
					break;
				}
				deck.stream().filter(c -> c.isIn(Zone.GRAVEYARD)).forEach(c -> c.putInto(Zone.LIBRARY));
				if (isMyTurn){
					playerDeckResetTimes++;
					System.out.println("我方第" + playerDeckResetTimes + "次重洗★");
				} else {
					enemyDeckResetTimes++;
					System.out.println("敌方第" + enemyDeckResetTimes + "次重洗★");
				}
			}
			// 抽卡处理，将一张卡从牌库置入抽卡堆叠
			deck.stream().filter(c -> c.isIn(Zone.LIBRARY)).findFirst().get().putInto(Zone.DRAWSTACK);
		}
		// 无法抽卡则返回
		if (gameover) {
			phase = 999;
			return;
		}
		// 堆叠处理
		drawStackResolve(isMyTurn);
		// 堆叠中卡牌特效所得的额外抽卡数处理
		if (isMyTurn) {
			if (playerExtraDraws > 0) {
				int exdraws = playerExtraDraws;
				playerExtraDraws = 0;
				System.out.println("我方额外抽" + exdraws + "张卡★");
				drrraaaaaaaawCards(deck, exdraws, isMyTurn);
			}
		} else {
			if (enemyExtraDraws > 0) {
				int exdraws = enemyExtraDraws;
				System.out.println("敌方额外抽" + exdraws + "张卡★");
				enemyExtraDraws = 0;
				drrraaaaaaaawCards(deck, exdraws, isMyTurn);
			}
		}
	}

	// 输入操作前的更新内容(比mainUpdate更早)
	public void firstUpdate() {
	}

	// 描绘操作后的更新内容(在mainUpdate和drawMain之后)
	public void lateUpdate() {
	}

}
