package service;
import java.rmi.Remote;
import java.rmi.RemoteException;
public interface IStringTools extends Remote{
    int occurrence(String text, char c) throws RemoteException;
    boolean isPalindrome(String text) throws RemoteException;
}
