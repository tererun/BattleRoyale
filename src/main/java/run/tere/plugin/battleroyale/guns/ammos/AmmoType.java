package run.tere.plugin.battleroyale.guns.ammos;

public enum AmmoType {
    IRON_AMMO("§7アイアンアモ", 1, 64), //ライト
    GOLD_AMMO("§6ゴールドアモ", 2, 32), //ヘビー
    DIAMOND_AMMO("§bダイヤモンドアモ", 3, 16), //スナイパー
    NETHER_BRICK_AMMO("§4ネザーレンガアモ", 4, 16); //ショットガン

    private String name;
    private int customModelData;
    private int defaultGiveAmount;

    AmmoType(String name, int customModelData, int defaultGiveAmount) {
        this.name = name;
        this.customModelData = customModelData;
        this.defaultGiveAmount = defaultGiveAmount;
    }

    public String getName() {
        return name;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public int getDefaultGiveAmount() {
        return defaultGiveAmount;
    }
}
