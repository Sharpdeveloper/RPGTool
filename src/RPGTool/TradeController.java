package RPGTool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.nio.file.*;
import java.util.Collections;
import java.util.Random;

public class TradeController {
    private String path;
    private String playerPath;
    private String citiesPath;
    private String goodsPath;
    private ArrayList<City> cities;
    private ArrayList<Good> goods ;
    private ArrayList<Player> loadedPlayer;
    private int selectedCityIndex;
    private int selectedPlayerIndex;
    private int amount;
    private ArrayList<StockListItem> items;
    private boolean firstStart;

    public ArrayList<City> getCities() { return cities;}
    public ArrayList<Good> getGoods () { return goods;}
    public ArrayList<Player> getLoadedPlayer() { return loadedPlayer;}
    public int getSelectedCityIndex() { return selectedCityIndex;}
    public int getSelectedPlayer() { return selectedPlayerIndex;}
    public int getAmount() { return amount;}
    public ArrayList<StockListItem> getItems() { return items;}
    public String getPath() {return path;}
    public String getPlayerPath() {return playerPath;}
    public String getCitiePath() {return citiesPath;}
    public String getGoodsPath() {return goodsPath;}
    public boolean getFirstStart() {return firstStart;}

    public void setCities(ArrayList<City> _cities) { cities =  _cities;}
    public void setGoods (ArrayList<Good> _goods) { goods = _goods;}
    public void setLoadedPlayer(ArrayList<Player> _loadedPlayer) { loadedPlayer = _loadedPlayer;}
    public void setSelectedCityIndex(int _selectedCityIndex) { selectedCityIndex = _selectedCityIndex;}
    public void setSelectedPlayerIndex(int _selectedPlayerIndex) { selectedPlayerIndex = _selectedPlayerIndex;}
    public void setAmount(int _amount) { amount = _amount;}
    public void setItems(ArrayList<StockListItem> _items) { items = _items;}

    public TradeController()
    {
        cities = new ArrayList<>();
        goods = new ArrayList<>();
        loadedPlayer = new ArrayList<>();
        items = new ArrayList<>();
        selectedCityIndex = -1;
        selectedPlayerIndex = -1;
        amount = 1;
        path = Paths.get(System.getProperty("user.home"), "RPGTool").toString();
        playerPath = Paths.get(path, "Player").toString();
        citiesPath = Paths.get(path, "Cities").toString();
        goodsPath = Paths.get(path, "Goods").toString();
        firstStart = false;
        init();
    }

    private void init(){
        if(Files.notExists(Paths.get(path))){
            try {
                Files.createDirectory(Paths.get(path));
            }
            catch (IOException iox){

            }
            firstStart = true;
        }
        if(Files.notExists(Paths.get(playerPath))){
            try {
                Files.createDirectory(Paths.get(playerPath));
            }
            catch (IOException iox) {

            }
            firstStart = true;
        }
        if(Files.notExists(Paths.get(citiesPath))){
            try {
                Files.createDirectory(Paths.get(citiesPath));
            }
            catch (IOException iox){

            }
            firstStart = true;
        }
        if(Files.notExists(Paths.get(goodsPath))){
            try {
                Files.createDirectory(Paths.get(goodsPath));
            }
            catch (IOException iox){

            }
            firstStart = true;
        }
        if (firstStart){
            Good g = new Good();
            g.setAmount(10, true);
            g.setBasicRequirement(2.5);
            g.setValue(20);
            g.setPriceClass(2);
            g.setName("Example-Good Wood");
            saveGood(g);

            Good g1 = new Good();
            g1.setAmount(15, true);
            g1.setBasicRequirement(2.0);
            g1.setValue(35);
            g1.setPriceClass(2);
            g1.setName("Example-Good Stone");
            saveGood(g1);

            Good g2 = new Good();
            g2.setAmount(5, true);
            g2.setBasicRequirement(10.0);
            g2.setValue(1);
            g2.setPriceClass(1);
            g2.setName("Example-Good Water");
            saveGood(g2);

            Player p = new Player();
            p.setMoney(200, true);
            p.getBackPack().add(g);
            p.getBackPack().add(g2);
            p.setName("Example-Player Arthur the Great");
            savePlayer(p);

            g.setAmount(10, true);
            g2.setAmount(50,true);

            City c = new City();
            c.setName("Example-City York");
            c.getNeeds().add(g2.getName());
            c.getProduction().add(g1.getName());
            c.setPopulation(2200);
            c.getStock().add(g);
            c.getStock().add(g1);
            c.getStock().add(g2);
            saveCity(c);
        }
    }

    public void readAllCities()
    {
        File citiesDirectory = new File(citiesPath);
        File[] cityFiles = citiesDirectory.listFiles();
        for(int i = 0; i < cityFiles.length; i++)
        {
            ArrayList<String> rows = new ArrayList<>();
            try (BufferedReader br = Files.newBufferedReader(Paths.get(cityFiles[i].getAbsolutePath())))
            {
                String row = "";
                while ((row = br.readLine()) != null)
                    rows.add(row);
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
            cities.add(City.parseCity(rows, goods));
        }

        Collections.sort(cities);
    }

    public void readAllGoods()
    {
        File goodsDirectory = new File(goodsPath);
        File[] goodFiles = goodsDirectory.listFiles();
        for (int i = 0; i < goodFiles.length; i++)
        {
            ArrayList<String> rows = new ArrayList<>();
            try (BufferedReader br = Files.newBufferedReader(Paths.get(goodFiles[i].getAbsolutePath()), Charset.forName("UTF-8")))
            {
                String row = "";
                while ((row = br.readLine()) != null)
                    rows.add(row);
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
            goods.add(Good.parseGood(rows));
        }

        Collections.sort(goods);

        items = new ArrayList<>();

        for(Good g: goods){
            items.add(new StockListItem(g.getName()));
        }
    }

    public void readAllPlayer()
    {
        File playerDirectory = new File(playerPath);
        File[] playerFiles = playerDirectory.listFiles();
        for (int i = 0; i < playerFiles.length; i++)
        {
            ArrayList<String> rows = new ArrayList<>();
            try (BufferedReader br = Files.newBufferedReader(Paths.get(playerFiles[i].getAbsolutePath()), Charset.forName("UTF-8")))
            {
                String row = "";
                while ((row = br.readLine()) != null)
                    rows.add(row);
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
            loadedPlayer.add(Player.parsePlayer(rows, goods));
        }

        Collections.sort(loadedPlayer);
    }

    public void refreshItems()
    {
        if(selectedCityIndex < 0)
            return;
        for(StockListItem s: items)
        {
            int stockID = Good.getIndexOfGood(cities.get(selectedCityIndex).getStock(), s.getName());
            if (stockID == -1)
            {
                s.setAmountPurchase(0);
                s.setPricePurchase(-1);
            }
            else
            {
                s.setPricePurchase(calculatePrice(s.getName(), true));
                s.setAmountPurchase(cities.get(selectedCityIndex).getStock().get(stockID).getAmount());
            }
            if(selectedPlayerIndex >= 0) {
                int backPackID = Good.getIndexOfGood(loadedPlayer.get(selectedPlayerIndex).getBackPack(), s.getName());
                if (backPackID == -1) {
                    s.setAmountSale(0);
                    s.setPriceSale(-1);
                } else {
                    s.setPriceSale(calculatePrice(s.getName(), false));
                    s.setAmountSale(loadedPlayer.get(selectedPlayerIndex).getBackPack().get(backPackID).getAmount());
                }
            }
        }
    }

    public void trade(int goodNameNo, boolean isPurchase)
    {
        if (selectedPlayerIndex < 0 || selectedCityIndex < 0)
            return;
        String goodName = items.get(goodNameNo).getName();
        int tradeAmount = amount;
        City tradeCity = cities.get(selectedCityIndex);
        Player tradePlayer = loadedPlayer.get(selectedPlayerIndex);
        int stockID = Good.getIndexOfGood(tradeCity.getStock(), goodName);
        int backPackID = Good.getIndexOfGood(tradePlayer.getBackPack(), goodName);
        Good tradeGood = null;
        if (isPurchase)
        {
            tradeGood = tradeCity.getStock().get(stockID);
            if(tradeCity.getStock().get(stockID).getAmount() < tradeAmount)
                tradeAmount = tradeCity.getStock().get(stockID).getAmount();
        }
        else
        {
            tradeGood = tradePlayer.getBackPack().get(backPackID);
            if (tradePlayer.getBackPack().get(backPackID).getAmount() < tradeAmount)
                tradeAmount = tradePlayer.getBackPack().get(backPackID).getAmount();
        }
        int price = calculatePrice(goodName, isPurchase, tradeAmount);
        if(isPurchase && tradePlayer.getMoney() < price)
        {
            while(tradePlayer.getMoney() < price)
            {
                tradeAmount--;
                price = calculatePrice(goodName, isPurchase, tradeAmount);
                if (price <= 0)
                    break;
            }
        }
        if(isPurchase)
        {
            tradeCity.getStock().get(stockID).setAmount(tradeAmount, false);
            tradePlayer.getBackPack().get(backPackID).setAmount(tradeAmount, true);
            tradePlayer.setMoney(price, false);
        }
        else
        {
            tradeCity.getStock().get(stockID).setAmount(tradeAmount, true);
            tradePlayer.getBackPack().get(backPackID).setAmount(tradeAmount, false);
            tradePlayer.setMoney(price, true);
        }
        savePlayer(tradePlayer);
        saveCity(tradeCity);
    }

    private void saveCity(City city)
    {
        Path file = Paths.get(citiesPath, city.getName() + ".txt");
        try (BufferedWriter bw = Files.newBufferedWriter(file, Charset.forName("UTF-8")))
        {
            for(String l: city.toLines()){
                bw.write(l);
                bw.newLine();
            }
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private void savePlayer(Player player)
    {
        Path file = Paths.get(playerPath, player.getName() + ".txt");
        try (BufferedWriter bw = Files.newBufferedWriter(file, Charset.forName("UTF-8")))
        {
            for(String l: player.toLines()){
                bw.write(l);
                bw.newLine();
            }
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    private void saveGood(Good good)
    {
        Path file = Paths.get(goodsPath, good.getName() + ".txt");
        try (BufferedWriter bw = Files.newBufferedWriter(file, Charset.forName("UTF-8")))
        {
            for(String l: good.toLines()){
                bw.write(l);
                bw.newLine();
            }
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public int calculatePrice(String goodName, boolean isPurchase) {
        return calculatePrice(goodName, isPurchase, -1);
    }
    
    public int calculatePrice(String goodName, boolean isPurchase, int _amount)
    {
        int price = 0;
        if (selectedCityIndex < 0)
            return -1;
        if (selectedPlayerIndex < 0)
        {
            City choosenCity = cities.get(selectedCityIndex);
            int backPackAmount = 0;
            int stockID = Good.getIndexOfGood(choosenCity.getStock(), goodName);
            Good choosenGood = new Good();
            if (stockID != -1)
            {
                choosenGood = choosenCity.getStock().get(stockID);
            }
            else {
                return -1;
            }
            if (_amount == -1)
                _amount = amount;
            if(_amount == 0)
                _amount = 1;
            double tempprice = determinePrice(_amount, choosenGood.getAmount(), choosenGood.getValue(), choosenGood.getBasicRequirement(), choosenCity.getPopulation(), choosenCity.getProduction().contains(choosenGood.getName()), choosenCity.getName().contains(choosenGood.getName()), isPurchase, backPackAmount);
            //Price -% Probe
            price = (int) Math.round(tempprice);

            return price;
        }
        City choosenCity = cities.get(selectedCityIndex);
        Player choosenPlayer = loadedPlayer.get(selectedPlayerIndex);
        int backPackAmount = 0;
        if(!isPurchase)
        {
            Player s = loadedPlayer.get(selectedPlayerIndex);
            int id = Good.getIndexOfGood(s.getBackPack(), goodName);
            backPackAmount = s.getBackPack().get(id).getAmount();
        }
        int stockID = Good.getIndexOfGood(choosenCity.getStock(), goodName);
        Good choosenGood = null;
        if (stockID != -1)
        {
            choosenGood = choosenCity.getStock().get(stockID);
        }
        int backPackID = Good.getIndexOfGood(loadedPlayer.get(selectedPlayerIndex).getBackPack(), goodName);
        if(backPackID != -1 && choosenGood == null)
        {
            choosenGood = choosenPlayer.getBackPack().get(backPackID);
        }
        if(stockID == -1 && backPackID == -1){
            return -1;
        }
        else if(backPackID == -1 && stockID != -1)
        {
            Good newGood = new Good();
            newGood.setName(choosenGood.getName());
            newGood.setPriceClass(choosenGood.getPriceClass());
            newGood.setValue(choosenGood.getValue());
            newGood.setBasicRequirement(choosenGood.getBasicRequirement());
            choosenPlayer.getBackPack().add(newGood);
        }
        else if(stockID == -1 && backPackID != -1)
        {
            Good newGood = new Good();
            newGood.setName(choosenGood.getName());
            newGood.setPriceClass(choosenGood.getPriceClass());
            newGood.setValue(choosenGood.getValue());
            newGood.setBasicRequirement(choosenGood.getBasicRequirement());
            choosenCity.getStock().add(newGood);
        }
        if (_amount == -1)
            _amount = amount;
        if(_amount == 0)
            _amount = 1;
        double tempprice = determinePrice(_amount, choosenGood.getAmount(), choosenGood.getValue(), choosenGood.getBasicRequirement(), choosenCity.getPopulation(), choosenCity.getProduction().contains(choosenGood.getName()), choosenCity.getName().contains(choosenGood.getName()), isPurchase, backPackAmount);
        //Price -% Probe
        price = (int) Math.round(tempprice);

        return price;
    }

    private double determinePrice(int _amount, int stockAmount, int basicValue, double basicRequirement, int population, boolean produces, boolean needs, boolean isPurchase, int backPackAmount)
    {
        if (isPurchase && stockAmount == 0)
            return 0.0;
        if (!isPurchase && backPackAmount == 0)
            return 0.0;
        if (isPurchase)
            backPackAmount++;
        if (!isPurchase)
            stockAmount++;
        if(_amount > 1)
        {
            return priceCalculation(_amount, stockAmount, basicValue, basicRequirement, population, produces, needs, isPurchase, backPackAmount) + determinePrice(_amount - 1, stockAmount-1, basicValue, basicRequirement, population, produces, needs, isPurchase, backPackAmount-1);
        }
        else
        {
            return priceCalculation(_amount, stockAmount, basicValue, basicRequirement, population, produces, needs, isPurchase, backPackAmount);
        }
    }

    //BasicValue +- Basicrequieremnt covered% (between -50% anf +50%) -20% (if city produces) + 25% if needed
    private double priceCalculation(int _amount, int stockAmount, int basicValue, double basicRequirement, int population, boolean produces, boolean needs, boolean isPurchase, int backPackAmount)
    {
        double price = basicValue;
        double covered = stockAmount / (basicRequirement * population);
        if (stockAmount == 1 && isPurchase)
        {
            covered = -1;
        }
        else
        {
            if (covered > 1.5)
                covered = 0.5;
            else if (covered < 0.5)
                covered = -0.5;
            else
                covered = covered - 1;
            if (isPurchase)
            {
                if (covered < 0)
                    covered = covered * 2;
                else
                    covered = covered / 2;
            }
        }

        price = price - (price * covered);
        if (produces)
            price = price - (price * 0.2);
        if (needs)
            price = price + (price * 0.25);
        return price;
    }

    public void produce()
    {
        for(City c: cities)
        {
            for(Good g: c.getStock())
            {
                if(c.getProduction().contains(g.getName()))
                {
                    double consumption = (c.getPopulation() * g.getBasicRequirement() * new Random().nextInt(c.getPopulation() / 10)) / 100;
                    g.setAmount((int) Math.round(consumption), true);
                }
                if(c.getNeeds().contains(g.getName()))
                {
                    double consumption = (c.getPopulation() * g.getBasicRequirement() * new Random().nextInt(c.getPopulation() / 20)) / 100;
                    g.setAmount((int) Math.round(consumption), false);
                    if (g.getAmount() < 0)
                        g.setAmount(getAmount(), true);
                }
            }

            saveCity(c);
        }
    }

    public void openDirectory(String _path){
        try {
            java.awt.Desktop.getDesktop().open(new File(_path));
        }
        catch (IOException iox)
        {}
    }
}
