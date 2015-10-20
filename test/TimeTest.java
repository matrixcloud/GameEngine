import org.lwjgl.Sys;

public class TimeTest {
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		System.out.println(Sys.getTime());
		System.out.println(Sys.getTimerResolution());
	}
}
