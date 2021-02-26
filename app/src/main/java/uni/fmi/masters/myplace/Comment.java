package uni.fmi.masters.myplace;

public class Comment {
    private int ID;
    private int userID;
    private int placeID;
    private int rating;
    private String comment;

    public Comment(int userID, int placeID, int rating, String comment) {
        this.userID = userID;
        this.placeID = placeID;
        this.rating = rating;
        this.comment = comment;
    }

    public Comment(){}

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPlaceID() {
        return placeID;
    }

    public void setPlaceID(int placeID) {
        this.placeID = placeID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
