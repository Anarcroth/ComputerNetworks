import java.util.UUID;

public class Account {

	private Integer pin;

	private UUID accountId;

	private boolean authorized;

	private String accountName;

	private Integer currentBalance;

	public Account() {

	}

	public Account(String accountName, Integer pin, Integer currentBalance) {

		this.pin = pin;
		this.accountName = accountName;
		this.currentBalance = currentBalance;

		accountId = UUID.randomUUID();
	}

	public Integer getPin() {

		return pin;
	}

	public void setPin(Integer pin) {

		this.pin = pin;
	}

	public UUID getAccountId() {

		return accountId;
	}

	public void setAccountId(UUID accountId) {

		this.accountId = accountId;
	}

	public boolean isAuthorized() {

		return authorized;
	}

	public void setAuthorized(boolean authorized) {

		this.authorized = authorized;
	}

	public String getAccountName() {

		return accountName;
	}

	public void setAccountName(String accountName) {

		this.accountName = accountName;
	}

	public Integer getCurrentBalance() {

		return currentBalance;
	}

	public void setCurrentBalance(Integer currentBalance) {

		this.currentBalance = currentBalance;
	}
}
