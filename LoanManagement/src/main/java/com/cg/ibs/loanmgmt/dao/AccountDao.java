package com.cg.ibs.loanmgmt.dao;

import java.math.BigInteger;

import com.cg.ibs.loanmgmt.bean.Account;

public interface AccountDao {
	Account getAccount(BigInteger accountNumber);
}
