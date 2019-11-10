package com.cg.ibs.loanmgmt.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.ibs.loanmgmt.bean.BankAdmins;
import com.cg.ibs.loanmgmt.bean.CustomerBean;
import com.cg.ibs.loanmgmt.bean.DocumentBean;
import com.cg.ibs.loanmgmt.bean.LoanMaster;
import com.cg.ibs.loanmgmt.bean.LoanTypeBean;
import com.cg.ibs.loanmgmt.bean.TopUp;
import com.cg.ibs.loanmgmt.bean.TopUpStatus;
import com.cg.ibs.loanmgmt.dao.BankAdminsDao;
import com.cg.ibs.loanmgmt.dao.CustomerDao;
import com.cg.ibs.loanmgmt.dao.DocumentDao;
import com.cg.ibs.loanmgmt.dao.LoanMasterDao;
import com.cg.ibs.loanmgmt.dao.LoanTypeDao;
import com.cg.ibs.loanmgmt.dao.TopUpDao;
import com.cg.ibs.loanmgmt.util.JpaUtil;

@Service("BankService")
public class BankServiceImpl implements BankService {
	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private LoanMasterDao loanMasterDao;
	@Autowired
	private BankAdminsDao bankAdminsDao;
	@Autowired
	private LoanTypeDao loanTypeDao;
	@Autowired
	private DocumentDao docDao;
	@Autowired
	private TopUpDao topUpDao;
	private TopUp topUp = new TopUp();

	private static Logger LOGGER = Logger.getLogger(BankServiceImpl.class);
	private LoanMaster loanMaster = new LoanMaster();
	private static DocumentBean doc;

	@Override
	public boolean verifyBankLogin(String userId, String password) {
		LOGGER.info("Verifying customer login details");
		boolean login = false;
		BankAdmins admin = bankAdminsDao.getAdminByUserId(userId);
		if (null != admin && password.equals(admin.getPassword())) {
			login = true;
		}
		return login;
	}

	public LoanTypeBean getLoanTypeByTypeID(Integer typeId) {
		return loanTypeDao.getLoanTypeByTypeID(typeId);
	}

	public CustomerBean getCustomerDetailsByUci(BigInteger uci) {
		return customerDao.getCustomerDetailsByUci(uci);
	}

	public BigInteger generateLoanNumber(LoanMaster loanMaster) {
		StringBuilder sb = new StringBuilder();
		sb.append(loanMaster.getAppliedDate().getYear()).append(loanMaster.getAppliedDate().getMonthValue())
				.append(loanMaster.getApplicationNumber());
		BigInteger bigInteger = new BigInteger(sb.toString());
		return bigInteger;

	}

	public LoanMaster updateLoanApproval(LoanMaster loanMasterTemp) {
		EntityTransaction transaction = JpaUtil.getTransaction();

		transaction.begin();
		loanMaster = loanMasterDao.updateLoanApprovalDao(loanMasterTemp, generateLoanNumber(loanMasterTemp));
		transaction.commit();
		return loanMaster;
	}

	@Override
	public BankAdmins getBankAdmin(String userId) {
		LOGGER.info("Fetching customer details");
		return bankAdminsDao.getAdminByUserId(userId);
	}

	public List<LoanMaster> getPendingLoans() {
		List<LoanMaster> listTemp = null;
		listTemp = loanMasterDao.getPendingLoans();
		return listTemp;
	}

	public List<LoanMaster> getPendingPreClosures() {
		List<LoanMaster> listTemp = null;
		listTemp = loanMasterDao.getPendingPreClosures();
		return listTemp;
	}

	public LoanMaster updatePreClosureApproval(LoanMaster loanMasterTemp) {
		EntityTransaction transaction = JpaUtil.getTransaction();
		transaction.begin();
		loanMaster = loanMasterDao.updatePreClosureApprovalDao(loanMasterTemp);
		transaction.commit();
		return loanMaster;
	}

	@Override
	public void downloadDocument(LoanMaster loanMaster) {
		doc = getDocumentByAppNum(loanMaster.getApplicationNumber());

		byte[] content = doc.getDocument();
		File dir = new File("./downloads");
		if (!dir.exists()) {
			dir.mkdir();
		}
		try (FileOutputStream outputStream = new FileOutputStream(
				dir.getPath() + "/" + loanMaster.getApplicationNumber() + ".pdf")) {
			outputStream.write(content);
			outputStream.flush();
			outputStream.close();

		} catch (FileNotFoundException exp) {
			System.out.println(exp.getMessage());
		} catch (IOException exp1) {
			System.out.println(exp1.getMessage());
		}
	}

	private DocumentBean getDocumentByAppNum(BigInteger applicationNumber) {
		doc = docDao.getDocumentByApplicantNum(applicationNumber);
		return doc;
	}

	public void updateLoanDenial(LoanMaster loanMasterTemp) {
		EntityTransaction transaction = JpaUtil.getTransaction();
		transaction.begin();
		loanMasterDao.updateLoanDenialDao(loanMasterTemp);
		transaction.commit();

	}

	@Override
	public TopUp setTopUp(TopUp topUpTemp) {
		EntityTransaction transaction = JpaUtil.getTransaction();
		transaction.begin();
		topUpTemp.setTopUpStatus(TopUpStatus.APPROVED);
		topUpTemp.setNumOftopUps(topUp.getNumOftopUps() + 1);
		generateTopUpId(topUpTemp);
		topUpDao.updateTopUpApprovalDao(topUpTemp);
		transaction.commit();
		return topUp;
	}

	public BigInteger generateTopUpId(TopUp topUpTemp) {
		StringBuilder sb = new StringBuilder();
		sb.append(topUpTemp.getApplicationNumber()).append("-").append(topUpTemp.getNumOftopUps());
		BigInteger topUpId = new BigInteger(sb.toString());
		return topUpId;
	}

	@Override
	public void updateTopUpDenial(TopUp topUp) {
		EntityTransaction transaction = JpaUtil.getTransaction();
		transaction.begin();
		topUp.setTopUpStatus(TopUpStatus.DENIED);
		topUpDao.updateTopUpDenialDao(topUp);
		transaction.commit();

	}

	@Override
	public LoanMaster getLoanByApplicantNum(BigInteger applicantNum) {
		EntityTransaction transaction = JpaUtil.getTransaction();
		transaction.begin();
		loanMaster = loanMasterDao.getLoanByApplicantNumber(applicantNum);
		transaction.commit();
		return null;
	}

	@Override
	public List<TopUp> getPendingTopUp() {
		List<TopUp> listPendingTopUp = null;
		listPendingTopUp = topUpDao.getPendingTopUp();
		return listPendingTopUp;
	}
}
