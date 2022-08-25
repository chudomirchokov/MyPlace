package uni.fmi.masters.myplace;

public class Category {
    private int ID;
    private String Name;
    private String IconPath;

    public Category(int id, String name, String iconPath) {
        this.ID = id;
        this.Name = name;
        this.IconPath = iconPath;
    }

    public Category(){}

    public String getIconPath() {
        return IconPath;
    }

    public void setIconPath(String iconPath) {
        IconPath = iconPath;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
