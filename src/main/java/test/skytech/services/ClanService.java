package test.skytech.services;

import test.skytech.entitys.Clan;

public interface ClanService {

    Clan getClanById(long clanId);

    void addClan(long clanId, Clan clan);
}
