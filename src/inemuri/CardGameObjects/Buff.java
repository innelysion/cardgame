package inemuri.CardGameObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import inemuri.CardGameObjects.Enum.Party;
import inemuri.CardGameObjects.Enum.Zone;

public class Buff {
	private int id; // 序号
	private String name; // 名称
	private int priority; // 优先级
	private boolean isActive; // 是否激活
	private boolean isEnemy; // 是否敌对buff(敌对buff将设置"敌军"为我军)
	private HashMap<String, Integer> gameData; // 对局数据容器

	// 传入的对局数据
	private int turns; // 当前回合
	private int phase; // 当前阶段
	private int playerTeamSize, enemyTeamSize; // 队伍人数
	private int playerActivingGirl, enemyActivingGirl; // 正在行动的角色索引
	private int playerHandSize, enemyHandSize; // 手牌上限
	private int playerExtraDraws, enemyExtraDraws; // 额外抽卡次数
	private int playerDeckResetTimes, enemyDeckResetTimes; // 牌库抓空后重置次数
	private int attackP, shieldP, focusP, attackE, shieldE, focusE; // 回合内伤害值,护盾加值,集中值
	private ArrayList<Girl> playerTeam, enemyTeam; // 队伍
	private ArrayList<Card> playerDeck, enemyDeck; // 卡组
	private ArrayList<Girl> allGirls; // 全体角色
	private ArrayList<Card> allCards; // 全体卡组

	// 非对局类成员的临时数据
	private ArrayList<Buff> activeBuffs; // 当前生效的BUFF

	Buff(int i) {
		id = i;
		isActive = true;
		isEnemy = false;
		playerTeam = new ArrayList<Girl>();
		enemyTeam = new ArrayList<Girl>();
		playerDeck = new ArrayList<Card>();
		enemyDeck = new ArrayList<Card>();
		allGirls = new ArrayList<Girl>();
		allCards = new ArrayList<Card>();
	}

	// 主处理
	public void run() {
		switch (id) {
		case 1: // B塞钱征收("如成功对敌方队伍造成HP伤害,下回合抽牌阶段额外抽三张卡")
			// 如果附有此buff的卡在堆叠中
			if (playerDeck.stream().filter(c -> c.getBuffs().contains(this)).anyMatch(c -> c.isIn(Zone.PLAYSTACK))) {
				// 本回合伤害大于(敌方堆叠产生的护盾值+敌方行动角色已有护盾)的和
				if (attackP > shieldE + enemyTeam.stream().filter(g -> g.getPosition() == enemyActivingGirl).findFirst()
						.get().getShield()) {
					playerExtraDraws += 3;
					System.out.println("●●●塞钱征收生效●●●下回合抽牌阶段额外抽三张卡");
				}
			}
			break;
		case 2: // B魔力吸收("敌方取得先手且敌方本回合使用过【魔】的话,【魔】增强3")
			if (focusP < focusE && playerDeck.stream().filter(c -> c.getBuffs().contains(this))
					.anyMatch(c -> c.isIn(Zone.GRAVEYARD))) {
				if (enemyDeck.stream().filter(c -> c.isIn(Zone.PLAYSTACK) || c.isIn(Zone.COSTSTACK))
						.anyMatch(c -> c.getElements().stream().anyMatch(e -> e.getName() == "魔"))) {
					attackP += 100;
					System.out.println("●●●魔力吸收生效●●●【魔】增强3");
				}
			}
			break;
		case 3: // B蛇蛙发饰
			break;
		case 4: // Bリグルpassive
			if (playerTeam.stream().filter(c -> c.getBuffs().contains(this)).anyMatch(g -> g.getHp() < 0)) {
				System.out.println("●●●リグル自爆●●●");
				enemyTeam.forEach(g -> g.hpDamage(300));
				isActive = false;
			}
			break;
		}
	}

	// 获取序号
	public int getId() {
		return id;
	}

	// 获取名称
	public String getName() {
		return name;
	}

	// 获取优先级
	public int getPriority() {
		return priority;
	}

	// 是否激活
	public boolean isActive() {
		return isActive;
	}

	// 设置激活与否
	public void setActive(boolean b) {
		isActive = b;
	}

	// 设置是否敌对buff
	public void setEnemy(boolean b) {
		isEnemy = b;
	}

	// 更新对局数据，区分敌我
	public void updateGameData(HashMap<String, Integer> d, ArrayList<Girl> g, ArrayList<Card> c, ArrayList<Buff> b) {
		playerTeam.clear();
		enemyTeam.clear();
		playerDeck.clear();
		enemyDeck.clear();
		gameData = d;
		activeBuffs = b;
		turns = gameData.get("turns");
		phase = gameData.get("turns");
		playerTeamSize = gameData.get(isEnemy ? "enemyTeamSize" : "playerTeamSize");
		enemyTeamSize = gameData.get(isEnemy ? "playerTeamSize" : "enemyTeamSize");
		playerActivingGirl = gameData.get(isEnemy ? "enemyActivingGirl" : "playerActivingGirl");
		enemyActivingGirl = gameData.get(isEnemy ? "playerActivingGirl" : "enemyActivingGirl");
		playerHandSize = gameData.get(isEnemy ? "enemyHandSize" : "playerHandSize");
		enemyHandSize = gameData.get(isEnemy ? "playerHandSize" : "enemyHandSize");
		playerExtraDraws = gameData.get(isEnemy ? "enemyExtraDraws" : "playerExtraDraws");
		enemyExtraDraws = gameData.get(isEnemy ? "playerExtraDraws" : "enemyExtraDraws");
		playerDeckResetTimes = gameData.get(isEnemy ? "enemyDeckResetTimes" : "playerDeckResetTimes");
		enemyDeckResetTimes = gameData.get(isEnemy ? "playerDeckResetTimes" : "enemyDeckResetTimes");
		attackP = gameData.get(isEnemy ? attackE : "attackP");
		shieldP = gameData.get(isEnemy ? shieldE : "shieldP");
		focusP = gameData.get(isEnemy ? focusE : "focusP");
		attackE = gameData.get(isEnemy ? attackP : "attackE");
		shieldE = gameData.get(isEnemy ? shieldP : "shieldE");
		focusE = gameData.get(isEnemy ? focusP : "focusE");
		allGirls = g;
		allCards = c;
		playerTeam.addAll(g.stream().filter(girl -> girl.belongs(isEnemy ? Party.ENEMY : Party.ALLY))
				.collect(Collectors.toList()));
		enemyTeam.addAll(g.stream().filter(girl -> girl.belongs(isEnemy ? Party.ALLY : Party.ENEMY))
				.collect(Collectors.toList()));
		playerDeck.addAll(c.stream().filter(card -> card.belongs(isEnemy ? Party.ENEMY : Party.ALLY))
				.collect(Collectors.toList()));
		enemyDeck.addAll(c.stream().filter(card -> card.belongs(isEnemy ? Party.ALLY : Party.ENEMY))
				.collect(Collectors.toList()));
	}

	// 更新和回传buff生效后的int类对局数据(角色/卡牌/BUFF是引用直接修改不用回传)
	public HashMap<String, Integer> returnGameData() {
		gameData.put("turns", turns);
		gameData.put(isEnemy ? "enemyTeamSize" : "playerTeamSize", playerTeamSize);
		gameData.put(isEnemy ? "playerTeamSize" : "enemyTeamSize", enemyTeamSize);
		gameData.put(isEnemy ? "enemyActivingGirl" : "playerActivingGirl", playerActivingGirl);
		gameData.put(isEnemy ? "playerActivingGirl" : "enemyActivingGirl", enemyActivingGirl);
		gameData.put(isEnemy ? "enemyHandSize" : "playerHandSize", playerHandSize);
		gameData.put(isEnemy ? "playerHandSize" : "enemyHandSize", enemyHandSize);
		gameData.put(isEnemy ? "enemyExtraDraws" : "playerExtraDraws", playerExtraDraws);
		gameData.put(isEnemy ? "playerExtraDraws" : "enemyExtraDraws", enemyExtraDraws);
		gameData.put(isEnemy ? "enemyDeckResetTimes" : "playerDeckResetTimes", playerDeckResetTimes);
		gameData.put(isEnemy ? "playerDeckResetTimes" : "enemyDeckResetTimes", enemyDeckResetTimes);
		gameData.put(isEnemy ? "attackE" : "attackP", attackP);
		gameData.put(isEnemy ? "shieldE" : "shieldP", shieldP);
		gameData.put(isEnemy ? "focusE" : "focusP", focusP);
		gameData.put(isEnemy ? "attackP" : "attackE", attackE);
		gameData.put(isEnemy ? "shieldP" : "shieldE", shieldE);
		gameData.put(isEnemy ? "focusP" : "focusE", focusE);
		return gameData;
	}
}

class BuffsPool {
	static String[] name = {};
	static int[] priority = {};
}