import java.io.IOException;
import java.util.List;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException, IOException {
        XPathExtractorv1 extractor = new XPathExtractorv1();
        XPathClassifier classifier = new XPathClassifier("path/to/model.zip");
        XPathConverter converter = new XPathConverter();

        List<String> xpaths = extractor.extractVisibleXpaths("https://es.wikipedia.org/wiki/Wikipedia:Portada");
        for (String xpath : xpaths) {
            double[] classification = classifier.classify(xpath);
            String relativeXPath = converter.convertToRelativeXPath(xpath);
            System.out.println("Original: " + xpath);
            System.out.println("Relative: " + relativeXPath);
            System.out.println("Classification: " + Arrays.toString(classification));
        }
    }
}
