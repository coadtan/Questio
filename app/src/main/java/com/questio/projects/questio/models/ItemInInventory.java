package com.questio.projects.questio.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ning jittima on 17/7/2558.
 */
public class ItemInInventory {
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

    @SerializedName("isequipped")
    private int isEquipped;

    public String getEquipSpritePath() {
        return equipSpritePath;
    }

    public void setEquipSpritePath(String equipSpritePath) {
        this.equipSpritePath = equipSpritePath;
    }

    public int getIsEquipped() {
        return isEquipped;
    }

    public void setIsEquipped(int isEquipped) {
        this.isEquipped = isEquipped;
    }

    public String getItemCollection() {
        return itemCollection;
    }

    public void setItemCollection(String itemCollection) {
        this.itemCollection = itemCollection;
    }

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

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    @Override
    public String toString() {
        return "ItemInInventory{" +
                "equipSpritePath='" + equipSpritePath + '\'' +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemPicPath='" + itemPicPath + '\'' +
                ", itemCollection='" + itemCollection + '\'' +
                ", positionId=" + positionId +
                ", isEquipped=" + isEquipped +
                '}';
    }
}
