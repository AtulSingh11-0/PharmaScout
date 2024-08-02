package com.demo.pharmascout.medicine.expired.model;

import com.demo.pharmascout.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "expired_medicine")
@EqualsAndHashCode(callSuper = true)
public class ExpiredMedicineModel extends Auditable implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String name;

	private String manufacturer;

	private String genericName;

	private String dosage;

	@Column(nullable = false)
	private int quantity;

}
