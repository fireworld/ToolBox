package cc.colorcat.toolbox.entity;

import java.util.List;
import java.util.Objects;

/**
 * Created by cxx on 2018/4/27.
 * xx.ch@outlook.com
 */
public class Province {
    private String enName;
    private String cnName;
    private List<City> cities;

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

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Province province = (Province) o;
        return Objects.equals(enName, province.enName) &&
                Objects.equals(cnName, province.cnName) &&
                Objects.equals(cities, province.cities);
    }

    @Override
    public int hashCode() {

        return Objects.hash(enName, cnName, cities);
    }

    @Override
    public String toString() {
        return "Province{" +
                "enName='" + enName + '\'' +
                ", cnName='" + cnName + '\'' +
                ", cities=" + cities +
                '}';
    }
}
