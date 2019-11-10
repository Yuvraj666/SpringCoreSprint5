package com.cg.ibs.loanmgmt.dao;

import java.math.BigInteger;

import com.cg.ibs.loanmgmt.bean.DocumentBean;

public interface DocumentDao {
	public DocumentBean getDocumentByApplicantNum(BigInteger applicantNum);
}
