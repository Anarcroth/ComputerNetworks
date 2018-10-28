public enum Commands {

	START,
	CLOSE,
	AUTH,
	BALANCE,
	CREDIT,
	DEBIT,
	PING;

	public static Commands getCommand(int index) {

		switch (index) {
			case 0: return PING;
			case 1: return START;
			case 2: return CLOSE;
			case 3: return BALANCE;
			case 4: return DEBIT;
			case 5: return CREDIT;
			default:
				throw new IllegalArgumentException("There is not such command");
		}
	}
}
