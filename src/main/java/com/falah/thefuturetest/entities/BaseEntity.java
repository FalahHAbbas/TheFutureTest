package com.falah.thefuturetest.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data

public class BaseEntity {
    private Date createdDate = Date.from(java.time.Instant.now());
    private Date updatedDate = Date.from(java.time.Instant.now());
    private UUID createdBy;
    private UUID updatedBy;
    private boolean isDeleted;


}
