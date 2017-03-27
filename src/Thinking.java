public class Thinking implements State {
	private int initTime;
	private int timeInterval;
	
	@Override
	public Response recieveRequestFrom(Philosopher philosopher, Request packet, boolean isLeft) {
		Chopstick chopstick = philosopher.getChopstick(isLeft);
		philosopher.setChopstick(null, isLeft);
		if (isLeft && chopstick!=null) {
			System.out.println("I am giving away left chopstick");
		}else if (chopstick!=null){
			System.out.println("I am giving away right chopstick");
		}
		
		return new Response(chopstick);
	}

	@Override
	public void switchedTo(Philosopher philosopher) {
		System.out.println("I am thinking");
	}

	@Override
	public void tick(Philosopher philosopher, int currentTime) {
		if (initTime == 0) {
			initTime = currentTime;
			return;
		}
		int timePassed = currentTime - initTime;
		if (timePassed > timeInterval){
			philosopher.setState(new Hungry());
		}
	}


}
