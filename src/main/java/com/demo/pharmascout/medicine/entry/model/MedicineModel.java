package com.demo.pharmascout.medicine.entry.model;

import com.demo.pharmascout.audit.Auditable;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.SortedSet;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "medicine")
@ToString(exclude = "expiryDates")
@EqualsAndHashCode(callSuper = true, exclude = "expiryDates")
public class MedicineModel extends Auditable implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	private String manufacturer;

	private String genericName;

	@Column(nullable = false)
	private String dosage;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false)
	private double price;

	private float discount;

	@JsonManagedReference
	@OneToMany(
			mappedBy = "medicine",
			fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	private SortedSet<ExpiryDates> expiryDates;

}
