package com.epam.esm.domain;

import java.util.List;

public class Tag {
    private long id;
    private String name;
    private List<GiftCertificate> giftCertificateList;

    public Tag(){}

    public Tag(String name) {
        this.name = name;
    }

    public Tag(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Tag(long id, String name, List<GiftCertificate> giftCertificateList) {
        this.id = id;
        this.name = name;
        this.giftCertificateList = giftCertificateList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GiftCertificate> getGiftCertificateList() {
        return giftCertificateList;
    }

    public void setGiftCertificateList(List<GiftCertificate> giftCertificateList) {
        this.giftCertificateList = giftCertificateList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tag{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", giftCertificateList=").append(giftCertificateList);
        sb.append('}');
        return sb.toString();
    }
}
