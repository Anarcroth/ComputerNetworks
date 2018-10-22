import java.util.UUID;

public class Account {

	private Integer pin;

	private Integer currentBalance;

	private UUID accountId;

	private String accountName;

	public Account() {

	}

	public Account(String accountName, Integer pin, Integer currentBalance) {

		this.pin = pin;
		this.accountName = accountName;
		this.currentBalance = currentBalance;

		accountId = UUID.randomUUID();
	}
}
