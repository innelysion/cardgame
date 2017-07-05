package inemuri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import inemuri.CardGameObjects.Buff;
import inemuri.CardGameObjects.Card;
import inemuri.CardGameObjects.Element;
import inemuri.CardGameObjects.Girl;
import inemuri.CardGameObjects.Enum.Party;
import inemuri.CardGameObjects.Enum.Type;
import inemuri.CardGameObjects.Enum.Zone;

public class GameBattle {
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
	private int attackP, shieldP, focusP, attackE, shieldE, focusE; // 回合内伤害值,护盾加值,集中值,P=Player,E=Enemy
	private HashMap<String, Integer> gameData; // 以上各项int数据的容器，传给卡牌和角色使用

	GameBattle() {
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
		attackP = shieldP = focusP = attackE = shieldE = focusE = 0;
		gameData = new HashMap<String, Integer>();

		getNowGameData();
	}

	// 对局主循环
	public void update() {
		// 步进操作，偶数阶段为等待输入下一步
		if ((Input.keyRe.ENTER || Input.keyRe.SPACE) && phase < 100) {
			phase++;
		}
		switch (phase) {
		case -1:
			System.out.println("【读取游戏】————————————————————————————————————————————————");
			runGameStartProcess();
			phase++;
			break;
		case 1:
			System.out.println("【回合" + turns + "】");
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
			System.out.print("我方打出了★");
			runPlayerActionProcess();
			System.out.println();
			phase++;
			break;
		case 7:
			System.out.println("【敌方行动】————————————————————————————————————————————————");
			System.out.print("敌方打出了★");
			runEnemyActionProcess();
			System.out.println();
			phase++;
			break;
		case 9:
			System.out.println("【结算】————————————————————————————————————————————————————");
			runBattleProcess();
			System.out.print("我方弃牌堆数量★");
			System.out.print(playerDeck.stream().filter(c -> c.isIn(Zone.GRAVEYARD)).count() + "     ");
			System.out.print("敌方弃牌堆数量★");
			System.out.print(enemyDeck.stream().filter(c -> c.isIn(Zone.GRAVEYARD)).count());
			System.out.println();
			phase++;
			break;
		case 11:
			System.out.println("【回合结束】————————————————————————————————————————————————");
			runTurnEndProcess();
			System.out.println();
			System.out.println();
			phase = 0;
			break;
		case 1000:
			System.out.println("【游戏结束！！！！！】———————————————————————————————————————");
			phase++;
			break;
		}
	}

	/////////////////////////////////////////////////////////////////////////////////
	// 以下为各阶段主处理

	// 阶段1: 回合开始环节
	private void runPrepareProcess() {
		if (checkGameOver()) {
			return;
		}
		playerNextGirl();
		enemyNextGirl();
	}

	// 阶段3: 双方抽卡环节
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

	// 阶段5: 我方出牌环节
	private void runPlayerActionProcess() {
		playerDeck.stream().filter(c -> c.isIn(Zone.HAND) && !c.isType(Type.EQUIPMENT)).forEach(c -> {
			c.putInto(Zone.PLAYSTACK);
			System.out.print("/" + c.getName());
		});
	}

	// 阶段7: 敌方出牌环节
	private void runEnemyActionProcess() {
		enemyDeck.stream().filter(c -> c.isIn(Zone.HAND) && !c.isType(Type.EQUIPMENT)).forEach(c -> {
			c.putInto(Zone.PLAYSTACK);
			System.out.print("/" + c.getName());
		});
	}

	// 阶段8: 战斗及结算环节
	private void runBattleProcess() {
		playStackResolve();
	}

	// 阶段11: 回合结束环节
	private void runTurnEndProcess() {
		// 清空回合内伤害值,护盾加值,集中值
		attackP = shieldP = focusP = attackE = shieldE = focusE = 0;
		turns++;
	}

	// 阶段-1：对局初始化环节（只会在对决开始运行一次）
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
		System.out.println();
		turns++;
	}

	// 以上为各阶段主处理
	/////////////////////////////////////////////////////////////////////////////////

	// buff效果判定
	private void checkBuffs() {
		// 获取所有角色和卡牌中激活的buff
		ArrayList<Buff> activeBuffs = new ArrayList<Buff>();
		allCards.stream().map(c -> c.getBuffs()).forEach(
				list -> list.stream().filter(buff -> buff.isActive()).forEach(legalbuff -> activeBuffs.add(legalbuff)));
		allGirls.stream().map(c -> c.getBuffs()).forEach(
				list -> list.stream().filter(buff -> buff.isActive()).forEach(legalbuff -> activeBuffs.add(legalbuff)));
		// 按优先度排序(优先度越大越靠后结算)
		activeBuffs.sort((b1, b2) -> Integer.compare(b1.getPriority(), b2.getPriority()));
		// 将对决数据传给buff，进行buff主处理，接收返回的对局数据
		activeBuffs.forEach(b -> {
			getNowGameData();
			b.updateGameData(gameData, allGirls, allCards, activeBuffs);
			b.run();
			setModifiedGameDataFromBuff(b);
		});
	}

	// 检测并处理战斗不能的角色进行胜负判定，以及重整队列和决定当前行动角色
	private void checkParty() {
		// 检查HP和设置战斗不能
		if (checkGameOver()) {
			return;
		}
		// 检查战斗不能标识，如果当前角色战斗不能则轮换下一名角色行动
		allGirls.stream().filter(g -> g.getHp() <= 0 && !g.isDead()).forEach(g -> {
			System.out.println(g.getName() + "战斗不能★");
			g.setDeadOrNot(true);
			if (g.belongs(Party.ALLY) && g.getPosition() == playerActivingGirl && phase < 9) {
				playerNextGirl();
			} else if (g.belongs(Party.ENEMY) && g.getPosition() == enemyActivingGirl && phase < 9) {
				enemyNextGirl();
			}
		});
	}

	// 胜负判定
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

	// 抽卡处理和抽卡堆叠处理
	private void drrraaaaaaaawCards(ArrayList<Card> deck, int times, boolean isMyTurn) {
		boolean gameover = false;
		// 抽卡次数
		for (int i = 0; i < times; i++) {
			// 如果牌库被抽空，则将墓地（墓地也为空则判负）洗入牌库
			if (deck.stream().noneMatch(c -> c.isIn(Zone.LIBRARY))) {
				if (deck.stream().noneMatch(c -> c.isIn(Zone.GRAVEYARD))) {
					// 判负
					System.out.println((isMyTurn ? "我方" : "敌方") + "无法继续抽卡★");
					gameover = true;
					break;
				}
				deck.stream().filter(c -> c.isIn(Zone.GRAVEYARD)).forEach(c -> c.putInto(Zone.LIBRARY));
				Collections.shuffle(deck);
				// 重洗次数增加
				playerDeckResetTimes += isMyTurn ? 1 : 0;
				enemyDeckResetTimes += isMyTurn ? 0 : 1;
				System.out.println((isMyTurn ? "我方第" + playerDeckResetTimes : "敌方第" + enemyDeckResetTimes) + "次重洗★");
			}
			// 抽卡处理，将一张卡从牌库置入抽卡堆叠
			deck.stream().filter(c -> c.isIn(Zone.LIBRARY)).findFirst().get().putInto(Zone.DRAWSTACK);
		}
		// 无法抽卡则设置游戏结束并返回
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
				System.out.println("我方额外抽" + exdraws + "张卡★");
				playerExtraDraws = 0;
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

	// 抽卡堆叠结算
	private void drawStackResolve(boolean isMyTurn) {

		ArrayList<Card> deck = isMyTurn ? playerDeck : enemyDeck;
		deck.stream().filter(c -> c.isIn(Zone.DRAWSTACK)).forEach(c -> {
			// 事件卡不经手牌直接进入出牌堆叠
			if (c.isType(Type.EVENT)) {
				System.out.println("触发事件★" + c.getName() + "★" + c.getDescription());
				c.putInto(Zone.PLAYSTACK);
			} else {
				c.putInto(Zone.HAND);
			}
		});
	}

	// 我方下一个角色行动
	private void playerNextGirl() {
		boolean flag = false;
		while (flag == false) {
			// 直到行动标识移动到非战斗不能的队员身上才跳出
			playerActivingGirl += playerActivingGirl < playerTeamSize - 1 ? 1 : -(playerTeamSize - 1);
			if (playerTeam.stream().anyMatch(g -> !g.isDead() && g.getPosition() == playerActivingGirl)) {
				Girl g = playerTeam.get(playerActivingGirl);
				flag = true;
				System.out.println("轮到我方【" + g.getName() + "】行动★HP:" + g.getHp() + " SHIELD:" + g.getShield());
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
				Girl g = enemyTeam.get(enemyActivingGirl);
				flag = true;
				System.out.println("轮到敌方【" + g.getName() + "】行动★HP:" + g.getHp() + " SHIELD:" + g.getShield());
			}
		}
	}

	// 出卡堆叠计算(P = player, E = Enemy)
	private void playStackResolve() {
		// 属性容器
		ArrayList<Element> collectorP = new ArrayList<Element>();
		ArrayList<Element> collectorE = new ArrayList<Element>();
		// 当前行动的对象
		Girl girlP = playerTeam.get(playerActivingGirl);
		Girl girlE = enemyTeam.get(enemyActivingGirl);
		// 收集所有处于合法生效区域卡牌的属性
		playerDeck.stream().filter(GameSetting.legalCheck)
				.forEach(c -> c.getElements().forEach(e -> e.addSameTo(collectorP)));
		enemyDeck.stream().filter(GameSetting.legalCheck)
				.forEach(c -> c.getElements().forEach(e -> e.addSameTo(collectorE)));
		// 将收集到的属性加在回合内伤害/护盾/集中值上
		attackP = collectValue(GameSetting.ATKElements, collectorP, girlP) * girlP.getAtk();
		attackE = collectValue(GameSetting.ATKElements, collectorE, girlE) * girlE.getAtk();
		shieldP = collectValue(new String[] { "盾" }, collectorP, girlP) * girlP.getDef();
		shieldE = collectValue(new String[] { "盾" }, collectorE, girlE) * girlE.getDef();
		focusP = collectValue(new String[] { "集" }, collectorP, girlP);
		focusE = collectValue(new String[] { "集" }, collectorE, girlE);
		// debug信息
		System.out.println("我方★DMG:" + attackP + "/SHD:" + shieldP + "/FUS:" + focusP);
		System.out.println("敌方★DMG:" + attackE + "/SHD:" + shieldE + "/FUS:" + focusE);
		// 进行特效处理
		checkBuffs();
		// 造成普攻伤害
		girlP.hpDamage(attackE - shieldP);
		girlE.hpDamage(attackP - shieldE);
		checkGameOver();
		System.out.println("【" + girlP.getName() + "】★HP:" + girlP.getHp() + " SHIELD:" + girlP.getShield());
		System.out.println("【" + girlE.getName() + "】★HP:" + girlE.getHp() + " SHIELD:" + girlE.getShield());
		// 将堆叠中结算完毕的卡牌丢入弃牌区
		allCards.stream().filter(c -> c.isIn(Zone.PLAYSTACK)).forEach(c -> {
			c.putInto(Zone.GRAVEYARD);
		});
	}

	// 属性效果总值，应用属性加减/乘除修正器和主属性修正
	private int collectValue(String[] name, ArrayList<Element> list, Girl girl) {
		int result = 0;
		// 按传入的名称数组寻找同名属性并计算效果合计值，加到结果上
		for (String n : name) {
			result += list.stream().filter(e -> e.getName() == n).mapToInt(e -> {
				// 比较是否和角色主属性匹配，匹配则效果翻倍
				int mainMod = e.anySameIn(girl.getElements()) ? 2 : 1;
				if (e.getValueBase() >= 0) {
					return (e.getValueBase() * mainMod + e.getValueAddMod()) * e.getValueMultiMod();
				} else {
					// 如果属性基础值小于0则直接返回0（治疗不在此计算）
					return 0;
				}
			}).sum();
		}
		return result;
	}

	// 将被BUFF修正的数据回传
	private void setModifiedGameDataFromBuff(Buff b) {
		gameData = b.returnGameData();
		turns = gameData.get("turns");
		phase = gameData.get("phase");
		playerTeamSize = gameData.get("playerTeamSize");
		enemyTeamSize = gameData.get("enemyTeamSize");
		playerActivingGirl = gameData.get("playerActivingGirl");
		enemyActivingGirl = gameData.get("enemyActivingGirl");
		playerHandSize = gameData.get("playerHandSize");
		enemyHandSize = gameData.get("enemyHandSize");
		playerExtraDraws = gameData.get("playerExtraDraws");
		enemyExtraDraws = gameData.get("enemyExtraDraws");
		playerDeckResetTimes = gameData.get("playerDeckResetTimes");
		enemyDeckResetTimes = gameData.get("enemyDeckResetTimes");
		attackP = gameData.get("attackP");
		shieldP = gameData.get("shieldP");
		focusP = gameData.get("focusP");
		attackE = gameData.get("attackE");
		shieldE = gameData.get("shieldE");
		focusE = gameData.get("focusE");
	}

	private void getNowGameData() {
		gameData.put("turns", turns);
		gameData.put("phase", phase);
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
		gameData.put("attackP", attackP);
		gameData.put("shieldP", shieldP);
		gameData.put("focusP", focusP);
		gameData.put("attackE", attackE);
		gameData.put("shieldE", shieldE);
		gameData.put("focusE", focusE);
	}
}
