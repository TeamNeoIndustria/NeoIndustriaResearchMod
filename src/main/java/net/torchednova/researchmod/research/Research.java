package net.torchednova.researchmod.research;

public class Research {
    public Research(int id, String name, String itemID, String displayname, String lore)
    {
        this.id = id;
        this.name = name;
        this.displayname = displayname;
        this.itemID = itemID;
        this.lore = lore;
        this.votes = 0;
    }

    public int id;
    public String name;
    public String displayname;
    public String itemID;
    public String lore;
    public int votes;
}
