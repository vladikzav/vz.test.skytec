package test.skytech.services;

public interface GoldTransactionService {

    String sendTransactionMessage(long from, int amount, long to);

    String sendTransactionMessage(long from, int amount, long to, String memo);

    String sendTransactionMessage(long from, long to);

    String sendTransactionMessage(long from, long to, String memo);

    String sendTransactionMessage(long from);

    String sendTransactionMessage(String message);

}
