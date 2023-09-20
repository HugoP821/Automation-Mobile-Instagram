package pages.base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.AndroidHelper;

import java.time.Duration;

public abstract class BasePage {
    protected final AndroidDriver driver;
    protected final WebDriverWait wait;
    protected final AndroidHelper helper;

    public BasePage(AndroidDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.helper = new AndroidHelper(driver);
        PageFactory.initElements(new AppiumFieldDecorator(this.driver, Duration.ofSeconds(10)), this);
    }

    protected BasePage(AndroidDriver driver, WebDriverWait wait, int waitTime){
        this.driver = driver;
        this.wait = wait;
        this.helper = new AndroidHelper(driver);
        PageFactory.initElements(new AppiumFieldDecorator(this.driver, Duration.ofSeconds(waitTime)), this);
    }
}