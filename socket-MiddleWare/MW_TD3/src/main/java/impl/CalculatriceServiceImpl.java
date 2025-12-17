package impl;

import service.CalculatriceInterface;

public class CalculatriceServiceImpl implements CalculatriceInterface {

    @Override
    public double addition(double a, double b) {
        return a + b;
    }

    @Override
    public double multiplication(double a, double b) {
        return a * b;
    }

    @Override
    public double soustraction(double a, double b) {
        return a - b;
    }

    @Override
    public double division(double a, double b) {
        if (b == 0) return Double.NaN;
        return a / b;
    }
}
