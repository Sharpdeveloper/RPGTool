package RPGTool;

public class StockListItem{
    private String purchase;
    private int amountPurchase;
    private int pricePurchase;
    private String name;
    private int priceSale;
    private int amountSale;
    private String sale;
    
    public String getPurchase() {return purchase;};
    public int getAmountPurchase() {return amountPurchase;};
    public int getPricePurchase() {return pricePurchase;};
    public String getName() {return name;};
    public int getPriceSale() {return priceSale;};
    public int getAmountSale() {return amountSale;};
    public String getSale() {return sale;};
    
    public void setPurchase(String _purchase) {purchase = _purchase;}
    public void setAmountPurchase(int _amountPurchase) {amountPurchase = _amountPurchase;}
    public void setPricePurchase(int _pricePurchase) {pricePurchase = _pricePurchase;}
    public void setName(String _name) {name = _name;}
    public void setPriceSale(int _priceSale) {priceSale = _priceSale;}
    public void setAmountSale(int _amountSale) {amountSale = _amountSale;}
    public void setSale(String _sale) {sale = _sale;}

    public StockListItem(String _name)
    {
        name = _name;
        purchase = "Buy";
        amountPurchase = 0;
        pricePurchase = -1;
        priceSale = -1;
        amountSale = 0;
        sale = "Sell";
    }
}
