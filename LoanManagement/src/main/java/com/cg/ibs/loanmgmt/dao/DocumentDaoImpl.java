package com.cg.ibs.loanmgmt.dao;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.cg.ibs.loanmgmt.bean.DocumentBean;
import com.cg.ibs.loanmgmt.util.JpaUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository("DocumentDao")
public class DocumentDaoImpl implements DocumentDao {
	private EntityManager entityManager;
	private static Logger LOGGER = Logger.getLogger(DocumentDao.class);
	DocumentBean document = new DocumentBean();

	public DocumentDaoImpl() {
		entityManager = JpaUtil.getEntityManger();
	}

	public DocumentBean getDocumentByApplicantNum(BigInteger applicantNum) {
		LOGGER.info("Applicant number is input to obtain documents.");
		TypedQuery<DocumentBean> query = entityManager
				.createQuery("Select d from DocumentBean l where applicationNumber=?1", DocumentBean.class);
		query.setParameter(1, applicantNum);
		document = query.getSingleResult();
		return document;
	}
}
