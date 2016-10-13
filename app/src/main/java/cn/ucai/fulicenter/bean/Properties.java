package cn.ucai.fulicenter.bean;

/**
 * Created by Administrator on 2016/10/13 0013.
 */

public class Properties {


    /**
     * id : 9529
     * goodsId : 0
     */

    private int id;
    private int goodsId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public String toString() {
        return "Properties{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                '}';
    }
}
