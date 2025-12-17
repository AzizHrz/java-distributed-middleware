package service;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMath extends Remote{
    int[] decomposition(int n) throws RemoteException;
    int pgcd(int a, int b) throws RemoteException;
    int ppcm(int a, int b) throws RemoteException;
}
