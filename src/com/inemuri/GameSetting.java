package inemuri;

import java.util.function.Predicate;

import inemuri.CardGameObjects.Card;
import inemuri.CardGameObjects.Enum.Type;
import inemuri.CardGameObjects.Enum.Zone;

// 全局变量
public class GameSetting {

	static int mainElementMultiply = 2;
	static Predicate<Card> legalCheck = c -> //
	(c.isIn(Zone.PLAYSTACK) && (c.isType(Type.ATTACK) || c.isType(Type.DEFENCE) || c.isType(Type.FOCUS))) || //
			(c.isIn(Zone.LIBRARY) && c.isType(Type.SEAL)) || //
			(c.isIn(Zone.GRAVEYARD) && c.isType(Type.MAGIC)) || //
			(c.isIn(Zone.HAND) && c.isType(Type.EQUIPMENT));//

	static String[] ATKElements = { "灵", "魔", "妖", "神", "幻", "源", "心", "无" };
}


