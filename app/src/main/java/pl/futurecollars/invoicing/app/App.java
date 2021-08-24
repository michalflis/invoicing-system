package pl.futurecollars.invoicing.app;

import static pl.futurecollars.invoicing.app.MessageUtils.getMessage;
import static pl.futurecollars.invoicing.utilities.StringUtils.join;
import static pl.futurecollars.invoicing.utilities.StringUtils.split;

import org.apache.commons.text.WordUtils;
import pl.futurecollars.invoicing.list.LinkedList;

public class App {

  public static void main(String[] args) {
    LinkedList tokens;
    tokens = split(getMessage());
    String result = join(tokens);
    System.out.println(WordUtils.capitalize(result));
  }
}
