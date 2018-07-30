package RPGTool;

import java.util.ArrayList;

public class Good implements Comparable {
    private String name;
    private int amount;
    private int priceClass;
    private int value;
    private double basicRequirement;

    public String getName() { return name; }
    public int getAmount() { return amount; }
    public int getPriceClass() { return priceClass; }
    public int getValue() { return value; }
    public double getBasicRequirement() { return basicRequirement; }

    public void setName(String _name) { name = _name; }
    public void setAmount(int _amount, boolean add) {
        if (add) {
            amount += _amount;
        } else {
            amount -= _amount;
        }
    }
    public void setPriceClass(int _priceClass) { priceClass = _priceClass; }
    public void setValue(int _value) { value = _value; }
    public void setBasicRequirement(double _basicRequirement) { basicRequirement = _basicRequirement; }

    public Good()
    {
        name = "Unkown Good";
        amount = 0;
        priceClass = 0;
        value = 0;
        basicRequirement = 0.0;
    }

    public static Good parseGood(ArrayList<String> input)
    {
        Good g = new Good();
        String[] row = input.get(0).split(":");
        g.name = row[1];
        row = input.get(1).split(":");
        try
        {
            g.priceClass = Integer.parseInt(row[1]);
        }
        catch (Exception e)
        {
            g.priceClass = 1;
        }
        row = input.get(2).split(":");
        try
        {
            g.value = Integer.parseInt(row[1]);
        }
        catch (Exception e)
        {
            g.value = 0;
        }

        row = input.get(3).split(":");
        try
        {
            g.basicRequirement = Double.parseDouble(row[1]);
        }
        catch(Exception e)
        {
            g.basicRequirement = 1.0;
        }
        return g;
    }

    public static int getIndexOfGood(ArrayList<Good> goods, String _name)
    {
        for (int i = 0; i < goods.size(); i++)
        {
            if (goods.get(i).name.compareTo(_name) == 0)
                return i;
        }
        return -1;
    }

    public ArrayList<String> toLines()
    {
        ArrayList<String> lines = new ArrayList<>();
        String line = "Name:" + name;
        lines.add(line);
        line = "PriceClass:" + priceClass;
        lines.add(line);
        line = "Value:" + value;
        lines.add(line);
        line = "BasicRequierment:" + basicRequirement;
        lines.add(line);
        return lines;
    }

    @Override
    public int compareTo(Object o) {
        Good g;
        try {
            g = (Good) o;
        }
        catch(Exception e)
        {
            return -1;
        }
        if (priceClass == g.priceClass)
        {
            return name.compareTo(g.name);
        }
        else if (priceClass > g.priceClass) {
            return 1;
        }
        else {
            return -1;
        }
    }

}
