package com.veylor.smp;

import com.veylor.smp.claim.ClaimCommand;
import com.veylor.smp.claim.ClaimListener;
import com.veylor.smp.claim.ClaimManager;
import com.veylor.smp.claim.UnclaimCommand;
import com.veylor.smp.commands.BalanceCommand;
import com.veylor.smp.commands.BountyCommand;
import com.veylor.smp.commands.HomeCommand;
import com.veylor.smp.commands.MinigameCommand;
import com.veylor.smp.commands.PayCommand;
import com.veylor.smp.commands.SetHomeCommand;
import com.veylor.smp.commands.SetSpawnCommand;
import com.veylor.smp.commands.SetWarpCommand;
import com.veylor.smp.commands.SmpCommand;
import com.veylor.smp.commands.SpawnCommand;
import com.veylor.smp.commands.TpaCommand;
import com.veylor.smp.commands.TpAcceptCommand;
import com.veylor.smp.commands.TpDenyCommand;
import com.veylor.smp.commands.WarpCommand;
import com.veylor.smp.commands.DelHomeCommand;
import com.veylor.smp.commands.DelWarpCommand;
import com.veylor.smp.commands.WildCommand;
import com.veylor.smp.data.DataStore;
import com.veylor.smp.economy.BountyManager;
import com.veylor.smp.economy.EconomyManager;
import com.veylor.smp.listeners.BountyListener;
import com.veylor.smp.listeners.CombatLogListener;
import com.veylor.smp.listeners.OnePlayerSleepListener;
import com.veylor.smp.listeners.PlayerConnectionListener;
import com.veylor.smp.minigame.MinigameManager;
import com.veylor.smp.teleport.TeleportRequestManager;
import com.veylor.smp.warp.HomeManager;
import com.veylor.smp.warp.SpawnManager;
import com.veylor.smp.warp.WarpManager;
import org.bukkit.plugin.java.JavaPlugin;

public class VeylorSMPPlugin extends JavaPlugin {
    private DataStore dataStore;
    private EconomyManager economyManager;
    private SpawnManager spawnManager;
    private HomeManager homeManager;
    private WarpManager warpManager;
    private TeleportRequestManager teleportRequestManager;
    private MinigameManager minigameManager;
    private BountyManager bountyManager;
    private ClaimManager claimManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        dataStore = new DataStore(this);
        economyManager = new EconomyManager(this, dataStore);
        bountyManager = new BountyManager(dataStore);
        spawnManager = new SpawnManager(this);
        homeManager = new HomeManager(dataStore);
        warpManager = new WarpManager(dataStore);
        teleportRequestManager = new TeleportRequestManager();
        minigameManager = new MinigameManager(this, economyManager);
        claimManager = new ClaimManager(dataStore);

        getCommand("smp").setExecutor(new SmpCommand());
        getCommand("spawn").setExecutor(new SpawnCommand(spawnManager));
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this, spawnManager));
        getCommand("home").setExecutor(new HomeCommand(homeManager));
        getCommand("sethome").setExecutor(new SetHomeCommand(homeManager));
        getCommand("delhome").setExecutor(new DelHomeCommand(homeManager));
        getCommand("warp").setExecutor(new WarpCommand(warpManager));
        getCommand("setwarp").setExecutor(new SetWarpCommand(warpManager));
        getCommand("delwarp").setExecutor(new DelWarpCommand(warpManager));
        getCommand("tpa").setExecutor(new TpaCommand(teleportRequestManager));
        getCommand("tpaccept").setExecutor(new TpAcceptCommand(teleportRequestManager));
        getCommand("tpdeny").setExecutor(new TpDenyCommand(teleportRequestManager));
        getCommand("wild").setExecutor(new WildCommand(this));
        getCommand("balance").setExecutor(new BalanceCommand(economyManager));
        getCommand("pay").setExecutor(new PayCommand(economyManager));
        getCommand("minigame").setExecutor(new MinigameCommand(minigameManager));
        getCommand("bounty").setExecutor(new BountyCommand(bountyManager, economyManager));
        getCommand("claim").setExecutor(new ClaimCommand(claimManager));
        getCommand("unclaim").setExecutor(new UnclaimCommand(claimManager));

        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(economyManager, spawnManager), this);
        getServer().getPluginManager().registerEvents(minigameManager, this);
        getServer().getPluginManager().registerEvents(new BountyListener(bountyManager, economyManager), this);
        getServer().getPluginManager().registerEvents(new CombatLogListener(this), this);
        getServer().getPluginManager().registerEvents(new OnePlayerSleepListener(this), this);
        getServer().getPluginManager().registerEvents(new ClaimListener(claimManager), this);
    }

    @Override
    public void onDisable() {
        dataStore.save();
    }
}
