package RPGTool;

import java.util.ArrayList;

public class City implements Comparable {
    private String name;
    private int population;
    private ArrayList<String> production;
    private ArrayList<String> needs;
    private ArrayList<Good> stock;

    public String getName() { return name; }
    public int getPopulation() { return population; }
    public ArrayList<String> getProduction() { return production; }
    public ArrayList<String> getNeeds() { return needs; }
    public ArrayList<Good> getStock() { return stock; }

    public void setName(String _name) { name = _name; }
    public void setPopulation(int _population) { population = _population; }

    public City()
    {
        name = "Unkown City";
        population = 1;
        production = new ArrayList<>();
        needs = new ArrayList<>();
        stock = new ArrayList<>();
    }

    public static City parseCity(ArrayList<String> input, ArrayList<Good> goods)
    {
        City s = new City();
        String[] row = input.get(0).split(":");
        s.name = row[1];
        row = input.get(1).split(":");
        try
        {
            s.population = Integer.parseInt(row[1]);
        }
        catch(Exception e)
        {
            s.population = 1;
        }
        row = input.get(2).split(":");
        String[] resources = row[1].split(";");
        for(int i = 0; i < resources.length; i++)
        {
            String[] Good = resources[i].split("\\|");
            Good g = new Good();
            g.setName(Good[0]);
            try
            {
                g.setAmount(Integer.parseInt(Good[1]), true);
            }
            catch (Exception e)
            {
                g.setAmount(0, true);
            }
            s.getStock().add(g);
        }
        row = input.get(3).split(":");
        resources = row[1].split(";");
        for (int i = 0; i < resources.length; i++)
        {
            s.production.add(resources[i]);
        }
        row = input.get(4).split(":");
        resources = row[1].split(";");
        for (int i = 0; i < resources.length; i++)
        {
            s.needs.add(resources[i]);
        }

        for(Good g: s.stock)
        {
            int a = Good.getIndexOfGood(goods, g.getName());
            if(a >= 0)
            {
                g.setPriceClass(goods.get(a).getPriceClass());
                g.setValue(goods.get(a).getValue());
                g.setBasicRequirement(goods.get(a).getBasicRequirement());
            }
        }
        return s;
    }

    public static int getIndexOfCity(ArrayList<City> cities, String name)
    {
        for (int i = 0; i < cities.size(); i++)
        {
            if (cities.get(i).getName() == name)
                return i;
        }
        return -1;
    }

    public ArrayList<String> toLines()
    {
        ArrayList<String> lines = new ArrayList<>();
        String line = "Name:" + name;
        lines.add(line);
        line = "Population:" + population;
        lines.add(line);
        line = "Lager:";
        for(Good g: stock)
        {
            line += g.getName() + "|" + g.getValue() + ";";
        }
        if(line.endsWith(";"))
            line = line.substring(0, line.length() - 1);
        lines.add(line);
        line = "Production:";
        for(String g: production)
        {
            line += g + ";";
        }
        if (line.endsWith(";"))
            line = line.substring(0, line.length() - 1);
        lines.add(line);
        line = "Needs:";
        for (String g: needs)
        {
            line += g + ";";
        }
        if (line.endsWith(";"))
            line = line.substring(0, line.length() - 1);
        lines.add(line);
        return lines;
    }

    @Override
    public int compareTo(Object o) {
        City c;
        try {
            c = (City) o;
        }
        catch(Exception e)
        {
            return -1;
        }
        return name.compareTo(c.name);
    }
}
