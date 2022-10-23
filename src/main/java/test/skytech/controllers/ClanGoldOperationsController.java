package test.skytech.controllers;

import test.skytech.Main;
import test.skytech.data.ClansData;
import test.skytech.data.UsersData;
import test.skytech.entitys.Clan;
import test.skytech.entitys.User;
import test.skytech.services.GoldTransactionService;

public class ClanGoldOperationsController implements GoldTransactionService {

    private final ClansData clans;
    private final UsersData users;

    public ClanGoldOperationsController(ClansData clans, UsersData users) {
        this.clans = clans;
        this.users = users;
    }

    public void addGoldToClan(long userId, long clanId, int gold) {
        Clan clan = clans.getClanById(clanId);
        User user = users.getUserById(userId);

        if(user.checkGoldAmount(gold)){
            user.takeGold(gold);
            clan.addGold(gold);
            sendTransactionMessage(userId, gold, clanId);
        }else{
            sendTransactionMessage("User" + userId + " don't have " + gold + " gold to add it in clan " + clanId);
        }
    }

    @Override
    public String sendTransactionMessage(long from, int amount, long to) {
        String message = new java.util.Date() + ": User - " + from + " added " + amount + " gold to clan: " + to +". ";
        Main.sendMessage(message);
        return message;
    }

    @Override
    public String sendTransactionMessage(long from, int amount, long to, String memo) {
        return null;
    }

    @Override
    public String sendTransactionMessage(long from, long to) {
        return null;
    }

    @Override
    public String sendTransactionMessage(long from, long to, String memo) {
        return null;
    }

    @Override
    public String sendTransactionMessage(long from) {
        return null;
    }

    @Override
    public String sendTransactionMessage(String message) {
        message = new java.util.Date() + ": "+ message;
        Main.sendMessage(message);
        return message;
    }


}
