package com.demo.pharmascout.medicine.entry.model;

import com.demo.pharmascout.audit.Auditable;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "medicine")
@EqualsAndHashCode ( callSuper = true )
//@EntityListeners(value = AuditingEntityListener.class)
public class MedicineModel extends Auditable implements Serializable {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private int id;

	@Column( nullable = false )
	private String name;

	private String manufacturer;

	private String genericName;

	private String dosage;

	@Column( nullable = false )
	private int quantity;

	@Column( nullable = false )
	private double price;

	private float discount;

//	@JsonFormat( pattern = "yyyy-MM-dd", timezone = "Asia/Kolkata", shape = JsonFormat.Shape.STRING, locale = "en_IN" )
	private Map<Object, Integer> mfgDate;

	@Column( nullable = false )
	@JsonFormat( pattern = "yyyy-MM-dd", timezone = "Asia/Kolkata", shape = JsonFormat.Shape.STRING, locale = "en_IN" )
	private LocalDate expDate;
}
