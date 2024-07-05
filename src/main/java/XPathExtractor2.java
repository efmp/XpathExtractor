import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class XPathExtractor2 {
    public static void main(String[] args) {
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
                String xpath = getXPath(driver, element);
                System.out.println("Elemento: " + element.getText() + " - XPath: " + xpath);
            }
        } finally {
            // Cierra el navegador
            driver.quit();
        }
    }

    // Método para obtener el XPath de un elemento
    public static String getXPath(WebDriver driver, WebElement element) {
        String script = "function getElementXPath(elt) {" +
                "var path = \"\";" +
                "for (; elt && elt.nodeType == 1; elt = elt.parentNode) {" +
                "idx = getElementIdx(elt);" +
                "xname = elt.tagName.toLowerCase();" +
                "if (idx > 1) xname += \"[\" + idx + \"]\";" +
                "path = \"/\" + xname + path;" +
                "}" +
                "return path;" +
                "}" +
                "function getElementIdx(elt) {" +
                "var count = 1;" +
                "for (var sib = elt.previousSibling; sib ; sib = sib.previousSibling) {" +
                "if(sib.nodeType == 1 && sib.tagName == elt.tagName) count++;" +
                "}" +
                "return count;" +
                "}" +
                "return getElementXPath(arguments[0]).toLowerCase();";

        return (String) ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(script, element);
    }
}
