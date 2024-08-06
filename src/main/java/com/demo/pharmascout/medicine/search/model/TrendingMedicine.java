package com.demo.pharmascout.medicine.search.model;

import com.demo.pharmascout.audit.Auditable;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table ( name = "trending_medicine" )
@EqualsAndHashCode ( callSuper = true )
public class TrendingMedicine extends Auditable implements Serializable {

	@Id
	@GeneratedValue ( strategy = GenerationType.IDENTITY )
	private Long id;

	@Column ( nullable = false )
	private String name;

	@Column ( nullable = false )
	private int quantity;

	@JsonFormat (
			pattern = "yyyy-MM-dd",
			timezone = "Asia/Kolkata",
			shape = JsonFormat.Shape.STRING,
			locale = "en_IN"
	)
	private LocalDate date;

}
