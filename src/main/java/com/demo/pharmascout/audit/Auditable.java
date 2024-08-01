package com.demo.pharmascout.audit;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
public abstract class Auditable {

	@CreatedDate
	@Temporal( TemporalType.TIMESTAMP )
	@JsonFormat ( pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Kolkata", shape = JsonFormat.Shape.STRING, locale = "en_IN" )
	private LocalDateTime createdDate;

	@LastModifiedDate
	@Temporal( TemporalType.TIMESTAMP )
	@JsonFormat ( pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Kolkata", shape = JsonFormat.Shape.STRING, locale = "en_IN" )
	private LocalDateTime lastModifiedDate;
}
