public enum Commands {

	START ("START"),
	CLOSE ("CLOSE"),
	AUTH ("AUTH"),
	BALANCE ("BALANCE"),
	CREDIT ("CREDIT"),
	DEBIT ("DEBIT"),
	PING ("PING");

	private final String state;

	private Commands(String s) {

		state = s;
	}

	@Override
	public String toString() {
		return state;
	}
}
