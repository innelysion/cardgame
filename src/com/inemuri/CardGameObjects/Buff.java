package com.inemuri.CardGameObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.inemuri.CardGameObjects.Enum.Party;
import com.inemuri.CardGameObjects.Enum.Zone;

public class Buff {
	// 固有信息
	private int id; // 序号
	private String name; // 名称
	private String nameIcon; // 图标文件名
	private String description; // 说明文

	// 状态
	private int priority; // 优先级
	private boolean isActive; // 是否激活
	private Party party; // 阵营
	private HashMap<String, Integer> gameData; // 对局数据容器

	// 某些效果需要的临时容器
	private ArrayList<Integer> mainValue;
	private ArrayList<Boolean> mainSwitch;

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

	Buff(int i, Party p) {
		id = i;
		isActive = true;
		party = p;
		playerTeam = new ArrayList<Girl>();
		enemyTeam = new ArrayList<Girl>();
		playerDeck = new ArrayList<Card>();
		enemyDeck = new ArrayList<Card>();
		allGirls = new ArrayList<Girl>();
		allCards = new ArrayList<Card>();

		mainValue = new ArrayList<Integer>();
		mainSwitch = new ArrayList<Boolean>();
	}

	// 主处理
	public void run() {
		switch (id) {
		case 1: // B乐园绝妙的赛钱箱("回合结束时敌方队伍HP总值因为任何原因比现在少的话,下回合抽牌阶段我方额外抽三张卡")
			// 事件卡只在出牌堆叠生效
			if (getMasterCard().anyMatch(c -> c.isIn(Zone.PLAYSTACK)) && !mainSwitch.contains(true)) {
				// 取得对方队伍的HP总量，打开开关
				mainValue.add(enemyTeam.stream().mapToInt(g -> g.getHp()).sum());
				mainValue.add(turns);
				mainSwitch.add(true);
			}
			// 回合结束后仍未触发的话效果结束
			if (mainSwitch.contains(true) && turns > mainValue.get(1)) {
				reset();
			} else if (mainSwitch.contains(true)
					&& enemyTeam.stream().mapToInt(g -> g.getHp()).sum() < mainValue.get(0)) {
				playerExtraDraws += 3;
				reset();
				System.out.println("●●●强行塞钱征收!●●●我方下回合额外抽3张卡");
			}

			break;
		case 2: // B魔力反馈("被敌方取得先手的情况下,己方本次攻击获得10*X的伤害加值,X等同于敌方本回合使用过的【魔】总值的一半")
			// 魔法卡只在弃牌区生效&被敌方先手
			if (getMasterCard().anyMatch(c -> c.isIn(Zone.GRAVEYARD)) && focusP < focusE) {
				int result = enemyDeck.stream()
						// 获得敌方本回合使用过的（包含技能消耗堆叠中的）卡牌的列表
						.filter(c -> c.isIn(Zone.PLAYSTACK) || c.isIn(Zone.COSTSTACK))
						// 展开卡牌列表，展开属性列表，获得所有【魔】属性
						.flatMap(clist -> clist.getElements().stream()).filter(e -> e.getName() == "魔").mapToInt(e -> {
							// 计算这张卡【魔】的总值
							return //
							e.getValueBase() > 0 ? (e.getValueBase() + e.getValueAddMod()) * e.getValueMultiMod() : 0;
							// 所有卡牌的【魔】总值求和
						}).sum();
				if (result > 0) {
					// 取总值的一半
					result = (int) (((double) result / 2) * 10);
					attackP += result;
					System.out.println("●●●魔力反馈生效●●●【伤害加值" + result + "】");
				}
			}
			break;
		case 3: // B蛇蛙发饰
			break;
		case 4: // 莉格露自爆
			if (getMasterGirl().anyMatch(g -> g.getHp() < 0)) {
				System.out.println("●●●莉格露自爆●●●");
				enemyTeam.forEach(g -> g.hpDamage(300));
				isActive = false;
			}
			break;
		}

	}

	// Get
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

	// 以上Gets
	// Sets
	// 设置激活与否
	public void setActive(boolean b) {
		isActive = b;
	}

	// 拿取对局数据
	public void updateGameData(HashMap<String, Integer> d, ArrayList<Girl> g, ArrayList<Card> c, ArrayList<Buff> b) {
		playerTeam.clear();
		enemyTeam.clear();
		playerDeck.clear();
		enemyDeck.clear();
		gameData = d;
		activeBuffs = b;
		turns = gameData.get("turns");
		phase = gameData.get("turns");
		playerTeamSize = gameData.get(party == Party.ENEMY ? "enemyTeamSize" : "playerTeamSize");
		enemyTeamSize = gameData.get(party == Party.ENEMY ? "playerTeamSize" : "enemyTeamSize");
		playerActivingGirl = gameData.get(party == Party.ENEMY ? "enemyActivingGirl" : "playerActivingGirl");
		enemyActivingGirl = gameData.get(party == Party.ENEMY ? "playerActivingGirl" : "enemyActivingGirl");
		playerHandSize = gameData.get(party == Party.ENEMY ? "enemyHandSize" : "playerHandSize");
		enemyHandSize = gameData.get(party == Party.ENEMY ? "playerHandSize" : "enemyHandSize");
		playerExtraDraws = gameData.get(party == Party.ENEMY ? "enemyExtraDraws" : "playerExtraDraws");
		enemyExtraDraws = gameData.get(party == Party.ENEMY ? "playerExtraDraws" : "enemyExtraDraws");
		playerDeckResetTimes = gameData.get(party == Party.ENEMY ? "enemyDeckResetTimes" : "playerDeckResetTimes");
		enemyDeckResetTimes = gameData.get(party == Party.ENEMY ? "playerDeckResetTimes" : "enemyDeckResetTimes");
		attackP = gameData.get(party == Party.ENEMY ? "attackE" : "attackP");
		shieldP = gameData.get(party == Party.ENEMY ? "shieldE" : "shieldP");
		focusP = gameData.get(party == Party.ENEMY ? "focusE" : "focusP");
		attackE = gameData.get(party == Party.ENEMY ? "attackP" : "attackE");
		shieldE = gameData.get(party == Party.ENEMY ? "shieldP" : "shieldE");
		focusE = gameData.get(party == Party.ENEMY ? "focusP" : "focusE");
		allGirls = g;
		allCards = c;
		playerTeam.addAll(g.stream().filter(girl -> girl.belongs(party == Party.ENEMY ? Party.ENEMY : Party.ALLY))
				.collect(Collectors.toList()));
		enemyTeam.addAll(g.stream().filter(girl -> girl.belongs(party == Party.ENEMY ? Party.ALLY : Party.ENEMY))
				.collect(Collectors.toList()));
		playerDeck.addAll(c.stream().filter(card -> card.belongs(party == Party.ENEMY ? Party.ENEMY : Party.ALLY))
				.collect(Collectors.toList()));
		enemyDeck.addAll(c.stream().filter(card -> card.belongs(party == Party.ENEMY ? Party.ALLY : Party.ENEMY))
				.collect(Collectors.toList()));
	}

	// 回传部分int对局数据
	public HashMap<String, Integer> returnGameData() {
		gameData.put(party == Party.ENEMY ? "enemyTeamSize" : "playerTeamSize", playerTeamSize);
		gameData.put(party == Party.ENEMY ? "playerTeamSize" : "enemyTeamSize", enemyTeamSize);
		gameData.put(party == Party.ENEMY ? "enemyActivingGirl" : "playerActivingGirl", playerActivingGirl);
		gameData.put(party == Party.ENEMY ? "playerActivingGirl" : "enemyActivingGirl", enemyActivingGirl);
		gameData.put(party == Party.ENEMY ? "enemyHandSize" : "playerHandSize", playerHandSize);
		gameData.put(party == Party.ENEMY ? "playerHandSize" : "enemyHandSize", enemyHandSize);
		gameData.put(party == Party.ENEMY ? "enemyExtraDraws" : "playerExtraDraws", playerExtraDraws);
		gameData.put(party == Party.ENEMY ? "playerExtraDraws" : "enemyExtraDraws", enemyExtraDraws);
		gameData.put(party == Party.ENEMY ? "enemyDeckResetTimes" : "playerDeckResetTimes", playerDeckResetTimes);
		gameData.put(party == Party.ENEMY ? "playerDeckResetTimes" : "enemyDeckResetTimes", enemyDeckResetTimes);
		gameData.put(party == Party.ENEMY ? "attackE" : "attackP", attackP);
		gameData.put(party == Party.ENEMY ? "shieldE" : "shieldP", shieldP);
		gameData.put(party == Party.ENEMY ? "focusE" : "focusP", focusP);
		gameData.put(party == Party.ENEMY ? "attackP" : "attackE", attackE);
		gameData.put(party == Party.ENEMY ? "shieldP" : "shieldE", shieldE);
		gameData.put(party == Party.ENEMY ? "focusP" : "focusE", focusE);
		return gameData;
	}

	// 以上Sets

	// 返回buff附着的卡牌stream
	private Stream<Card> getMasterCard() {
		return allCards.stream().filter(c -> c.getBuffs().contains(this));
	}

	// 返回buff附着的角色stream
	private Stream<Girl> getMasterGirl() {
		return allGirls.stream().filter(c -> c.getBuffs().contains(this));
	}

	// 恢复初始状态
	private void reset() {
		mainValue.clear();
		mainSwitch.clear();
	}

}

// BUFF数据库
class BuffsPool {
	static String[] name = { "DEFAULT_BUFF_NAME", "B乐园绝妙的赛钱箱", "B魔力吸收", "B蛇蛙发饰", "" };
	static int[] priority = { 0, 1, -1, 0, 0 };
}