package inemuri;

import java.util.ArrayList;

// 全局变量
public class GameSetting {


}

// 类型
enum Types {
	// 默认
	DEFAULT,
	// 使用属性表的基础行动(攻击, 防御, 集中)
	ACTION,
	// (效果)装备, 结界, 法术
	EQUIPMENT, SEAL, MAGIC,
	// (事件)事件
	EVENT,
	// (非玩家可用)触发器
	TRIGGER,
	// (其他)
	OTHER,
}
