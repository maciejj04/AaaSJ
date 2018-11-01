package com.maciejj.AaaSJ.domain;

import javax.persistence.*;

@Entity
public class AudioFileDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CREATOR_ID")
    private String creator;         // UUID ?

    @Embedded
    private BaseAudioAttributes baseAttributes;

    @Embedded
    private ComputedAudioAttributes attributes;

//    private AudioFileMetadata metadata;

//    public AudioFileDetails() { }

    public AudioFileDetails(String name, String creator, BaseAudioAttributes baseAttributes, ComputedAudioAttributes attributes) {
        this.name = name;
        this.creator = creator;
        this.baseAttributes = baseAttributes;
        this.attributes = attributes;
    }

    public BaseAudioAttributes getBaseAttributes() {
        return baseAttributes;
    }

    public void setBaseAttributes(BaseAudioAttributes baseAttributes) {
        this.baseAttributes = baseAttributes;
    }

    public String getName() {
        return name;
    }

    public String getCreator() {
        return creator;
    }

    public ComputedAudioAttributes getAttributes() {
        return attributes;
    }
}
