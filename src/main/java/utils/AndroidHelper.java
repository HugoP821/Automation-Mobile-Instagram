package utils;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.time.Duration;
import java.util.Map;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;

public class AndroidHelper {
    public static final int SHORT_SWIPE = 15;
    public static final int MEDIUM_SWIPE = 30;
    public static final int LONG_SWIPE = 45;
    private final AndroidDriver driver;
    private final String prefix;
    private int centerX;
    private int centerY;
    private TouchAction touch;
    private int height;
    private int width;

    public AndroidHelper(AndroidDriver driver){
        this.driver = driver;
        prefix = determinePrefix();
        width = driver.manage().window().getSize().getWidth();
        height = driver.manage().window().getSize().getHeight();
        centerX = width / 2;
        centerY = height / 2;
        touch = new TouchAction(driver);
    }

    public DesiredCapabilities getDesiredCapabilities() {
        @SuppressWarnings("unchecked")
        DesiredCapabilities caps = new DesiredCapabilities(
                (Map<String, ?>) driver.getCapabilities().getCapability("desired"));

        return caps;
    }

    private String determinePrefix() {
        return String.format("%s [%s]",
                getDesiredCapabilities().getCapability(MobileCapabilityType.DEVICE_NAME),
                getDesiredCapabilities().getCapability(MobileCapabilityType.UDID));
    }

    public String getPrefix() {
        return prefix;
    }
    public void swipe(int fromX, int fromY, int toX, int toY) {
        System.out.println("Pressing swipe from (" + fromX + "," + fromY + ") until (" + toX + "," + toY + ")");

        touch.press(point(fromX, fromY))
                .waitAction(waitOptions(Duration.ofSeconds(1)))
                .moveTo(point(toX, toY))
                .release()
                .perform();
    }
    public void swipe(int fromX, int fromY, SwipeDirection direction, int screenPercentage) {
        int toX;
        int toY;

        if (screenPercentage < 1 || screenPercentage > 100) {
            throw new IllegalArgumentException("the percentage must be between 1 and 100: " + screenPercentage + ".");
        }

        switch (direction) {
            case DOWN:
                toX = fromX;
                toY = fromY + (height / 100 * screenPercentage);
                break;
            case LEFT:
                toX = fromX - (width / 100 * screenPercentage);
                toY = fromY;
                break;
            case RIGHT:
                toX = fromX + (width / 100 * screenPercentage);
                toY = fromY;
                break;
            case UP:
                toX = fromX;
                toY = fromY - (height / 100 * screenPercentage);
                break;
            default:
                throw new IllegalStateException("Address not supported: " + direction);
        }

        swipe(fromX, fromY, toX, toY);
    }

    public void swipe(SwipeDirection direction) {
        swipe(centerX, centerY, direction, MEDIUM_SWIPE);
    }

    public void swipe(SwipeDirection direction, int percentage) {
        if (percentage < 1 || percentage > 100) {
            throw new IllegalArgumentException("The percentage must be between 1 and 100: " + percentage + ".");
        }

        swipe(centerX, centerY, direction, percentage);
    }

    public void swipe(AndroidElement element, SwipeDirection direction) {
        swipe(element, direction, MEDIUM_SWIPE);
    }

    public void swipe(AndroidElement element, SwipeDirection direction, int screenPercentage) {
        swipe(element.getCenter().x, element.getCenter().y, direction, screenPercentage);
    }

    public void waitSeconds(int seconds) {
        int miliSeconds = 1000;
        synchronized (driver) {
            try {
                driver.wait(seconds * miliSeconds);
            } catch (InterruptedException e) {
                System.err.println("Wait the driver interrupted. " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public enum SwipeDirection {
        DOWN,
        LEFT,
        RIGHT,
        UP
    }

}
