package RPGTool;

import java.util.ArrayList;

public class Player implements Comparable {
    private String name;
    private int money;
    private ArrayList<Good> backPack;

    public String getName() { return name; }
    public int getMoney() { return money; }
    public ArrayList<Good> getBackPack() { return backPack; }

    public void setName(String _name) { name = _name; }
    public void setMoney(int _money, boolean add) {
        if(add) {
            money += _money;
        }
        else {
            money -= _money;
        }
            }

    public Player()
    {
        name = "Unkown Player";
        money = 0;
        backPack = new ArrayList<>();
    }

    public static Player parsePlayer(ArrayList<String> input, ArrayList<Good> goods)
    {
        Player s = new Player();
        String[] row = input.get(0).split(":");
        s.name = row[1];
        row = input.get(1).split(":");
        try
        {
            s.money = Integer.parseInt(row[1]);
        }
        catch (Exception e)
        {
            s.money = 0;
        }
        row = input.get(2).split(":");
        String[] resources = row[1].split(";");
        for (int i = 0; i < resources.length; i++)
        {
            String[] oneGood = resources[i].split("\\|");
            Good g = new Good();
            g.setName(oneGood[0]);
            try
            {
                g.setAmount(Integer.parseInt(oneGood[1]), true);
            }
            catch (Exception e)
            {
                g.setAmount(0, true);
            }
            s.backPack.add(g);
        }
        for(Good g: s.backPack)
        {
            int a = Good.getIndexOfGood(goods, g.getName());
            if (a >= 0)
            {
                g.setPriceClass(goods.get(a).getPriceClass());
                g.setValue(goods.get(a).getValue());
                g.setBasicRequirement(goods.get(a).getBasicRequirement());
            }
        }
        return s;
    }

    public static int getIndexOfPlayer(ArrayList<Player> Player, String _name)
    {
        for (int i = 0; i < Player.size(); i++)
        {
            if (Player.get(i).name.compareTo(_name) == 0)
                return i;
        }
        return -1;
    }

    public ArrayList<String> toLines()
    {
        ArrayList<String> lines = new ArrayList<>();
        String line = "Name:" + name;
        lines.add(line);
        line = "Money:" + money;
        lines.add(line);
        line = "Backpack:";
        for (Good g: backPack)
        {
            line += g.getName() + "|" + g.getAmount() + ";";
        }
        if (line.endsWith(";"))
            line = line.substring(0, line.length() - 1);
        lines.add(line);
        return lines;
    }

    @Override
    public int compareTo(Object o) {
        Player p;
        try {
            p = (Player) o;
        }
        catch(Exception e)
        {
            return -1;
        }
        return name.compareTo(p.name);
    }
}
