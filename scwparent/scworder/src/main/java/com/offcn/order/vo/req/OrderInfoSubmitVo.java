package com.offcn.order.vo.req;

public class OrderInfoSubmitVo {


    private String accessToken;
    private Integer projectid;//项目ID
    private Integer returnid;//回报ID
    private Integer rtncount;//回报数量
    private String address;//收货地址
    private Byte invoice;//是否开发票 0 - 不开发票， 1 - 开发票
    private String invoictitle;//发票名头
    private String remark;//备注

    public OrderInfoSubmitVo(String accessToken, Integer projectid, Integer returnid, Integer rtncount, String address, Byte invoice, String invoictitle, String remark) {
        this.accessToken = accessToken;
        this.projectid = projectid;
        this.returnid = returnid;
        this.rtncount = rtncount;
        this.address = address;
        this.invoice = invoice;
        this.invoictitle = invoictitle;
        this.remark = remark;
    }

    public OrderInfoSubmitVo() {
    }

    @Override
    public String toString() {
        return "OrderInfoSubmitVo{" +
                "accessToken='" + accessToken + '\'' +
                ", projectid=" + projectid +
                ", returnid=" + returnid +
                ", rtncount=" + rtncount +
                ", address='" + address + '\'' +
                ", invoice=" + invoice +
                ", invoictitle='" + invoictitle + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getProjectid() {
        return projectid;
    }

    public void setProjectid(Integer projectid) {
        this.projectid = projectid;
    }

    public Integer getReturnid() {
        return returnid;
    }

    public void setReturnid(Integer returnid) {
        this.returnid = returnid;
    }

    public Integer getRtncount() {
        return rtncount;
    }

    public void setRtncount(Integer rtncount) {
        this.rtncount = rtncount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Byte getInvoice() {
        return invoice;
    }

    public void setInvoice(Byte invoice) {
        this.invoice = invoice;
    }

    public String getInvoictitle() {
        return invoictitle;
    }

    public void setInvoictitle(String invoictitle) {
        this.invoictitle = invoictitle;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
