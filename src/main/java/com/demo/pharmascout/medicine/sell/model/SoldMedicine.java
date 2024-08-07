package com.demo.pharmascout.medicine.sell.model;

import com.demo.pharmascout.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * @author cocat
 * @created 07-08-2024 - 09:07 PM
 * @package-name com.demo.pharmascout.medicine.sell.model
 * @project PharmaScout
 */

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "sold_medicine")
@EqualsAndHashCode(callSuper = true)
public class SoldMedicine extends Auditable implements Serializable {

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
	private double discountedPrice;

	@Column(nullable = false)
	private double actualPrice;

	private float discount;

}
