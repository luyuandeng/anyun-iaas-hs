package com.anyun.cloud.param;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/13/16
 */
public class ThirdpartApplicationConnectParam {
    private String x500DomainContent;   //O=包头公安,OU=包头公安信通处,L=包头,ST=内蒙古,C=CN
    private String name;    //应用名称
    private String descript;
    private boolean useOpend;  //是否使用平台OpenID
    private String icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isUseOpend() {
        return useOpend;
    }

    public void setUseOpend(boolean useOpend) {
        this.useOpend = useOpend;
    }

    public String getX500DomainContent() {
        return x500DomainContent;
    }

    public void setX500DomainContent(String x500DomainContent) {
        this.x500DomainContent = x500DomainContent;
    }
}
