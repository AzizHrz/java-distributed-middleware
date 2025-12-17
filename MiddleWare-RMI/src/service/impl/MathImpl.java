package service.impl;

import service.IMath;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class MathImpl extends UnicastRemoteObject implements IMath {
    //constructor
    public MathImpl() throws RemoteException {}

    @Override
    public int[] decomposition(int n) throws RemoteException {
        ArrayList<Integer> list = new ArrayList<>();
        int d = 2;
        while(n > 1) {
            while(n % d == 0) {
                list.add(d);
                n /= d;
            }
            d++;
        }
        return list.stream().mapToInt(i -> i).toArray();
    }

    @Override
    public int pgcd(int a, int b) throws RemoteException {
        while(b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    @Override
    public int ppcm(int a, int b) throws RemoteException {
        return (a * b) / pgcd(a, b);
    }
}
