package cn.sliew.carp.module.application.oam.model.status;

import lombok.Data;

@Data
public class Revision {

    private String name;
    private String revision;
    /**
     * RevisionHash record the hash value of the spec of ApplicationRevision object.
     */
    private String revisionHash;
}
