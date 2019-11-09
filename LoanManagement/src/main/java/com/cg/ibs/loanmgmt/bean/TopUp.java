package com.cg.ibs.loanmgmt.bean;

	import java.math.BigDecimal;
	import java.math.BigInteger;
	import java.time.LocalDate;

	import javax.persistence.Column;
	import javax.persistence.Entity;
	import javax.persistence.EnumType;
	import javax.persistence.Enumerated;
	import javax.persistence.Id;
	import javax.persistence.Table;

	@Entity
	@Table(name = "Top_Up")
	public class TopUp {
		@Id
		@Column(name = "top_Up_Id")
		private BigInteger topUpId;
		@Column(name = "application_num", nullable = false)
		private BigInteger applicationNumber;
		@Column(name = "top_Up_Amount", nullable = false)
		private BigDecimal topUpAmount;
		@Column(name = "top_Up_tenure", nullable = false)
		private int topUpTenure;
		@Column(name = "top_Up_Emi", nullable = false)
		private BigDecimal topUpEmi;
		@Column(name = "top_Up_Balance", nullable = false)
		private BigDecimal topUpBalance;
		@Enumerated(EnumType.STRING)
		@Column(name = "top_up_status", nullable = false)
		private TopUpStatus topUpStatus;
		@Column(name = "topUp_applied_date")
		private LocalDate topUpAppliedDate;
		@Column(name = "topUp_approved_date")
		private LocalDate topUpApprovedDate;
	}