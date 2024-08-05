package com.demo.pharmascout.medicine.entry.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "expiry_dates")
@ToString(exclude = "medicine")
@EqualsAndHashCode(of = {"expiryDate"})
public class ExpiryDates {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonFormat (
			pattern = "yyyy-MM-dd",
			timezone = "Asia/Kolkata",
			shape = JsonFormat.Shape.STRING,
			locale = "en_IN"
	)
	private LocalDate expiryDate;

	@Column (nullable = false)
	private int quantity;

	@JsonBackReference
	@ManyToOne (
			fetch = FetchType.LAZY,
			optional = false
	)
	@JoinColumn (
			name = "medicine_id",
			referencedColumnName = "id"
	)
	private MedicineModel medicine;

	@Column (nullable = false)
	private boolean isExpired = false;

}
