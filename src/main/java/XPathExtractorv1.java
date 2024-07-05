import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

public class XPathExtractorv1 {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");

        WebDriver driver = new ChromeDriver();
        driver.get("https://www.example.com");

        List<WebElement> elements = driver.findElements(By.xpath("//*"));
        for (WebElement element : elements) {
            if (element.isDisplayed()) {
                String xpath = getElementXPath(driver, element);
                System.out.println(xpath);
            }
        }

        driver.quit();
    }

    public static String getElementXPath(WebDriver driver, WebElement element) {
        String js = "function absoluteXPath(element) {"
                + "var comp, comps = [];"
                + "var parent = null;"
                + "var xpath = '';"
                + "var getPos = function(element) {"
                + "var position = 1, curNode;"
                + "if (element.nodeType == Node.ATTRIBUTE_NODE) {"
                + "return null;"
                + "}"
                + "for (curNode = element.previousSibling; curNode; curNode = curNode.previousSibling) {"
                + "if (curNode.nodeName == element.nodeName) {"
                + "++position;"
                + "}"
                + "}"
                + "return position;"
                + "};"
                + "if (element instanceof Document) {"
                + "return '/';"
                + "}"
                + "for (; element && !(element instanceof Document); element = element.nodeType == Node.ATTRIBUTE_NODE ? element.ownerElement : element.parentNode) {"
                + "comp = comps[comps.length] = {};"
                + "switch (element.nodeType) {"
                + "case Node.TEXT_NODE:"
                + "comp.name = 'text()';"
                + "break;"
                + "case Node.ATTRIBUTE_NODE:"
                + "comp.name = '@' + element.nodeName;"
                + "break;"
                + "case Node.PROCESSING_INSTRUCTION_NODE:"
                + "comp.name = 'processing-instruction()';"
                + "break;"
                + "case Node.COMMENT_NODE:"
                + "comp.name = 'comment()';"
                + "break;"
                + "case Node.ELEMENT_NODE:"
                + "comp.name = element.nodeName;"
                + "break;"
                + "}"
                + "comp.position = getPos(element);"
                + "}"
                + "for (var i = comps.length - 1; i >= 0; i--) {"
                + "comp = comps[i];"
                + "xpath += '/' + comp.name.toLowerCase();"
                + "if (comp.position !== null) {"
                + "xpath += '[' + comp.position + ']';"
                + "}"
                + "}"
                + "return xpath;"
                + "} return absoluteXPath(arguments[0]);";
        return (String) ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(js, element);
    }
    public List<String> extractVisibleXpaths(String url) {
        WebDriverManager.chromedriver().driverVersion("126.0.6478.126").setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        ChromeDriver driver = new ChromeDriver(options);

        driver.get(url);

        List<WebElement> elements = driver.findElements(By.xpath("//*"));
        List<String> xpaths = new ArrayList<>();

        for (WebElement element : elements) {
            if (element.isDisplayed()) {
                String xpath = getElementXPath(driver, element);
                xpaths.add(xpath);
            }
        }
        return xpaths;
    }
    public void example(){
        // Configura WebDriverManager para manejar ChromeDriver
        WebDriverManager.chromedriver().driverVersion("126.0.6478.126").setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        //126.0.6478.127
        // Crea una instancia del navegador
        ChromeDriver driver = new ChromeDriver(options);

        try {
            // Abre la página web
            driver.get("https://es.wikipedia.org/wiki/Wikipedia:Portada");

            // Encuentra todos los elementos de interés (ejemplo: todos los links)
            List<WebElement> elements = driver.findElements(By.tagName("span"));

            // Itera sobre los elementos y obtiene su XPath
            for (WebElement element : elements) {
                String xpath = getElementXPath(driver, element);
                System.out.println("Elemento: " + element.getText() + " - XPath: " + xpath);
            }
        } finally {
            // Cierra el navegador
            driver.quit();
        }
    }

}
