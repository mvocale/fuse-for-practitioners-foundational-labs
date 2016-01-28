package org.fuse.usecase;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.globex.Account;
import org.globex.CorporateAccount;

/**
 * Aggregator implementation which extract the id and salescontact from
 * CorporateAccount and update the Account
 */
public class AccountAggregator implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {

		if (oldExchange == null) {
			return newExchange;
		}
		CorporateAccount corporateAccount;
		Account account;
		Object object = newExchange.getIn().getBody();
		if (object instanceof CorporateAccount) {
			corporateAccount = (CorporateAccount) object;
			account = oldExchange.getIn().getBody(Account.class);
		} else {
			corporateAccount = oldExchange.getIn().getBody(
					CorporateAccount.class);
			account = (Account) object;
		}
		account.setClientId(corporateAccount.getId());
		account.setSalesRepresentative(corporateAccount.getSalesContact());
		oldExchange.getIn().setBody(account);
		return oldExchange;
	}

}