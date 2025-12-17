package service.impl;

import service.IStringTools;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class StringToolsImpl extends UnicastRemoteObject implements IStringTools {

    public StringToolsImpl() throws RemoteException {}

    @Override
    public int occurrence(String text, char c) throws RemoteException {
        int count = 0;
        for(char ch : text.toCharArray()) {
            if(ch == c) count++;
        }
        return count;
    }

    @Override
    public boolean isPalindrome(String text) throws RemoteException {
        String clean = text.replaceAll("\\s+", "").toLowerCase();
        return clean.equals(new StringBuilder(clean).reverse().toString());
    }
}
