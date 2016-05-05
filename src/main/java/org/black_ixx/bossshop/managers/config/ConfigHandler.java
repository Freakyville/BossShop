package org.black_ixx.bossshop.managers.config;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.features.PointsManager.PointsPlugin;
import org.black_ixx.bossshop.points.PointsAPI;
import org.bukkit.Bukkit;

public class ConfigHandler {

	// /////////////////////////////////////// <- Init class

	public ConfigHandler(BossShop plugin) {
		this.plugin = plugin;

		//addDefaults(); //Not needed anymore because default config is copied from resources folder. NOTE: Needing new code in case of adding new config lines

		if (plugin.getConfig().getBoolean("signs.enabled") || plugin.getConfig().getBoolean("EnableSigns")) {
			ClassManager.manager.getSettings().setSignsEnabled(true);
		}

		String main = plugin.getConfig().getString("MainShop");
		ClassManager.manager.getSettings().setMainShop(main.toLowerCase());
		ClassManager.manager.getSettings().setInventoryCustomizingHideEnabled(plugin.getConfig().getBoolean("HideItemsPlayersDoNotHavePermissionsFor"));
		ClassManager.manager.getSettings().setTransactionLogEnabled(plugin.getConfig().getBoolean("EnableTransactionLog"));
		ClassManager.manager.getSettings().setServerPingingSpeed((plugin.getConfig().getInt("ServerPingingDelay")));
		ClassManager.manager.getSettings().setMetricsEnabled((!plugin.getConfig().getBoolean("DisableMetrics")));
		ClassManager.manager.getSettings().setUpdaterEnabled((!plugin.getConfig().getBoolean("DisableUpdateNotifications")));
		ClassManager.manager.getSettings().setUnsafeEnchantmentsEnabled((!plugin.getConfig().getBoolean("AllowUnsafeEnchantments")));
		ClassManager.manager.getSettings().setPointsPlugin(findPointsPlugin(plugin.getConfig().getString("PointsPlugin")));
		ClassManager.manager.getSettings().setAutoDownloadUpdateEnabled((!plugin.getConfig().getBoolean("DisableUpdateAutoDownload")));
		ClassManager.manager.getSettings().setLoadSubfoldersEnabled(plugin.getConfig().getBoolean("SearchSubfoldersForShops"));
		ClassManager.manager.getSettings().setCanPlayersSellItemsWithGreaterEnchants(plugin.getConfig().getBoolean("CanPlayersSellItemsWithGreaterEnchants"));
	}

	private PointsPlugin findPointsPlugin(String config_points_plugin){
		if (config_points_plugin != null) {
			for(PointsPlugin pp : PointsPlugin.values()){
				for(String nick : pp.getNicknames()){
					if(nick.equalsIgnoreCase(config_points_plugin)){
						return pp;
					}
				}
			}
		}
		//Either had no plugin entered or was not successful at searching
		for(PointsPlugin pp : PointsPlugin.values()){
			String plugin_name = pp.getPluginName();
			if(plugin_name!=null){
				if(Bukkit.getPluginManager().getPlugin(plugin_name) != null){
					return pp;
				}
			}

		}
		//Having custom points plugin
		if (PointsAPI.get(config_points_plugin) != null) {
			PointsPlugin.CUSTOM.setCustom(config_points_plugin);
			return PointsPlugin.CUSTOM;
		}

		return null;
	}

	// /////////////////////////////////////// <- Variables

	private BossShop plugin;

	// /////////////////////////////////////// <- Add Defaults

	public void addDefaults() {
		plugin.reloadConfig();

		plugin.getConfig().addDefault("EnableSigns", true);
		plugin.getConfig().addDefault("MainShop", "Menu");
		plugin.getConfig().addDefault("HideItemsPlayersDoNotHavePermissionsFor", false);
		plugin.getConfig().addDefault("CanPlayersSellItemsWithGreaterEnchants", false);
		plugin.getConfig().addDefault("EnableTransactionLog", false);
		plugin.getConfig().addDefault("SearchSubfoldersForShops", false);
		plugin.getConfig().addDefault("ServerPingingDelay", 20);
		plugin.getConfig().addDefault("AllowUnsafeEnchantments", false);
		plugin.getConfig().addDefault("DisableUpdateNotifications", false);
		plugin.getConfig().addDefault("DisableUpdateAutoDownload", false);
		plugin.getConfig().addDefault("MultiplierGroups.Enabled", false);
		plugin.getConfig().addDefault("MultiplierGroups.List", new String[] { "Permission.Node:<type>:<multiplier>", "BossShop.PriceMultiplier.Points1:points:0.75", "BossShop.PriceMultiplier.Points1:points:0.5", "BossShop.PriceMultiplier.Money1:money:0.75", "BossShop.PriceMultiplier.Money2:money:0.5", "BossShop.PriceMultiplier.MoneyNegative:money:2.0", "BossShop.PriceMultiplier.Exp:exp:0.8"});
		plugin.getConfig().addDefault("PointsPlugin", "auto-detect");
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveConfig();

	}


}
