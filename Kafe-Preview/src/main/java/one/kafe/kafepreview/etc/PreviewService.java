package one.kafe.kafepreview.etc;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

import javax.transaction.Transactional;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;

import lombok.RequiredArgsConstructor;
import one.kafe.kafepreview.repository.SUrlRepository;

@Service
@RequiredArgsConstructor
public class PreviewService {

	@Value("${selenium.host}")
	private String seleniumHost;

	@Value("${aws.bucket-name}")
	private String bucketName;

	private final AmazonS3 client;

	private final SUrlRepository sUrlRepository;

	public String generatePreview(String url) throws MalformedURLException {
		File file = takeScreenshot(url);
		String objectName = System.nanoTime() + "-" + UUID.randomUUID().toString() + ".png";
		client.putObject(bucketName, objectName, file);
		return client.getUrl(bucketName, objectName).toString();
	}

	@Transactional
	public void updatePreviewUrl(Long seq, String url) {
		sUrlRepository.updatePreviewBySeq(seq, url);
	}

	private File takeScreenshot(String url) throws MalformedURLException {
		ChromeOptions options = new ChromeOptions();
		options.setAcceptInsecureCerts(true);
		options.setHeadless(true);
		options.setCapability(CapabilityType.BROWSER_NAME, "chrome");
		options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
		WebDriver webDriver = new RemoteWebDriver(new URL(seleniumHost), options);
		webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		webDriver.get(url);
		Dimension dimension = new Dimension(1920, 1080);
		webDriver.manage().window().setSize(dimension);
		File screenshot = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);
		webDriver.quit();
		return screenshot;
	}

}
