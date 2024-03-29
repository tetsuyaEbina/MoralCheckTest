package beans;

public class Result {
	private int id = 0;
	private int userId = 0; // アカウントID
	private double initialAbility = 0;
	private double ability = 0;
	private double answerAllTime = 0;

	public Result(int id, int userId, double initialAbility, double ability, double answerAllTime) {
		super();
		this.id = id;
		this.userId = userId;
		this.initialAbility = initialAbility;
		this.ability = ability;
		this.answerAllTime = answerAllTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public double getInitialAbility() {
		return initialAbility;
	}

	public void setInitialAbility(double initialAbility) {
		this.initialAbility = initialAbility;
	}

	public double getAbility() {
		return ability;
	}

	public void setAbility(double ability) {
		this.ability = ability;
	}

	public double getAnswerAllTime() {
		return answerAllTime;
	}

	public void setAnswerAllTime(double answerAllTime) {
		this.answerAllTime = answerAllTime;
	}
}