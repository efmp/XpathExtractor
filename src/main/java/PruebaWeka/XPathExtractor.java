package PruebaWeka;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

import java.io.File;
import java.io.IOException;

public class XPathExtractor {
    private static final String CHROME_DRIVER_PATH = "/path/to/chromedriver";
    private WebDriver driver;
    private Classifier classifier;

    public XPathExtractor() throws Exception {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        driver = new ChromeDriver();
        classifier = (Classifier) weka.core.SerializationHelper.read("path/to/model.model");
    }

    public void extractAndOptimize(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.body().getAllElements();

        for (Element element : elements) {
            String relativeXPath = generateRelativeXPath(element);
            boolean isValid = validateXPath(relativeXPath);

            if (isValid) {
                System.out.println("Valid XPath: " + relativeXPath);
            }
        }
    }

    private String generateRelativeXPath(Element element) {
        String tagName = element.tagName();
        Elements siblings = element.siblingElements();
        int index = siblings.indexOf(element) + 1;

        if (index > 1) {
            return "//" + tagName + "[" + index + "]";
        } else {
            return "//" + tagName;
        }
    }

    private boolean validateXPath(String xPath) {
        try {
            WebElement webElement = driver.findElement(By.xpath(xPath));
            return webElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

//    private double classifyXPath(String xPath) throws Exception {
//        Instance instance = new Instance(1.0, new double[]{/* feature values */});
//        return classifier.classifyInstance(instance);
//    }

    public static void main(String[] args) throws Exception {
        XPathExtractor extractor = new XPathExtractor();
        extractor.extractAndOptimize("http://example.com");
    }
}
