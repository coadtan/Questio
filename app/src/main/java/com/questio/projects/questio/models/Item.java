package com.questio.projects.questio.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ning jittima on 4/7/2558.
 */
public class Item {

    private static final String LOG_TAG = Item.class.getSimpleName();


    /*itemid
    itemname
    itempicpath
    equipspritepath
    itemcollection
    positionid
    */

    @SerializedName("itemid")
    private int itemId;

    @SerializedName("itemname")
    private String itemName;

    @SerializedName("itempicpath")
    private String itemPicPath;

    @SerializedName("equipspritepath")
    private String equipSpritePath;

    @SerializedName("itemcollection")
    private String itemCollection;

    @SerializedName("positionid")
    private int positionId;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPicPath() {
        return itemPicPath;
    }

    public void setItemPicPath(String itemPicPath) {
        this.itemPicPath = itemPicPath;
    }

    public String getEquipSpritePath() {
        return equipSpritePath;
    }

    public void setEquipSpritePath(String equipSpritePath) {
        this.equipSpritePath = equipSpritePath;
    }

    public String getItemCollection() {
        return itemCollection;
    }

    public void setItemCollection(String itemCollection) {
        this.itemCollection = itemCollection;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemPicPath='" + itemPicPath + '\'' +
                ", equipSpritePath='" + equipSpritePath + '\'' +
                ", itemCollection='" + itemCollection + '\'' +
                ", positionId=" + positionId +
                '}';
    }
}
