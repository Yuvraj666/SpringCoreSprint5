package com.cg.ibs.loanmgmt.bean;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "document")
@SequenceGenerator(name = "docseq", initialValue = 6000, allocationSize = 1)
public class DocumentBean implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "docseq")
	@Column(name = "document_Id", nullable = false)
	private long documentId;
	@Column(name = "application_num", nullable = false)
	private BigInteger applicationNumber;
	@Lob
	@Column(name = "file", nullable = true)
	private byte[] document;
	@Column(name = "document_name", nullable = false)
	private String documentName;

	public long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(long documentId) {
		this.documentId = documentId;
	}

	public BigInteger getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(BigInteger applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public byte[] getDocument() {
		return document;
	}

	public void setDocument(byte[] document) {
		this.document = document;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public DocumentBean() {
		super();
	}

}
