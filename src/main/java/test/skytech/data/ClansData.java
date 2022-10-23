package test.skytech.data;

import test.skytech.entitys.Clan;
import test.skytech.services.ClanService;
import java.util.concurrent.ConcurrentSkipListMap;

public class ClansData implements ClanService {

    private volatile ConcurrentSkipListMap<Long, Clan> clans;


    public ClansData(){
        clans = new ConcurrentSkipListMap<>();
    }
    public Clan getClanById(long clanId) {
        return clans.get(clanId);
    }

    @Override
    public synchronized void addClan(long clanId, Clan clan) {
        clans.put(clanId, clan);
    }
}
