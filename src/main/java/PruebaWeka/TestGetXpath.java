package PruebaWeka;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.List;

public class TestGetXpath {
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
            driver.get("https://www.mclibre.org/consultar/htmlcss/html/html-plantilla.html");

            WebElement dom = driver.findElement(By.xpath("/*"));
            String domContent = dom.getAttribute("outerHTML");
            // Imprimir el contenido del DOM
            //System.out.println(domContent);
            driver.quit();

            chatGPT(domContent);

            //System.out.println(domContent);

//            // Encuentra todos los elementos de interés (ejemplo: todos los links)
//            List<WebElement> elements = driver.findElements(By.tagName("span"));
//
//            // Itera sobre los elementos y obtiene su XPath
//            for (WebElement element : elements) {
//                String xpath = getXPath(driver, element);
//                System.out.println("Elemento: " + element.getText() + " - XPath: " + xpath);
//            }
        } finally {
            // Cierra el navegador
           // driver.quit();
        }
    }

    public static void chatGPT(String dom){
        OpenAIConnector openAIConnector = new OpenAIConnector();
        try {
            String prompt = "crea un poema sobre el atardecer ";
            String response = openAIConnector.getResponse(prompt);
            System.out.println("Respuesta de OpenAI: " + response);
        } catch (IOException e) {
            System.err.println("Error");
            e.printStackTrace();
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
