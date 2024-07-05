public class XPathConverter {
    public static String convertToRelativeXPath(String absoluteXPath) {
        // Implementa la lógica para convertir XPath absoluto a relativo
        return absoluteXPath.replaceFirst("/html/body", ".");
    }
}