package inemuri.CardGameObjects.Enum;

public enum Zone {
	// 默认
	DEFAULT,
	// 牌库, 手牌, 弃牌, 放逐
	LIBRARY, HAND, GRAVEYARD, EXILE,
	// 抽牌堆叠, 打牌堆叠, 技能消耗堆叠
	DRAWSTACK, PLAYSTACK, COSTSTACK;
}

