package com.epam.esm.model.dto;

public class SortDto {

    private String date;
    private String name;

    public SortDto(){}

    public SortDto(String date, String name){
        this.name = name;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SortDto sortDto = (SortDto) o;

        if (date != null ? !date.equals(sortDto.date) : sortDto.date != null) return false;
        return name != null ? name.equals(sortDto.name) : sortDto.name == null;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SortDto{" + "date='" + date + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
