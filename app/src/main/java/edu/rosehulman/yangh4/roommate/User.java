package edu.rosehulman.yangh4.roommate;

import java.util.ArrayList;

/**
 * Created by Hao Yang on 7/22/2017.
 */

public class User extends People {
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public ArrayList<Group> getBelonggroup() {
        return belonggroup;
    }

    public void setBelonggroup(ArrayList<Group> belonggroup) {
        this.belonggroup = belonggroup;
    }

    private ArrayList<Group> belonggroup;

}
