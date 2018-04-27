package cc.colorcat.toolbox.entity;

import java.util.List;
import java.util.Objects;

/**
 * Created by cxx on 2018/4/27.
 * xx.ch@outlook.com
 */
public class City {
    private String enName;
    private String cnName;
    private List<Region> regions;

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

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(enName, city.enName) &&
                Objects.equals(cnName, city.cnName) &&
                Objects.equals(regions, city.regions);
    }

    @Override
    public int hashCode() {

        return Objects.hash(enName, cnName, regions);
    }

    @Override
    public String toString() {
        return "City{" +
                "enName='" + enName + '\'' +
                ", cnName='" + cnName + '\'' +
                ", regions=" + regions +
                '}';
    }
}
