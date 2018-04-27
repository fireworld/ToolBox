package cc.colorcat.toolbox.entity;

import java.util.Objects;

/**
 * Created by cxx on 2018/4/27.
 * xx.ch@outlook.com
 */
public class Region {
    private String code;
    private String enName;
    private String cnName;
    private String stateCode;
    private String stateEnName;
    private String stateCnName;
    private String provEnName;
    private String provCnName;
    private String cityEnName;
    private String cityCnName;
    private String latitude;
    private String longitude;
    private String adCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateEnName() {
        return stateEnName;
    }

    public void setStateEnName(String stateEnName) {
        this.stateEnName = stateEnName;
    }

    public String getStateCnName() {
        return stateCnName;
    }

    public void setStateCnName(String stateCnName) {
        this.stateCnName = stateCnName;
    }

    public String getProvEnName() {
        return provEnName;
    }

    public void setProvEnName(String provEnName) {
        this.provEnName = provEnName;
    }

    public String getProvCnName() {
        return provCnName;
    }

    public void setProvCnName(String provCnName) {
        this.provCnName = provCnName;
    }

    public String getCityEnName() {
        return cityEnName;
    }

    public void setCityEnName(String cityEnName) {
        this.cityEnName = cityEnName;
    }

    public String getCityCnName() {
        return cityCnName;
    }

    public void setCityCnName(String cityCnName) {
        this.cityCnName = cityCnName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return Objects.equals(code, region.code) &&
                Objects.equals(enName, region.enName) &&
                Objects.equals(cnName, region.cnName) &&
                Objects.equals(stateCode, region.stateCode) &&
                Objects.equals(stateEnName, region.stateEnName) &&
                Objects.equals(stateCnName, region.stateCnName) &&
                Objects.equals(provEnName, region.provEnName) &&
                Objects.equals(provCnName, region.provCnName) &&
                Objects.equals(cityEnName, region.cityEnName) &&
                Objects.equals(cityCnName, region.cityCnName) &&
                Objects.equals(latitude, region.latitude) &&
                Objects.equals(longitude, region.longitude) &&
                Objects.equals(adCode, region.adCode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(code, enName, cnName, stateCode, stateEnName, stateCnName, provEnName, provCnName, cityEnName, cityCnName, latitude, longitude, adCode);
    }

    @Override
    public String toString() {
        return "Region{" +
                "code='" + code + '\'' +
                ", enName='" + enName + '\'' +
                ", cnName='" + cnName + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", stateEnName='" + stateEnName + '\'' +
                ", stateCnName='" + stateCnName + '\'' +
                ", provEnName='" + provEnName + '\'' +
                ", provCnName='" + provCnName + '\'' +
                ", cityEnName='" + cityEnName + '\'' +
                ", cityCnName='" + cityCnName + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", adCode='" + adCode + '\'' +
                '}';
    }
}
