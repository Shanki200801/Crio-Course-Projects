package com.crio.warmup.stock;

public class Trade {
    private String symbol, tradeType, purchaseDate;
    private int quantity;

    public String getSymbol() {
        return symbol;
    }
    public String getPurchaseDate() {
        return purchaseDate;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getTradeType() {
        return tradeType;
    }
    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public Trade(){

    }

    public Trade(String symbol, String tradeType, String purchaseDate, int quantity){
        this.symbol = symbol;
        this.purchaseDate = purchaseDate;
        this.tradeType = tradeType;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return this.symbol + " " + this.purchaseDate; 
    }
}
