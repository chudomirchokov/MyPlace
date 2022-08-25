package uni.fmi.masters.myplace;

public class Place {
    private int ID;
    private String Name;
    private String Address;
    private String Phone;
    private String ImagePath;
    private int CategoryID;
    private int rating;

    public Place(String name, String address, String imagePath, String phone, int rating) {
        this.Name = name;
        this.Address = address;
        this.ImagePath = imagePath;
        this.Phone = phone;
        this.rating = rating;
    }

    public Place(){}

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }


    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

