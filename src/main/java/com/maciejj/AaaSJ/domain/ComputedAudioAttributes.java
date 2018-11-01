package com.maciejj.AaaSJ.domain;

import javax.persistence.Embeddable;

@Embeddable
public class ComputedAudioAttributes {

    private int offset;

    public ComputedAudioAttributes(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
