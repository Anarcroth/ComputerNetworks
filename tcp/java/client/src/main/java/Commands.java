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
			case 3: return AUTH;
			case 4: return BALANCE;
			case 5: return DEBIT;
			case 6: return CREDIT;
			default:
				throw new IllegalArgumentException("There is not such command");
		}
	}
}
